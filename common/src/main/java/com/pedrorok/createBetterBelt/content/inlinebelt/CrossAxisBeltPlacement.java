package com.pedrorok.createBetterBelt.content.inlinebelt;

import java.util.List;

import com.pedrorok.createBetterBelt.registry.CBBBlocks;
import com.simibubi.create.AllBlocks;
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
import net.minecraft.world.level.block.state.BlockState;

/**
 * @author Rok, Pedro Lucas nmm. 08/10/2026
 * @project create-better-belt
 */
public class CrossAxisBeltPlacement {

	public static boolean isCrossAxisConnection(Level level, BlockPos first, BlockPos second) {
		if (level == null || first == null || second == null || first.equals(second))
			return false;
		if (!level.isLoaded(first) || !level.isLoaded(second))
			return false;

		BlockState firstState = level.getBlockState(first);
		BlockState secondState = level.getBlockState(second);
		if (!ShaftBlock.isShaft(firstState) || !ShaftBlock.isShaft(secondState))
			return false;

		Axis firstAxis = firstState.getValue(AbstractSimpleShaftBlock.AXIS);
		Axis secondAxis = secondState.getValue(AbstractSimpleShaftBlock.AXIS);

		if (firstAxis == secondAxis || firstAxis.isVertical() || secondAxis.isVertical())
			return false;
		if (!second.closerThan(first, BeltConnectorItem.maxLength()))
			return false;

		BlockPos diff = second.subtract(first);
		Axis path = pathAxis(diff);
		if (path == null)
			return false;

		int along = path.choose(diff.getX(), 0, diff.getZ());
		if (diff.getY() != 0 && Math.abs(diff.getY()) != Math.abs(along))
			return false;

		if (!(level.getBlockEntity(first) instanceof KineticBlockEntity firstKinetic))
			return false;
		if (!(level.getBlockEntity(second) instanceof KineticBlockEntity secondKinetic))
			return false;

		float firstSpeed = firstKinetic.getTheoreticalSpeed();
		float secondSpeed = secondKinetic.getTheoreticalSpeed();
		if (firstSpeed != 0 && secondSpeed != 0 && Math.signum(firstSpeed) != Math.signum(secondSpeed))
			return false;

		return isPathClear(level, first, second);
	}

	private static Axis pathAxis(BlockPos diff) {
		if (diff.getX() != 0 && diff.getZ() == 0)
			return Axis.X;
		if (diff.getZ() != 0 && diff.getX() == 0)
			return Axis.Z;
		return null;
	}

	private static boolean isPathClear(Level level, BlockPos first, BlockPos second) {
		BlockPos diff = second.subtract(first);
		BlockPos step = new BlockPos((int) Math.signum(diff.getX()), (int) Math.signum(diff.getY()),
			(int) Math.signum(diff.getZ()));

		int limit = 1000;
		for (BlockPos current = first.offset(step); !current.equals(second) && limit-- > 0; current =
			current.offset(step)) {
			BlockState state = level.getBlockState(current);
			if (ShaftBlock.isShaft(state) && !state.getValue(AbstractSimpleShaftBlock.AXIS)
				.isVertical())
				continue;
			if (!state.canBeReplaced())
				return false;
		}
		return true;
	}

	public static boolean createCrossAxisBelts(Level level, BlockPos start, BlockPos end, CasingType casing) {
		BlockPos diff = end.subtract(start);
		Axis path = pathAxis(diff);
		int along = path.choose(diff.getX(), 0, diff.getZ());
		Direction facing = Direction.get(along > 0 ? AxisDirection.POSITIVE : AxisDirection.NEGATIVE, path);
		BeltSlope slope = diff.getY() == 0 ? BeltSlope.HORIZONTAL
			: diff.getY() > 0 ? BeltSlope.UPWARD : BeltSlope.DOWNWARD;

		level.playSound(null, BlockPos.containing(VecHelper.getCenterOf(start.offset(end))
			.scale(.5f)), SoundEvents.WOOL_PLACE, SoundSource.BLOCKS, .5f, 1f);

		List<BlockPos> segments = InlineBeltPlacement.chainBetween(start, end, slope, facing);
		boolean failed = false;

		for (BlockPos pos : segments) {
			BlockState existing = level.getBlockState(pos);
			if (existing.getDestroySpeed(level, pos) == -1) {
				failed = true;
				break;
			}

			BeltPart part = pos.equals(start) ? BeltPart.START : pos.equals(end) ? BeltPart.END : BeltPart.MIDDLE;
			if (part == BeltPart.MIDDLE && ShaftBlock.isShaft(existing))
				part = BeltPart.PULLEY;

			boolean inline = part != BeltPart.MIDDLE && ShaftBlock.isShaft(existing)
				&& existing.getValue(AbstractSimpleShaftBlock.AXIS) == path;
			BlockState belt = inline
				? CBBBlocks.INLINE_BELT.getDefaultState()
					.setValue(InlineBeltBlock.BRASS, casing == CasingType.BRASS)
				: AllBlocks.BELT.getDefaultState();

			if (!existing.canBeReplaced())
				level.destroyBlock(pos, false);

			KineticBlockEntity.switchToBlockState(level, pos, ProperWaterloggedBlock.withWater(level,
				belt.setValue(BeltBlock.SLOPE, slope)
					.setValue(BeltBlock.PART, part)
					.setValue(BeltBlock.CASING, inline)
					.setValue(BeltBlock.HORIZONTAL_FACING, facing),
				pos));

			if (inline)
				InlineBeltBlock.encaseShaftedSegment(level, pos, casing);
		}

		if (!failed)
			return true;

		for (BlockPos pos : segments)
			if (level.getBlockState(pos)
				.getBlock() instanceof BeltBlock)
				level.destroyBlock(pos, false);
		return false;
	}
}
