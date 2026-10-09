package com.pedrorok.createBetterBelt.data;

import com.pedrorok.createBetterBelt.registry.CBBRegistrate;

import io.github.fabricators_of_create.porting_lib.data.ExistingFileHelper;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

/**
 * @author Rok, Pedro Lucas nmm. 08/10/2026
 * @project create-better-belt
 */
public class CreateBetterBeltDatagen implements DataGeneratorEntrypoint {

	@Override
	public void onInitializeDataGenerator(FabricDataGenerator generator) {
		CBBRegistrate.get().onData(generator.createPack(), ExistingFileHelper.withResourcesFromArg());
	}
}
