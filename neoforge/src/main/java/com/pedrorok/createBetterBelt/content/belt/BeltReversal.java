package com.pedrorok.createBetterBelt.content.belt;

import java.util.List;

import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.content.kinetics.belt.BeltBlock;
import com.simibubi.create.content.kinetics.belt.BeltBlockEntity;
import com.simibubi.create.content.kinetics.belt.BeltHelper;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

/**
 * @author Rok, Pedro Lucas nmm. 08/10/2026
 * @project create-better-belt
 */
public class BeltReversal {

	public static boolean toggleDirection(Level level, BlockPos pos) {
		BeltBlockEntity controller = BeltHelper.getControllerBE(level, pos);
		if (controller == null)
			return false;

		List<BlockPos> chain = BeltBlock.getBeltChain(level, controller.getBlockPos());
		if (chain.isEmpty())
			return false;

		for (BlockPos segment : chain) {
			if (!(level.getBlockEntity(segment) instanceof ReversibleBelt be))
				continue;
			be.cbb$setReversed(!be.cbb$isReversed());
		}
		return true;
	}

	public static boolean isReversed(KineticBlockEntity be) {
		return be instanceof ReversibleBelt belt && belt.cbb$isReversed();
	}

	public static float shaftSpeedOf(KineticBlockEntity be) {
		float speed = be.getSpeed();
		return isReversed(be) ? -speed : speed;
	}
}
