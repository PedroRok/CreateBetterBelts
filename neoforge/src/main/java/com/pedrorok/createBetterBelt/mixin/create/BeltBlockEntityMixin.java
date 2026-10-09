package com.pedrorok.createBetterBelt.mixin.create;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.pedrorok.createBetterBelt.compat.BeltIdentity;
import com.pedrorok.createBetterBelt.content.belt.ReversibleBelt;
import com.simibubi.create.content.kinetics.belt.BeltBlockEntity;
import com.tterrag.registrate.util.entry.BlockEntry;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

/**
 * @author Rok, Pedro Lucas nmm. 08/10/2026
 * @project create-better-belt
 */
@Mixin(value = BeltBlockEntity.class, remap = false)
public class BeltBlockEntityMixin implements ReversibleBelt {

	@Unique
	private boolean cbb$reversed;

	@Redirect(method = { "tick", "hasPulley" }, at = @At(value = "INVOKE",
		target = "Lcom/tterrag/registrate/util/entry/BlockEntry;has(Lnet/minecraft/world/level/block/state/BlockState;)Z"))
	private boolean cbb$anyBelt(BlockEntry<?> entry, BlockState state) {
		return BeltIdentity.isBelt(entry, state);
	}

	@Override
	public boolean cbb$isReversed() {
		return cbb$reversed;
	}

	@Override
	public void cbb$setReversed(boolean reversed) {
		if (cbb$reversed == reversed)
			return;
		cbb$reversed = reversed;
		cbb$self().notifyUpdate();
	}

	@Inject(method = "write", at = @At("HEAD"))
	private void cbb$write(CompoundTag tag, HolderLookup.Provider registries, boolean clientPacket, CallbackInfo ci) {
		tag.putBoolean("Reversed", cbb$reversed);
	}

	@Inject(method = "read", at = @At("HEAD"))
	private void cbb$read(CompoundTag tag, HolderLookup.Provider registries, boolean clientPacket, CallbackInfo ci) {
		cbb$reversed = tag.getBoolean("Reversed");
	}

	@Inject(method = "setController", at = @At("TAIL"))
	private void cbb$adoptControllerDirection(BlockPos controller, CallbackInfo ci) {
		BeltBlockEntity self = cbb$self();
		Level level = self.getLevel();
		if (level == null || self.isController())
			return;
		if (level.getBlockEntity(controller) instanceof ReversibleBelt c)
			cbb$setReversed(c.cbb$isReversed());
	}

	@Unique
	private BeltBlockEntity cbb$self() {
		return (BeltBlockEntity) (Object) this;
	}
}
