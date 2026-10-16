package com.pedrorok.createBetterBelt;

import com.pedrorok.createBetterBelt.client.CBBPartialModels;
import com.pedrorok.createBetterBelt.registry.CBBRegistrate;

import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;

/**
 * @author Rok, Pedro Lucas nmm. 24/06/2026
 * @project tests-multi-platform
 */
@Mod(Constants.MOD_ID)
public class CreateBetterBelt {

	public CreateBetterBelt() {
		IEventBus eventBus = FMLJavaModLoadingContext.get()
			.getModEventBus();

		CommonClass.init();

		CBBRegistrate.get().registerEventListeners(eventBus);
		CBBRegistrate.buildEntries();

		if (FMLEnvironment.dist.isClient())
			CBBPartialModels.init();
	}
}
