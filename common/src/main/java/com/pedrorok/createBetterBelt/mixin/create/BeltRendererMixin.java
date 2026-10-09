package com.pedrorok.createBetterBelt.mixin.create;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import com.mojang.blaze3d.vertex.PoseStack;
import com.pedrorok.createBetterBelt.client.CBBPartialModels;
import com.pedrorok.createBetterBelt.compat.BeltIdentity;
import com.pedrorok.createBetterBelt.content.inlinebelt.InlineBeltBlock;
import com.pedrorok.createBetterBelt.content.belt.BeltReversal;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityRenderer;
import com.simibubi.create.content.kinetics.belt.BeltBlockEntity;
import com.simibubi.create.content.kinetics.belt.BeltRenderer;
import com.tterrag.registrate.util.entry.BlockEntry;

import dev.engine_room.flywheel.lib.model.baked.PartialModel;
import net.createmod.catnip.animation.AnimationTickHolder;
import net.createmod.catnip.render.SuperByteBuffer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.state.BlockState;

/**
 * @author Rok, Pedro Lucas nmm. 08/10/2026
 * @project create-better-belt
 */
@Mixin(BeltRenderer.class)
public class BeltRendererMixin {

	@Redirect(method = "renderSafe", at = @At(value = "INVOKE",
		target = "Lcom/tterrag/registrate/util/entry/BlockEntry;has(Lnet/minecraft/world/level/block/state/BlockState;)Z"))
	private boolean cbb$anyBelt(BlockEntry<?> entry, BlockState state) {
		return BeltIdentity.isBelt(entry, state);
	}

	@Redirect(
		method = "renderSafe",
		at = @At(value = "INVOKE", target = "Lnet/minecraft/core/Direction;getClockWise()Lnet/minecraft/core/Direction;"))
	private Direction cbb$pulleyAlongBelt(Direction facing, BeltBlockEntity be, float partialTicks, PoseStack ms,
		MultiBufferSource buffer, int light, int overlay) {
		return InlineBeltBlock.pulleyDirection(be.getBlockState(), facing.getClockWise());
	}

	@Redirect(
		method = "renderSafe",
		at = @At(value = "INVOKE",
			target = "Lcom/simibubi/create/content/kinetics/belt/BeltRenderer;getBeltPartial(ZZZZ)Ldev/engine_room/flywheel/lib/model/baked/PartialModel;"))
	private PartialModel cbb$inlineBeltEnds(boolean diagonal, boolean start, boolean end, boolean bottom,
		BeltBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {
		return CBBPartialModels.beltPartial(be.getBlockState(), diagonal, start, end, bottom,
			BeltRenderer.getBeltPartial(diagonal, start, end, bottom));
	}

	@Redirect(
		method = "renderSafe",
		at = @At(value = "INVOKE",
			target = "Lcom/simibubi/create/content/kinetics/base/KineticBlockEntityRenderer;standardKineticRotationTransform(Lnet/createmod/catnip/render/SuperByteBuffer;Lcom/simibubi/create/content/kinetics/base/KineticBlockEntity;I)Lnet/createmod/catnip/render/SuperByteBuffer;"))
	private SuperByteBuffer cbb$pulleyFollowsShaft(SuperByteBuffer buffer, KineticBlockEntity be, int light) {
		if (!BeltReversal.isReversed(be))
			return KineticBlockEntityRenderer.standardKineticRotationTransform(buffer, be, light);

		Axis axis = KineticBlockEntityRenderer.getRotationAxisOf(be);
		float time = AnimationTickHolder.getRenderTime(be.getLevel());
		float offset = KineticBlockEntityRenderer.getRotationOffsetForPosition(be, be.getBlockPos(), axis);
		float angle = ((time * BeltReversal.shaftSpeedOf(be) * 3f / 10 + offset) % 360) / 180 * Mth.PI;
		return KineticBlockEntityRenderer.kineticRotationTransform(buffer, be, axis, angle, light);
	}
}
