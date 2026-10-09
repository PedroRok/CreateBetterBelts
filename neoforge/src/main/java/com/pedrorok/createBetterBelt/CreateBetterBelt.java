package com.pedrorok.createBetterBelt;

import com.pedrorok.createBetterBelt.client.CBBPartialModels;
import com.pedrorok.createBetterBelt.registry.CBBCapabilities;
import com.pedrorok.createBetterBelt.registry.CBBRegistrate;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLEnvironment;

/**
 * @author Rok, Pedro Lucas nmm. 24/06/2026
 * @project tests-multi-platform
 */
@Mod(Constants.MOD_ID)
public class CreateBetterBelt {

	public CreateBetterBelt(IEventBus eventBus) {
		CommonClass.init();

		CBBRegistrate.register(eventBus);
		eventBus.addListener(CBBCapabilities::register);

		if (FMLEnvironment.dist.isClient())
			CBBPartialModels.init();
	}
}
