package com.pedrorok.createBetterBelt.registry;

import com.pedrorok.createBetterBelt.Constants;
import com.simibubi.create.foundation.data.CreateRegistrate;

/**
 * @author Rok, Pedro Lucas nmm. 08/10/2026
 * @project create-better-belt
 */
public class CBBRegistrate {

	private static final CreateRegistrate REGISTRATE = CreateRegistrate.create(Constants.MOD_ID);

	public static CreateRegistrate get() {
		return REGISTRATE;
	}

	public static void buildEntries() {
		CBBBlocks.register();
		CBBBlockEntityTypes.register();
	}
}
