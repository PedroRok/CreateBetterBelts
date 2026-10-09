package com.pedrorok.createBetterBelt.registry;

import com.pedrorok.createBetterBelt.mixin.create.BeltBlockEntityAccessor;
import com.simibubi.create.content.kinetics.belt.BeltBlock;

import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.items.IItemHandler;

/**
 * @author Rok, Pedro Lucas nmm. 08/10/2026
 * @project create-better-belt
 */
public class CBBCapabilities {

	public static void register(RegisterCapabilitiesEvent event) {
		event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, CBBBlockEntityTypes.INLINE_BELT.get(), (be, context) -> {
			if (!BeltBlock.canTransportObjects(be.getBlockState()))
				return null;
			BeltBlockEntityAccessor accessor = (BeltBlockEntityAccessor) be;
			if (!be.isRemoved() && accessor.cbb$getItemHandler() == null)
				accessor.cbb$initializeItemHandler();
			return (IItemHandler) accessor.cbb$getItemHandler();
		});
	}
}
