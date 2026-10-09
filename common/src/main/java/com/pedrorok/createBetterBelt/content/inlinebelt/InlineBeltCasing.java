package com.pedrorok.createBetterBelt.content.inlinebelt;

import org.jetbrains.annotations.Nullable;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.content.kinetics.belt.BeltBlockEntity.CasingType;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

/**
 * @author Rok, Pedro Lucas nmm. 09/10/2026
 * @project create-better-belt
 */
public class InlineBeltCasing {

	public record Source(int slot, CasingType type) {}

	public static CasingType typeOf(ItemStack stack) {
		if (AllBlocks.ANDESITE_CASING.isIn(stack))
			return CasingType.ANDESITE;
		if (AllBlocks.BRASS_CASING.isIn(stack))
			return CasingType.BRASS;
		return CasingType.NONE;
	}

	@Nullable
	public static Source find(Player player, InteractionHand hand) {
		Inventory inventory = player.getInventory();
		int anchor = inventory.selected;

		for (int distance = 0; distance < Inventory.getSelectionSize(); distance++) {
			Source right = at(inventory, anchor + distance);
			if (right != null)
				return right;
			if (distance == 0)
				continue;
			Source left = at(inventory, anchor - distance);
			if (left != null)
				return left;
		}

		return at(inventory, Inventory.SLOT_OFFHAND);
	}

	@Nullable
	private static Source at(Inventory inventory, int slot) {
		if (slot != Inventory.SLOT_OFFHAND && !Inventory.isHotbarSlot(slot))
			return null;
		CasingType type = typeOf(inventory.getItem(slot));
		return type == CasingType.NONE ? null : new Source(slot, type);
	}

	public static boolean place(Level level, BlockPos start, BlockPos end, @Nullable Player player,
		InteractionHand hand) {
		boolean inline = InlineBeltPlacement.isInlineConnection(level, start, end);
		boolean cross = !inline && CrossAxisBeltPlacement.isCrossAxisConnection(level, start, end);
		if (!inline && !cross)
			return false;

		Source source = player == null ? null : find(player, hand);
		CasingType type = source == null ? CasingType.ANDESITE : source.type();

		if (inline)
			InlineBeltPlacement.createInlineBelts(level, start, end, type);
		else
			CrossAxisBeltPlacement.createCrossAxisBelts(level, start, end, type);
		return true;
	}
}
