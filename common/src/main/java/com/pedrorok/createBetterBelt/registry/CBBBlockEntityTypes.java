package com.pedrorok.createBetterBelt.registry;

import com.pedrorok.createBetterBelt.content.inlinebelt.InlineBeltBlockEntity;
import com.simibubi.create.content.kinetics.belt.BeltBlockEntity;
import com.simibubi.create.content.kinetics.belt.BeltRenderer;
import com.simibubi.create.content.kinetics.belt.BeltVisual;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.tterrag.registrate.util.entry.BlockEntityEntry;

/**
 * @author Rok, Pedro Lucas nmm. 08/10/2026
 * @project create-better-belt
 */
public class CBBBlockEntityTypes {

	private static final CreateRegistrate REGISTRATE = CBBRegistrate.get();

	public static final BlockEntityEntry<InlineBeltBlockEntity> INLINE_BELT = REGISTRATE
		.blockEntity("inline_belt", InlineBeltBlockEntity::new)
		.visual(() -> BeltVisual::new, BeltBlockEntity::shouldRenderNormally)
		.validBlocks(CBBBlocks.INLINE_BELT)
		.renderer(() -> BeltRenderer::new)
		.register();

	public static void register() {}
}
