package faceless.artent.spells;

import faceless.artent.Artent;
import faceless.artent.ArtentClient;
import faceless.artent.spells.entity.SprayElementEntity;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationAxis;

public class SprayParticleEntityRenderer extends EntityRenderer<SprayElementEntity> {

	private EntityModel<SprayElementEntity> fire;
	private EntityModel<SprayElementEntity> water;

	public SprayParticleEntityRenderer(EntityRendererFactory.Context context) {
		super(context);
		fire = new SprayParticleEntityModel(context.getPart(ArtentClient.SPRAY_PARTICLE_LAYER), SprayElementEntity.SprayElement.Fire);
		water = new SprayParticleEntityModel(context.getPart(ArtentClient.SPRAY_PARTICLE_LAYER), SprayElementEntity.SprayElement.Water);
	}

	@Override
	public void render(SprayElementEntity entity,
					   float yaw,
					   float tickDelta,
					   MatrixStack matrixStack,
					   VertexConsumerProvider vertexConsumerProvider,
					   int light) {

		var buffer = vertexConsumerProvider.getBuffer(this.fire.getLayer(this.getTexture(entity)));
		if (entity.getSprayElement() == SprayElementEntity.SprayElement.Fire) {
			matrixStack.push();
			fire.render(matrixStack, buffer, light, OverlayTexture.DEFAULT_UV, 1f, 1f, 1f, 1f);
			matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(180));
			matrixStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(90));
			fire.render(matrixStack, buffer, light, OverlayTexture.DEFAULT_UV, 1f, 1f, 1f, 1f);
			matrixStack.pop();
		}
		if (entity.getSprayElement() == SprayElementEntity.SprayElement.Water) {
			matrixStack.push();
			water.render(matrixStack, buffer, light, OverlayTexture.DEFAULT_UV, 1f, 1f, 1f, 1f);
			matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(180));
			matrixStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(90));
			water.render(matrixStack, buffer, light, OverlayTexture.DEFAULT_UV, 1f, 1f, 1f, 1f);
			matrixStack.pop();
		}
	}

	@Override
	public Identifier getTexture(SprayElementEntity sprayElement) {
		return new Identifier(Artent.MODID, "textures/entity/spray_particle.png");
	}
}
