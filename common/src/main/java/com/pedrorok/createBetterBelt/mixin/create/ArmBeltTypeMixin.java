package com.pedrorok.createBetterBelt.mixin.create;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import com.pedrorok.createBetterBelt.compat.BeltIdentity;
import com.simibubi.create.content.kinetics.mechanicalArm.AllArmInteractionPointTypes;
import com.tterrag.registrate.util.entry.BlockEntry;

import net.minecraft.world.level.block.state.BlockState;

/**
 * @author Rok, Pedro Lucas nmm. 23/10/2026
 * @project create-better-belt
 */
@Mixin(AllArmInteractionPointTypes.BeltType.class)
public class ArmBeltTypeMixin {

	@Redirect(method = "canCreatePoint", remap = false, at = @At(value = "INVOKE", remap = true,
		target = "Lcom/tterrag/registrate/util/entry/BlockEntry;has(Lnet/minecraft/world/level/block/state/BlockState;)Z"))
	private boolean cbb$anyBelt(BlockEntry<?> entry, BlockState state) {
		return BeltIdentity.isBelt(entry, state);
	}
}
