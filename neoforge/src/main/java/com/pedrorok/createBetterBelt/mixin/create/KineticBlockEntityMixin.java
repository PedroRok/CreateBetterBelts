package com.pedrorok.createBetterBelt.mixin.create;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.pedrorok.createBetterBelt.content.belt.BeltReversal;
import com.pedrorok.createBetterBelt.content.belt.ReversibleBelt;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;

/**
 * @author Rok, Pedro Lucas nmm. 08/10/2026
 * @project create-better-belt
 */
@Mixin(value = KineticBlockEntity.class, remap = false)
public class KineticBlockEntityMixin {

	@ModifyReturnValue(method = "getSpeed", at = @At("RETURN"))
	private float cbb$reverseBelt(float speed) {
		return (Object) this instanceof ReversibleBelt belt && belt.cbb$isReversed() ? -speed : speed;
	}
}
