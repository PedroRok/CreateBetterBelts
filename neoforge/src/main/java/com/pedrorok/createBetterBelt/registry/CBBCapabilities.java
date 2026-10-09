package com.pedrorok.createBetterBelt.registry;

import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;

/**
 * @author Rok, Pedro Lucas nmm. 08/10/2026
 * @project create-better-belt
 */
public class CBBCapabilities {

	public static void register(RegisterCapabilitiesEvent event) {
		event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, CBBBlockEntityTypes.INLINE_BELT.get(),
			(be, context) -> be.getOrCreateItemHandler());
	}
}
