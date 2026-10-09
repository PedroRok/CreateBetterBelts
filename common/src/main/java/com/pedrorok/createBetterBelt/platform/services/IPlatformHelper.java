package com.pedrorok.createBetterBelt.platform.services;

/**
 * @author Rok, Pedro Lucas nmm. 24/06/2026
 * @project tests-multi-platform
 */
public interface IPlatformHelper {

    String getPlatformName();

    boolean isModLoaded(String modId);

    boolean isDevelopmentEnvironment();

    default String getEnvironmentName() {
        return isDevelopmentEnvironment() ? "development" : "production";
    }
}
