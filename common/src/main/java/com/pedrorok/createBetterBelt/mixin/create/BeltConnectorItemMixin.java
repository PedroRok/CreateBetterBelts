package com.pedrorok.createBetterBelt.mixin.create;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.pedrorok.createBetterBelt.content.inlinebelt.CrossAxisBeltPlacement;
import com.pedrorok.createBetterBelt.content.inlinebelt.InlineBeltPlacement;
import com.simibubi.create.content.kinetics.belt.item.BeltConnectorItem;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

/**
 * @author Rok, Pedro Lucas nmm. 08/10/2026
 * @project create-better-belt
 */
@Mixin(BeltConnectorItem.class)
public class BeltConnectorItemMixin {

	@Inject(method = "canConnect", at = @At("HEAD"), cancellable = true)
	private static void cbb$allowInline(Level world, BlockPos first, BlockPos second,
		CallbackInfoReturnable<Boolean> cir) {
		if (InlineBeltPlacement.isInlineConnection(world, first, second)
			|| CrossAxisBeltPlacement.isCrossAxisConnection(world, first, second))
			cir.setReturnValue(true);
	}

	@Inject(method = "createBelts", at = @At("HEAD"), cancellable = true)
	private static void cbb$createInline(Level world, BlockPos start, BlockPos end, CallbackInfo ci) {
		if (InlineBeltPlacement.isInlineConnection(world, start, end)) {
			InlineBeltPlacement.createInlineBelts(world, start, end);
			ci.cancel();
			return;
		}
		if (CrossAxisBeltPlacement.isCrossAxisConnection(world, start, end)) {
			CrossAxisBeltPlacement.createCrossAxisBelts(world, start, end);
			ci.cancel();
		}
	}
}
