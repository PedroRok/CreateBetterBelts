package com.pedrorok.createBetterBelt.content.inlinebelt;

import com.pedrorok.createBetterBelt.registry.CBBBlockEntityTypes;
import com.simibubi.create.content.kinetics.belt.BeltBlock;
import com.simibubi.create.content.kinetics.belt.BeltBlockEntity;
import com.simibubi.create.content.kinetics.belt.BeltBlockEntity.CasingType;
import com.simibubi.create.content.kinetics.belt.BeltPart;
import com.simibubi.create.content.kinetics.belt.BeltSlope;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

/**
 * @author Rok, Pedro Lucas nmm. 08/10/2026
 * @project create-better-belt
 */
public class InlineBeltBlock extends BeltBlock {

	public InlineBeltBlock(Properties properties) {
		super(properties);
	}

	@Override
	public Axis getRotationAxis(BlockState state) {
		if (state.getValue(SLOPE) == BeltSlope.SIDEWAYS)
			return Axis.Y;
		return state.getValue(HORIZONTAL_FACING)
			.getAxis();
	}

	@Override
	public BlockEntityType<? extends BeltBlockEntity> getBlockEntityType() {
		return CBBBlockEntityTypes.INLINE_BELT.get();
	}

	public static boolean isInlineBelt(BlockState state) {
		return state.getBlock() instanceof InlineBeltBlock;
	}

	public static boolean hasShaft(BlockState state) {
		return isInlineBelt(state) && state.getValue(PART) != BeltPart.MIDDLE;
	}

	public static void encaseShaftedSegment(Level level, BlockPos pos) {
		if (level.isClientSide)
			return;
		BlockState state = level.getBlockState(pos);
		if (!hasShaft(state))
			return;
		if (!(level.getBlockEntity(pos) instanceof BeltBlockEntity be) || be.casing != CasingType.NONE)
			return;

		be.setCasingType(CasingType.ANDESITE);
		((BeltBlock) state.getBlock()).updateCoverProperty(level, pos, level.getBlockState(pos));
	}

	public static Direction pulleyDirection(BlockState state, Direction fallback) {
		if (!isInlineBelt(state) || state.getValue(SLOPE) == BeltSlope.SIDEWAYS)
			return fallback;
		return state.getValue(HORIZONTAL_FACING);
	}

	@Override
	protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos,
		Player player, InteractionHand hand, BlockHitResult hitResult) {
		ItemInteractionResult result = super.useItemOn(stack, state, level, pos, player, hand, hitResult);
		encaseShaftedSegment(level, pos);
		return result;
	}
}
