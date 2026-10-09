package com.pedrorok.createBetterBelt;

import com.pedrorok.createBetterBelt.platform.Services;

/**
 * @author Rok, Pedro Lucas nmm. 24/06/2026
 * @project tests-multi-platform
 */
public class CommonClass {

	public static void init() {
		Constants.LOG.info("{} loading on {}", Constants.MOD_NAME, Services.PLATFORM.getPlatformName());
	}
}
