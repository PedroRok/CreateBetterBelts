package com.pedrorok.createBetterBelt.registry;

import com.pedrorok.createBetterBelt.mixin.create.BeltBlockEntityAccessor;
import com.simibubi.create.content.kinetics.belt.BeltBlock;

import net.fabricmc.fabric.api.transfer.v1.item.ItemStorage;

/**
 * @author Rok, Pedro Lucas nmm. 08/10/2026
 * @project create-better-belt
 */
public class CBBItemStorage {

	public static void register() {
		ItemStorage.SIDED.registerForBlockEntity((be, direction) -> {
			if (!BeltBlock.canTransportObjects(be.getBlockState()))
				return null;
			BeltBlockEntityAccessor accessor = (BeltBlockEntityAccessor) be;
			if (!be.isRemoved() && accessor.cbb$getItemHandler() == null)
				accessor.cbb$initializeItemHandler();
			return accessor.cbb$getItemHandler();
		}, CBBBlockEntityTypes.INLINE_BELT.get());
	}
}
