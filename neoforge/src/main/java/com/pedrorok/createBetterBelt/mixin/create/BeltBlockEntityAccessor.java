package com.pedrorok.createBetterBelt.mixin.create;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

import com.simibubi.create.content.kinetics.belt.BeltBlockEntity;

import net.neoforged.neoforge.items.IItemHandler;

/**
 * @author Rok, Pedro Lucas nmm. 09/10/2026
 * @project create-better-belt
 */
@Mixin(BeltBlockEntity.class)
public interface BeltBlockEntityAccessor {

	@Accessor("itemHandler")
	IItemHandler cbb$getItemHandler();

	@Invoker("initializeItemHandler")
	void cbb$initializeItemHandler();
}
