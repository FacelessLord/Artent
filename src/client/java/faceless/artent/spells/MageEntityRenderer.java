package faceless.artent.spells;

import faceless.artent.Artent;
import faceless.artent.api.GeoHeldItemFeatureRenderer;
import faceless.artent.spells.entity.MageEntity;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.joml.AxisAngle4f;
import org.joml.Quaternionf;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;
import software.bernie.geckolib.model.data.EntityModelData;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import software.bernie.geckolib.util.RenderUtils;

public class MageEntityRenderer extends GeoEntityRenderer<MageEntity> {

	private GeoHeldItemFeatureRenderer<MageEntity> heldItemRenderer;

	public MageEntityRenderer(EntityRendererFactory.Context renderManager) {
		super(renderManager, new DefaultedEntityGeoModel<>(new Identifier(Artent.MODID, "mage")));
		heldItemRenderer = new GeoHeldItemFeatureRenderer<>(renderManager.getHeldItemRenderer());
	}

	@Override
	public void render(MageEntity entity, float entityYaw, float partialTick, MatrixStack poseStack, VertexConsumerProvider bufferSource, int packedLight) {
		super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
	}

//	public void renderRecursively(MatrixStack poseStack, MageEntity animatable, GeoBone bone, RenderLayer renderType, VertexConsumerProvider bufferSource,
//								  VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight,
//								  int packedOverlay, float red, float green, float blue, float alpha) {
//		poseStack.push();
//		RenderUtils.prepMatrixForBone(poseStack, bone);
//		if (Objects.equals(bone.getName(), "rightHand"))
//			heldItemRenderer.renderMain(poseStack, bufferSource, packedLight, animatable);
//		if (Objects.equals(bone.getName(), "leftHand"))
//			heldItemRenderer.renderOff(poseStack, bufferSource, packedLight, animatable);
//		poseStack.pop();
//
//		super.renderRecursively(poseStack, animatable, bone, renderType, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
//	}

	public void actuallyRender(MatrixStack poseStack, MageEntity animatable, BakedGeoModel model, RenderLayer renderType,
							   VertexConsumerProvider bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick,
							   int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		super.actuallyRender(poseStack, animatable, model, renderType,
		  bufferSource, buffer, false, partialTick, packedLight, packedOverlay, red, green, blue, alpha);


		poseStack.push();

		boolean shouldSit = animatable.hasVehicle() && (animatable.getVehicle() != null);
		float lerpBodyRot = MathHelper.lerpAngleDegrees(partialTick, animatable.prevBodyYaw, animatable.bodyYaw);
		float lerpHeadRot = MathHelper.lerpAngleDegrees(partialTick, animatable.prevHeadYaw, animatable.headYaw);
		float netHeadYaw = lerpHeadRot - lerpBodyRot;

		if (shouldSit && animatable.getVehicle() instanceof LivingEntity livingentity) {
			lerpBodyRot = MathHelper.lerpAngleDegrees(partialTick, livingentity.prevBodyYaw, livingentity.bodyYaw);
			netHeadYaw = lerpHeadRot - lerpBodyRot;
			float clampedHeadYaw = MathHelper.clamp(MathHelper.wrapDegrees(netHeadYaw), -85, 85);
			lerpBodyRot = lerpHeadRot - clampedHeadYaw;

			if (clampedHeadYaw * clampedHeadYaw > 2500f)
				lerpBodyRot += clampedHeadYaw * 0.2f;

			netHeadYaw = lerpHeadRot - lerpBodyRot;
		}

		if (animatable.getPose() == EntityPose.SLEEPING) {
			Direction bedDirection = animatable.getSleepingDirection();

			if (bedDirection != null) {
				float eyePosOffset = animatable.getEyeHeight(EntityPose.STANDING) - 0.1F;

				poseStack.translate(-bedDirection.getOffsetX() * eyePosOffset, 0, -bedDirection.getOffsetZ() * eyePosOffset);
			}
		}

		float ageInTicks = animatable.age + partialTick;
		float limbSwingAmount = 0;
		float limbSwing = 0;

		applyRotations(animatable, poseStack, ageInTicks, lerpBodyRot, partialTick);

		if (!shouldSit && animatable.isAlive()) {
			limbSwingAmount = animatable.limbAnimator.getSpeed(partialTick);
			limbSwing = animatable.limbAnimator.getPos(partialTick);

			if (animatable.isBaby())
				limbSwing *= 3f;

			if (limbSwingAmount > 1f)
				limbSwingAmount = 1f;
		}

		if (!isReRender) {
			float headPitch = MathHelper.lerp(partialTick, animatable.prevPitch, animatable.getPitch());
			float motionThreshold = getMotionAnimThreshold(animatable);
			Vec3d velocity = animatable.getVelocity();
			float avgVelocity = (float) (Math.abs(velocity.x) + Math.abs(velocity.z) / 2f);
			AnimationState<MageEntity> animationState = new AnimationState<>(animatable, limbSwing, limbSwingAmount, partialTick, avgVelocity >= motionThreshold && limbSwingAmount != 0);
			long instanceId = getInstanceId(animatable);

			animationState.setData(DataTickets.TICK, animatable.getTick(animatable));
			animationState.setData(DataTickets.ENTITY, animatable);
			animationState.setData(DataTickets.ENTITY_MODEL_DATA, new EntityModelData(shouldSit, animatable.isBaby(), -netHeadYaw, -headPitch));
			this.model.addAdditionalStateData(animatable, instanceId, animationState::setData);
			this.model.handleAnimations(animatable, instanceId, animationState);
		}

		poseStack.translate(0, 0.01f, 0);
		var topBone = model.topLevelBones().stream().findFirst();
		if (topBone.isEmpty())
			return;

		poseStack.push();
		var rightHand = model.searchForChildBone(topBone.get(), "leftHand");
		RenderUtils.prepMatrixForBone(poseStack, rightHand);
		RenderUtils.translateToPivotPoint(poseStack, rightHand);
		poseStack.multiply(new Quaternionf(new AxisAngle4f((float) Math.PI / 2, -1, 0, 0)));
		poseStack.translate(2 / 16f, 2 / 16f, -10 / 16f);
		heldItemRenderer.renderMain(poseStack, bufferSource, packedLight, animatable);
		poseStack.pop();

		poseStack.push();
		var leftHand = model.searchForChildBone(topBone.get(), "rightHand");
		RenderUtils.prepMatrixForBone(poseStack, leftHand);
		RenderUtils.translateToPivotPoint(poseStack, leftHand);
		poseStack.multiply(new Quaternionf(new AxisAngle4f((float) Math.PI / 2, -1, 0, 0)));
		poseStack.translate(-2 / 16f, 2 / 16f, -10 / 16f);
		heldItemRenderer.renderOff(poseStack, bufferSource, packedLight, animatable);
		poseStack.pop();

		poseStack.pop();
	}
}
