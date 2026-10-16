package com.pedrorok.createBetterBelt.mixin.create;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.pedrorok.createBetterBelt.client.CBBPartialModels;
import com.pedrorok.createBetterBelt.content.belt.BeltReversal;
import com.pedrorok.createBetterBelt.content.inlinebelt.InlineBeltBlock;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.content.kinetics.base.RotatingInstance;
import com.simibubi.create.content.kinetics.belt.BeltBlockEntity;
import com.simibubi.create.content.kinetics.belt.BeltRenderer;
import com.simibubi.create.content.kinetics.belt.BeltVisual;

import dev.engine_room.flywheel.api.visualization.VisualizationContext;
import dev.engine_room.flywheel.lib.model.baked.PartialModel;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;

/**
 * @author Rok, Pedro Lucas nmm. 08/10/2026
 * @project create-better-belt
 */
@Mixin(BeltVisual.class)
public class BeltVisualMixin {

	@Unique
	private BlockState cbb$orientationState;

	@SuppressWarnings("rawtypes")
	@Redirect(method = "getOrientation", remap = false, at = @At(value = "INVOKE", remap = true,
		target = "Lnet/minecraft/world/level/block/state/BlockState;getValue(Lnet/minecraft/world/level/block/state/properties/Property;)Ljava/lang/Comparable;"))
	private Comparable cbb$rememberState(BlockState state, Property property) {
		cbb$orientationState = state;
		return state.getValue(property);
	}

	@Inject(method = "getOrientation", at = @At("RETURN"), cancellable = true, remap = false)
	private void cbb$pulleyAlongBelt(CallbackInfoReturnable<Direction> cir) {
		cir.setReturnValue(InlineBeltBlock.pulleyDirection(cbb$orientationState, cir.getReturnValue()));
	}

	@Redirect(method = "<init>", at = @At(value = "INVOKE",
		target = "Lcom/simibubi/create/content/kinetics/belt/BeltRenderer;getBeltPartial(ZZZZ)Ldev/engine_room/flywheel/lib/model/baked/PartialModel;"))
	private PartialModel cbb$inlineBeltEnds(boolean diagonal, boolean start, boolean end, boolean bottom,
		VisualizationContext context, BeltBlockEntity blockEntity, float partialTick) {
		return CBBPartialModels.beltPartial(blockEntity.getBlockState(), diagonal, start, end, bottom,
			BeltRenderer.getBeltPartial(diagonal, start, end, bottom));
	}

	@Redirect(method = { "<init>", "update" }, remap = false, at = @At(value = "INVOKE",
		target = "Lcom/simibubi/create/content/kinetics/base/RotatingInstance;setup(Lcom/simibubi/create/content/kinetics/base/KineticBlockEntity;)Lcom/simibubi/create/content/kinetics/base/RotatingInstance;"))
	private RotatingInstance cbb$pulleyFollowsShaft(RotatingInstance instance, KineticBlockEntity be) {
		return instance.setup(be, BeltReversal.shaftSpeedOf(be));
	}
}
