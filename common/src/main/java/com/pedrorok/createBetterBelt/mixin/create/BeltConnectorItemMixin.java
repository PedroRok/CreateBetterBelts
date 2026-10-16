package com.pedrorok.createBetterBelt.mixin.create;

import com.simibubi.create.content.kinetics.belt.BeltBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.pedrorok.createBetterBelt.content.inlinebelt.CrossAxisBeltPlacement;
import com.pedrorok.createBetterBelt.content.inlinebelt.InlineBeltCasing;
import com.pedrorok.createBetterBelt.content.inlinebelt.InlineBeltPlacement;
import com.simibubi.create.content.kinetics.belt.item.BeltConnectorItem;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;

/**
 * @author Rok, Pedro Lucas nmm. 08/10/2026
 * @project create-better-belt
 */
@Mixin(BeltConnectorItem.class)
public class BeltConnectorItemMixin {

	@Inject(method = "canConnect", at = @At("HEAD"), cancellable = true, remap = false)
	private static void cbb$allowInline(Level world, BlockPos first, BlockPos second,
		CallbackInfoReturnable<Boolean> cir) {
		if (InlineBeltPlacement.isInlineConnection(world, first, second)
			|| CrossAxisBeltPlacement.isCrossAxisConnection(world, first, second))
			cir.setReturnValue(true);
	}

	@WrapOperation(method = "useOn", at = @At(value = "INVOKE", target = "Lcom/simibubi/create/content/kinetics/belt/item/BeltConnectorItem;createBelts(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/core/BlockPos;)V"))
	private void cbb$createWithCasing(Level world, BlockPos start, BlockPos end, Operation<Void> original,
		@Local(argsOnly = true) UseOnContext context) {
		if (!InlineBeltCasing.place(world, start, end, context.getPlayer(), context.getHand()))
			original.call(world, start, end);
	}

	@Inject(method = "createBelts", at = @At("HEAD"), cancellable = true, remap = false)
	private static void cbb$createInline(Level world, BlockPos start, BlockPos end, CallbackInfo ci) {
		if (InlineBeltPlacement.isInlineConnection(world, start, end)) {
			InlineBeltPlacement.createInlineBelts(world, start, end, BeltBlockEntity.CasingType.ANDESITE);
			ci.cancel();
			return;
		}
		if (CrossAxisBeltPlacement.isCrossAxisConnection(world, start, end)) {
			CrossAxisBeltPlacement.createCrossAxisBelts(world, start, end, BeltBlockEntity.CasingType.ANDESITE);
			ci.cancel();
		}
	}
}
