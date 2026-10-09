package com.pedrorok.createBetterBelt.mixin.create;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.pedrorok.createBetterBelt.compat.BeltIdentity;
import com.pedrorok.createBetterBelt.content.belt.BeltReversal;
import com.simibubi.create.AllItems;
import com.simibubi.create.content.kinetics.belt.BeltBlock;
import com.tterrag.registrate.util.entry.BlockEntry;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

/**
 * @author Rok, Pedro Lucas nmm. 08/10/2026
 * @project create-better-belt
 */
@Mixin(value = BeltBlock.class, remap = false)
public class BeltBlockMixin {

	private static final String BELT_ENTRY_HAS =
		"Lcom/tterrag/registrate/util/entry/BlockEntry;has(Lnet/minecraft/world/level/block/state/BlockState;)Z";

	@Redirect(method = { "initBelt", "getBeltChain", "canTransportObjects" },
		at = @At(value = "INVOKE", target = BELT_ENTRY_HAS))
	private static boolean cbb$anyBeltStatic(BlockEntry<?> entry, BlockState state) {
		return BeltIdentity.isBelt(entry, state);
	}

	@Redirect(method = { "onRemove", "updateEntityAfterFallOn" },
		at = @At(value = "INVOKE", target = BELT_ENTRY_HAS))
	private boolean cbb$anyBelt(BlockEntry<?> entry, BlockState state) {
		return BeltIdentity.isBelt(entry, state);
	}

	@Inject(method = "useItemOn", at = @At("HEAD"), cancellable = true)
	private void cbb$wrenchReverses(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player,
		InteractionHand hand, BlockHitResult hitResult, CallbackInfoReturnable<ItemInteractionResult> cir) {
		if (player.isShiftKeyDown() || !player.mayBuild() || !AllItems.WRENCH.isIn(stack))
			return;
		if (level.isClientSide) {
			cir.setReturnValue(ItemInteractionResult.SUCCESS);
			return;
		}
		if (!BeltReversal.toggleDirection(level, pos)) {
			cir.setReturnValue(ItemInteractionResult.FAIL);
			return;
		}
		level.playSound(null, pos, SoundEvents.ITEM_FRAME_ROTATE_ITEM, SoundSource.BLOCKS, .75f, 1.2f);
		cir.setReturnValue(ItemInteractionResult.SUCCESS);
	}
}
