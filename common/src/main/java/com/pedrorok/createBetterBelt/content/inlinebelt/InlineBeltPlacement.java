package com.pedrorok.createBetterBelt.content.inlinebelt;

import java.util.LinkedList;
import java.util.List;

import com.pedrorok.createBetterBelt.registry.CBBBlocks;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.content.kinetics.belt.BeltBlock;
import com.simibubi.create.content.kinetics.belt.BeltBlockEntity.CasingType;
import com.simibubi.create.content.kinetics.belt.BeltPart;
import com.simibubi.create.content.kinetics.belt.BeltSlope;
import com.simibubi.create.content.kinetics.belt.item.BeltConnectorItem;
import com.simibubi.create.content.kinetics.simpleRelays.AbstractSimpleShaftBlock;
import com.simibubi.create.content.kinetics.simpleRelays.ShaftBlock;
import com.simibubi.create.foundation.block.ProperWaterloggedBlock;

import net.createmod.catnip.math.VecHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.core.Direction.AxisDirection;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

/**
 * @author Rok, Pedro Lucas nmm. 08/10/2026
 * @project create-better-belt
 */
public class InlineBeltPlacement {

	public static Axis acrossFrom(Axis shaftAxis) {
		return shaftAxis == Axis.X ? Axis.Z : Axis.X;
	}

	public static boolean isAlongShaft(BlockPos first, BlockPos second, Axis shaftAxis) {
		if (shaftAxis.isVertical())
			return false;
		BlockPos diff = second.subtract(first);
		int along = Math.abs(shaftAxis.choose(diff.getX(), 0, diff.getZ()));
		int across = Math.abs(acrossFrom(shaftAxis).choose(diff.getX(), 0, diff.getZ()));
		return along > across;
	}

	public static boolean isInlineConnection(Level level, BlockPos first, BlockPos second) {
		if (level == null || first == null || second == null || first.equals(second))
			return false;
		if (!level.isLoaded(first) || !level.isLoaded(second))
			return false;

		BlockState firstState = level.getBlockState(first);
		BlockState secondState = level.getBlockState(second);
		if (!ShaftBlock.isShaft(firstState) || !ShaftBlock.isShaft(secondState))
			return false;

		Axis axis = firstState.getValue(AbstractSimpleShaftBlock.AXIS);
		if (axis != secondState.getValue(AbstractSimpleShaftBlock.AXIS))
			return false;
		if (axis.isVertical())
			return false;
		if (!second.closerThan(first, BeltConnectorItem.maxLength()))
			return false;

		BlockPos diff = second.subtract(first);
		int along = axis.choose(diff.getX(), 0, diff.getZ());
		int sideways = (axis == Axis.X ? Axis.Z : Axis.X).choose(diff.getX(), 0, diff.getZ());
		int vertical = diff.getY();

		if (along == 0 || sideways != 0)
			return false;
		if (vertical != 0 && Math.abs(vertical) != Math.abs(along))
			return false;

		if (!(level.getBlockEntity(first) instanceof KineticBlockEntity firstKinetic))
			return false;
		if (!(level.getBlockEntity(second) instanceof KineticBlockEntity secondKinetic))
			return false;

		float firstSpeed = firstKinetic.getTheoreticalSpeed();
		float secondSpeed = secondKinetic.getTheoreticalSpeed();
		if (firstSpeed != 0 && secondSpeed != 0 && Math.signum(firstSpeed) != Math.signum(secondSpeed))
			return false;

		return isPathClear(level, first, second, axis);
	}

	static boolean isPathClear(Level level, BlockPos first, BlockPos second, Axis axis) {
		BlockPos diff = second.subtract(first);
		BlockPos step = new BlockPos((int) Math.signum(diff.getX()), (int) Math.signum(diff.getY()),
			(int) Math.signum(diff.getZ()));

		int limit = 1000;
		for (BlockPos current = first.offset(step); !current.equals(second) && limit-- > 0; current =
			current.offset(step)) {
			BlockState state = level.getBlockState(current);
			if (ShaftBlock.isShaft(state) && state.getValue(AbstractSimpleShaftBlock.AXIS) == axis)
				continue;
			if (!state.canBeReplaced())
				return false;
		}
		return true;
	}

	public static boolean createInlineBelts(Level level, BlockPos start, BlockPos end, CasingType casing) {
		BlockState startState = level.getBlockState(start);
		Axis axis = startState.getValue(AbstractSimpleShaftBlock.AXIS);
		BlockPos diff = end.subtract(start);

		int along = axis.choose(diff.getX(), 0, diff.getZ());
		Direction facing = Direction.get(along > 0 ? AxisDirection.POSITIVE : AxisDirection.NEGATIVE, axis);
		BeltSlope slope = diff.getY() == 0 ? BeltSlope.HORIZONTAL
			: diff.getY() > 0 ? BeltSlope.UPWARD : BeltSlope.DOWNWARD;

		level.playSound(null, BlockPos.containing(VecHelper.getCenterOf(start.offset(end))
			.scale(.5f)), SoundEvents.WOOL_PLACE, SoundSource.BLOCKS, .5f, 1f);

		List<BlockPos> segments = chainBetween(start, end, slope, facing);
		BlockState belt = CBBBlocks.INLINE_BELT.getDefaultState();
		boolean failed = false;

		for (BlockPos pos : segments) {
			BlockState existing = level.getBlockState(pos);
			if (existing.getDestroySpeed(level, pos) == -1) {
				failed = true;
				break;
			}

			BeltPart part = pos.equals(start) ? BeltPart.START : pos.equals(end) ? BeltPart.END : BeltPart.MIDDLE;
			if (part == BeltPart.MIDDLE && ShaftBlock.isShaft(existing)
				&& existing.getValue(AbstractSimpleShaftBlock.AXIS) == axis)
				part = BeltPart.PULLEY;

			if (!existing.canBeReplaced())
				level.destroyBlock(pos, false);

			boolean shafted = part != BeltPart.MIDDLE;

			KineticBlockEntity.switchToBlockState(level, pos, ProperWaterloggedBlock.withWater(level,
				belt.setValue(BeltBlock.SLOPE, slope)
					.setValue(BeltBlock.PART, part)
					.setValue(BeltBlock.CASING, shafted)
					.setValue(InlineBeltBlock.BRASS, shafted && casing == CasingType.BRASS)
					.setValue(BeltBlock.HORIZONTAL_FACING, facing),
				pos));

			if (shafted)
				InlineBeltBlock.encaseShaftedSegment(level, pos, casing);
		}

		if (!failed)
			return true;

		for (BlockPos pos : segments)
			if (level.getBlockState(pos)
				.getBlock() instanceof InlineBeltBlock)
				level.destroyBlock(pos, false);
		return false;
	}

	static List<BlockPos> chainBetween(BlockPos start, BlockPos end, BeltSlope slope, Direction facing) {
		List<BlockPos> positions = new LinkedList<>();
		BlockPos current = start;
		int limit = 1000;

		while (!current.equals(end) && limit-- > 0) {
			positions.add(current);
			current = current.relative(facing);
			if (slope != BeltSlope.HORIZONTAL)
				current = current.above(slope == BeltSlope.UPWARD ? 1 : -1);
		}

		positions.add(end);
		return positions;
	}

	static boolean isInlineBelt(BlockEntity be) {
		return be instanceof InlineBeltBlockEntity;
	}
}
