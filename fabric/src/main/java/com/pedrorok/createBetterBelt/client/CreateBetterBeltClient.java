package com.pedrorok.createBetterBelt.client;

import net.fabricmc.api.ClientModInitializer;

/**
 * @author Rok, Pedro Lucas nmm. 08/10/2026
 * @project create-better-belt
 */
public class CreateBetterBeltClient implements ClientModInitializer {

	@Override
	public void onInitializeClient() {
		CBBPartialModels.init();
	}
}
