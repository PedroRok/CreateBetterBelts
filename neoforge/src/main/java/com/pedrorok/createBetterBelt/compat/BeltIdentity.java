package com.pedrorok.createBetterBelt.compat;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.content.kinetics.belt.BeltBlock;
import com.tterrag.registrate.util.entry.BlockEntry;

import net.minecraft.world.level.block.state.BlockState;

/**
 * @author Rok, Pedro Lucas nmm. 08/10/2026
 * @project create-better-belt
 */
public class BeltIdentity {

	public static boolean isBelt(BlockEntry<?> entry, BlockState state) {
		if (entry == AllBlocks.BELT)
			return state.getBlock() instanceof BeltBlock;
		return entry.has(state);
	}
}
