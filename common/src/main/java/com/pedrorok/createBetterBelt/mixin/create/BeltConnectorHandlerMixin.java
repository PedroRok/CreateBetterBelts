package com.pedrorok.createBetterBelt.mixin.create;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import com.pedrorok.createBetterBelt.content.inlinebelt.InlineBeltPlacement;
import com.simibubi.create.AllDataComponents;
import com.simibubi.create.AllItems;
import com.simibubi.create.content.kinetics.belt.item.BeltConnectorHandler;
import com.simibubi.create.content.kinetics.simpleRelays.ShaftBlock;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction.Axis;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

/**
 * @author Rok, Pedro Lucas nmm. 08/10/2026
 * @project create-better-belt
 */
@Mixin(BeltConnectorHandler.class)
public class BeltConnectorHandlerMixin {

	@ModifyVariable(method = "tick", at = @At("STORE"), ordinal = 0, remap = false)
	private static Axis cbb$previewAlongShaft(Axis shaftAxis) {
		BlockPos first = cbb$firstShaft();
		BlockPos selected = cbb$selectedShaft();
		if (first == null || selected == null)
			return shaftAxis;
		if (!InlineBeltPlacement.isAlongShaft(first, selected, shaftAxis))
			return shaftAxis;
		return InlineBeltPlacement.acrossFrom(shaftAxis);
	}

	private static BlockPos cbb$firstShaft() {
		Player player = Minecraft.getInstance().player;
		if (player == null)
			return null;
		for (InteractionHand hand : InteractionHand.values()) {
			ItemStack held = player.getItemInHand(hand);
			if (!AllItems.BELT_CONNECTOR.isIn(held))
				continue;
			if (held.has(AllDataComponents.BELT_FIRST_SHAFT))
				return held.get(AllDataComponents.BELT_FIRST_SHAFT);
		}
		return null;
	}

	private static BlockPos cbb$selectedShaft() {
		Level level = Minecraft.getInstance().level;
		if (level == null || !(Minecraft.getInstance().hitResult instanceof BlockHitResult hit)
			|| hit.getType() == HitResult.Type.MISS)
			return null;

		BlockPos selected = hit.getBlockPos();
		if (level.getBlockState(selected)
			.canBeReplaced())
			return null;
		if (!ShaftBlock.isShaft(level.getBlockState(selected)))
			selected = selected.relative(hit.getDirection());
		return selected;
	}
}
