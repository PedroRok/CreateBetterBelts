package com.pedrorok.createBetterBelt.content.inlinebelt;

import com.pedrorok.createBetterBelt.Constants;
import com.simibubi.create.content.kinetics.belt.BeltBlock;
import com.simibubi.create.content.kinetics.belt.BeltGenerator;
import com.simibubi.create.content.kinetics.belt.BeltPart;
import com.simibubi.create.content.kinetics.belt.BeltSlope;
import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;

import net.minecraft.core.Direction;
import net.minecraft.core.Direction.AxisDirection;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import io.github.fabricators_of_create.porting_lib.models.generators.ModelFile;

/**
 * @author Rok, Pedro Lucas nmm. 08/10/2026
 * @project create-better-belt
 */
public class InlineBeltGenerator extends BeltGenerator {

	private static final String CREATE = "create";

	@Override
	public <T extends Block> ModelFile getModel(DataGenContext<Block, T> ctx, RegistrateBlockstateProvider prov,
		BlockState state) {
		boolean casing = state.getValue(BeltBlock.CASING);

		if (!casing)
			return create("block/belt/particle");

		BeltPart part = state.getValue(BeltBlock.PART);
		Direction direction = state.getValue(BeltBlock.HORIZONTAL_FACING);
		BeltSlope slope = state.getValue(BeltBlock.SLOPE);
		boolean downward = slope == BeltSlope.DOWNWARD;
		boolean diagonal = slope == BeltSlope.UPWARD || downward;
		boolean vertical = slope == BeltSlope.VERTICAL;
		boolean pulley = part == BeltPart.PULLEY;
		boolean sideways = slope == BeltSlope.SIDEWAYS;
		boolean negative = direction.getAxisDirection() == AxisDirection.NEGATIVE;

		if ((vertical && negative || downward || sideways && negative) && part != BeltPart.MIDDLE && !pulley)
			part = part == BeltPart.END ? BeltPart.START : BeltPart.END;

		if (vertical)
			slope = BeltSlope.SIDEWAYS;

		String slopeName = diagonal ? "diagonal" : slope.getSerializedName();

		if (slopeName.equals("horizontal") && part != BeltPart.MIDDLE)
			return own("block/inline_belt_" + (part == BeltPart.END ? "end" : "start") + "_casing");

		return create("block/belt_casing/" + slopeName + "_" + part.getSerializedName());
	}

	private static ModelFile create(String path) {
		return new ModelFile.UncheckedModelFile(ResourceLocation.fromNamespaceAndPath(CREATE, path));
	}

	private static ModelFile own(String path) {
		return new ModelFile.UncheckedModelFile(ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, path));
	}
}
