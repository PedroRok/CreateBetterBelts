package com.pedrorok.createBetterBelt;

import net.minecraftforge.fml.common.Mod;

/**
 * @author Rok, Pedro Lucas nmm. 24/06/2026
 * @project tests-multi-platform
 */
@Mod(Constants.MOD_ID)
public class CreateBetterBelt {

    public CreateBetterBelt() {
        Constants.LOG.info("Hello Forge world!");
        CommonClass.init();

    }
}
