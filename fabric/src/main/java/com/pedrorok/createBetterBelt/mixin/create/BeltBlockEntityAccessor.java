package com.pedrorok.createBetterBelt.mixin.create;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

import com.simibubi.create.content.kinetics.belt.BeltBlockEntity;

import io.github.fabricators_of_create.porting_lib.transfer.item.SlottedStackStorage;

/**
 * @author Rok, Pedro Lucas nmm. 09/10/2026
 * @project create-better-belt
 */
@Mixin(BeltBlockEntity.class)
public interface BeltBlockEntityAccessor {

	@Accessor(value = "itemHandler", remap = false)
	SlottedStackStorage cbb$getItemHandler();

	@Invoker(value = "initializeItemHandler", remap = false)
	void cbb$initializeItemHandler();
}
