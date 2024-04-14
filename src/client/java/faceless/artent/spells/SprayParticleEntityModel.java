package faceless.artent.spells;

import com.google.common.collect.ImmutableList;
import faceless.artent.spells.entity.SprayElementEntity;
import net.minecraft.client.model.*;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;

public class SprayParticleEntityModel extends EntityModel<SprayElementEntity> {
	private final ModelPart base;

	public SprayParticleEntityModel(ModelPart modelPart, SprayElementEntity.SprayElement type) {
		this.base = modelPart.getChild(type == SprayElementEntity.SprayElement.Fire ? "fire" : "water");
	}

	public static TexturedModelData getTexturedModelData() {
		ModelData modelData = new ModelData();
		ModelPartData modelPartData = modelData.getRoot();
		modelPartData.addChild("fire", ModelPartBuilder.create().uv(0, 0).cuboid(-1F, -1F, -1F, 2, 2, 2), ModelTransform.NONE);
		modelPartData.addChild("water", ModelPartBuilder.create().uv(0, 8).cuboid(-1F, -1F, -1F, 2, 2, 2), ModelTransform.NONE);
		return TexturedModelData.of(modelData, 16, 16);
	}

	@Override
	public void setAngles(SprayElementEntity entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
	}

	@Override
	public void render(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, float red, float green, float blue, float alpha) {
		ImmutableList.of(this.base).forEach((modelRenderer) -> {
			modelRenderer.render(matrices, vertices, light, overlay);
		});
	}
}
