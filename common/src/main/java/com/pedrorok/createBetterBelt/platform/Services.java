package com.pedrorok.createBetterBelt.platform;

import com.pedrorok.createBetterBelt.Constants;
import com.pedrorok.createBetterBelt.platform.services.IPlatformHelper;

import java.util.ServiceLoader;

/**
 * @author Rok, Pedro Lucas nmm. 24/06/2026
 * @project tests-multi-platform
 */
public class Services {

    public static final IPlatformHelper PLATFORM = load(IPlatformHelper.class);
    public static <T> T load(Class<T> clazz) {
        final T loadedService = ServiceLoader.load(clazz).findFirst().orElseThrow(() -> new NullPointerException("Failed to load service for " + clazz.getName()));
        Constants.LOG.debug("Loaded {} for service {}", loadedService, clazz);
        return loadedService;
    }
}
