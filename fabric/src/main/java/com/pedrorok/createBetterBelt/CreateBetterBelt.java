package com.pedrorok.createBetterBelt;

import com.pedrorok.createBetterBelt.registry.CBBRegistrate;

import net.fabricmc.api.ModInitializer;

/**
 * @author Rok, Pedro Lucas nmm. 24/06/2026
 * @project tests-multi-platform
 */
public class CreateBetterBelt implements ModInitializer {

	@Override
	public void onInitialize() {
		CommonClass.init();

		CBBRegistrate.buildEntries();
		CBBRegistrate.get().register();
	}
}
