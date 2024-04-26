package faceless.artent.api;

import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.HeldItemRenderer;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;

public class GeoHeldItemFeatureRenderer<T extends LivingEntity> {

	private final HeldItemRenderer heldItemRenderer;

	public GeoHeldItemFeatureRenderer(HeldItemRenderer heldItemRenderer) {

		this.heldItemRenderer = heldItemRenderer;
	}

	public void renderMain(MatrixStack matrixStack,
						   VertexConsumerProvider vertexConsumerProvider,
						   int light,
						   T livingEntity) {
		ItemStack itemStack = livingEntity.getMainHandStack();
		if (itemStack.isEmpty())
			return;

		matrixStack.push();
		this.heldItemRenderer.renderItem(livingEntity, itemStack, ModelTransformationMode.THIRD_PERSON_RIGHT_HAND, false, matrixStack, vertexConsumerProvider, light);
		matrixStack.pop();
	}

	public void renderOff(MatrixStack matrixStack,
						  VertexConsumerProvider vertexConsumerProvider,
						  int light,
						  T livingEntity) {
		ItemStack itemStack = livingEntity.getOffHandStack();
		if (itemStack.isEmpty())
			return;

		matrixStack.push();
		this.heldItemRenderer.renderItem(livingEntity, itemStack, ModelTransformationMode.THIRD_PERSON_RIGHT_HAND, true, matrixStack, vertexConsumerProvider, light);
		matrixStack.pop();
	}
}
