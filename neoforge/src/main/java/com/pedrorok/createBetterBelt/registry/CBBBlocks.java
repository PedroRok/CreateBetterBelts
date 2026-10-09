package com.pedrorok.createBetterBelt.registry;

import static com.simibubi.create.foundation.data.TagGen.axeOrPickaxe;

import com.pedrorok.createBetterBelt.content.inlinebelt.InlineBeltBlock;
import com.pedrorok.createBetterBelt.content.inlinebelt.InlineBeltGenerator;
import com.simibubi.create.content.kinetics.belt.BeltBlock;
import com.simibubi.create.content.kinetics.belt.BeltModel;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.tterrag.registrate.util.entry.BlockEntry;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.MapColor;

/**
 * @author Rok, Pedro Lucas nmm. 08/10/2026
 * @project create-better-belt
 */
public class CBBBlocks {

	private static final CreateRegistrate REGISTRATE = CBBRegistrate.get();

	public static final BlockEntry<InlineBeltBlock> INLINE_BELT =
		REGISTRATE.block("inline_belt", InlineBeltBlock::new)
			.properties(p -> p.sound(SoundType.WOOL)
				.strength(0.8f)
				.mapColor(MapColor.COLOR_GRAY))
			.addLayer(() -> RenderType::cutoutMipped)
			.transform(axeOrPickaxe())
			.blockstate(new InlineBeltGenerator()::generate)
			.onRegister(CreateRegistrate.blockModel(() -> BeltModel::new))
			.clientExtension(() -> () -> new BeltBlock.RenderProperties())
			.register();

	public static void register() {}
}
