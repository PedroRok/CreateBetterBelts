package com.pedrorok.createBetterBelt.client;

import com.pedrorok.createBetterBelt.Constants;
import com.pedrorok.createBetterBelt.content.inlinebelt.InlineBeltBlock;

import dev.engine_room.flywheel.lib.model.baked.PartialModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;

/**
 * @author Rok, Pedro Lucas nmm. 08/10/2026
 * @project create-better-belt
 */
public class CBBPartialModels {

	public static final PartialModel INLINE_BELT_START = block("inline_belt_start");
	public static final PartialModel INLINE_BELT_END = block("inline_belt_end");

	private static PartialModel block(String path) {
		return PartialModel.of(ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "block/" + path));
	}

	public static void init() {}

	public static PartialModel beltPartial(BlockState state, boolean diagonal, boolean start, boolean end,
		boolean bottom, PartialModel fallback) {
		if (diagonal || bottom || !(start || end))
			return fallback;
		if (!InlineBeltBlock.isInlineBelt(state))
			return fallback;
		return start ? INLINE_BELT_START : INLINE_BELT_END;
	}
}
