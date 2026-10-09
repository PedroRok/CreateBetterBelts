package com.pedrorok.createBetterBelt;

import net.fabricmc.api.ModInitializer;

/**
 * @author Rok, Pedro Lucas nmm. 24/06/2026
 * @project tests-multi-platform
 */
public class CreateBetterBelt implements ModInitializer {

    @Override
    public void onInitialize() {

        Constants.LOG.info("Hello Fabric world!");
        CommonClass.init();
    }
}
