package com.pedrorok.createBetterBelt.registry;

import com.pedrorok.createBetterBelt.Constants;
import com.simibubi.create.foundation.data.CreateRegistrate;

import net.neoforged.bus.api.IEventBus;

/**
 * @author Rok, Pedro Lucas nmm. 08/10/2026
 * @project create-better-belt
 */
public class CBBRegistrate {

	private static final CreateRegistrate REGISTRATE = CreateRegistrate.create(Constants.MOD_ID);

	public static CreateRegistrate get() {
		return REGISTRATE;
	}

	public static void register(IEventBus modEventBus) {
		REGISTRATE.registerEventListeners(modEventBus);

		CBBBlocks.register();
		CBBBlockEntityTypes.register();
	}
}
