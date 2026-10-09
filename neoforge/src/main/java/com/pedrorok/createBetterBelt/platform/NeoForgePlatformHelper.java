package com.pedrorok.createBetterBelt.platform;

import com.pedrorok.createBetterBelt.content.inlinebelt.InlineBeltBlock;
import com.pedrorok.createBetterBelt.content.inlinebelt.InlineBeltGenerator;
import com.pedrorok.createBetterBelt.platform.services.IPlatformHelper;
import com.simibubi.create.content.kinetics.belt.BeltBlock;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.tterrag.registrate.builders.BlockBuilder;
import com.tterrag.registrate.util.nullness.NonNullUnaryOperator;

import net.neoforged.fml.ModList;
import net.neoforged.fml.loading.FMLLoader;

/**
 * @author Rok, Pedro Lucas nmm. 24/06/2026
 * @project create-better-belt
 */
public class NeoForgePlatformHelper implements IPlatformHelper {

	@Override
	public String getPlatformName() {
		return "NeoForge";
	}

	@Override
	public boolean isModLoaded(String modId) {
		return ModList.get().isLoaded(modId);
	}

	@Override
	public boolean isDevelopmentEnvironment() {
		return !FMLLoader.isProduction();
	}

	@Override
	public NonNullUnaryOperator<BlockBuilder<InlineBeltBlock, CreateRegistrate>> inlineBeltPlatformSetup() {
		return builder -> builder.blockstate(new InlineBeltGenerator()::generate)
			.clientExtension(() -> () -> new BeltBlock.RenderProperties());
	}
}
