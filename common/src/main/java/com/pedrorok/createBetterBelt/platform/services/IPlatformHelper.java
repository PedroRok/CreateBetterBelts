package com.pedrorok.createBetterBelt.platform.services;

import com.pedrorok.createBetterBelt.content.inlinebelt.InlineBeltBlock;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.tterrag.registrate.builders.BlockBuilder;
import com.tterrag.registrate.util.nullness.NonNullUnaryOperator;

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

	NonNullUnaryOperator<BlockBuilder<InlineBeltBlock, CreateRegistrate>> inlineBeltPlatformSetup();
}
