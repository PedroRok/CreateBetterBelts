package com.pedrorok.createBetterBelt.content.inlinebelt;

import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.content.kinetics.belt.BeltBlock;
import com.simibubi.create.content.kinetics.belt.BeltBlockEntity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.IItemHandler;

/**
 * @author Rok, Pedro Lucas nmm. 08/10/2026
 * @project create-better-belt
 */
public class InlineBeltBlockEntity extends BeltBlockEntity {

	public InlineBeltBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	@Override
	public void initialize() {
		super.initialize();
		InlineBeltBlock.encaseShaftedSegment(level, worldPosition);
	}

	public IItemHandler getOrCreateItemHandler() {
		if (!BeltBlock.canTransportObjects(getBlockState()))
			return null;
		if (!isRemoved() && itemHandler == null)
			initializeItemHandler();
		return itemHandler;
	}

	@Override
	public float propagateRotationTo(KineticBlockEntity target, BlockState stateFrom, BlockState stateTo, BlockPos diff,
		boolean connectedViaAxes, boolean connectedViaCogs) {
		if (target instanceof BeltBlockEntity belt && getController().equals(belt.getController()))
			return 1;
		return super.propagateRotationTo(target, stateFrom, stateTo, diff, connectedViaAxes, connectedViaCogs);
	}
}
