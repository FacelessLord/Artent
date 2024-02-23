package faceless.artent.trading;

import faceless.artent.api.MiscUtils;
import faceless.artent.objects.ModItems;
import faceless.artent.trading.entity.CoinEntity;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.random.Random;

public class CoinEntityRenderer extends EntityRenderer<CoinEntity> {
	private final ItemRenderer itemRenderer;
	private final Random random = Random.create();

	public CoinEntityRenderer(EntityRendererFactory.Context context) {
		super(context);
		this.itemRenderer = context.getItemRenderer();
	}

	@Override
	public void render(CoinEntity coinEntity,
		float f,
		float g,
		MatrixStack matrixStack,
		VertexConsumerProvider vertexConsumerProvider,
		int i) {
		if (coinEntity.getCoinType() > 2)
			return;
		var coinType = MiscUtils.limit(coinEntity.getCoinType(), 0, 2);
		matrixStack.push();
		ItemStack itemStack = new ItemStack(ModItems.Coins[coinType]);
		int j = itemStack.isEmpty() ? 187 : Item.getRawId(itemStack.getItem()) + coinEntity.getId();
		this.random.setSeed(j);

		BakedModel bakedModel = this.itemRenderer.getModel(itemStack, coinEntity.getWorld(), null, coinEntity.getId());
		boolean hasDepth = bakedModel.hasDepth();
		float n = coinEntity.getRotation(g);
		matrixStack.multiply(RotationAxis.POSITIVE_Y.rotation(n));
		float xModelTranslation = bakedModel.getTransformation().ground.scale.x();
		float yModelTranslation = bakedModel.getTransformation().ground.scale.y();
		float zModelTranslation = bakedModel.getTransformation().ground.scale.z();

		float bobbingY = MathHelper.sin(((coinEntity.age + g) / 10.0f + coinEntity.uniqueOffset)) * 0.1f + 0.1f;
		float m = bakedModel.getTransformation().getTransformation(ModelTransformationMode.GROUND).scale.y();
		matrixStack.translate(0.0f, bobbingY + 0.25f * m, 0.0f);
		var count = coinEntity.getRenderedAmount();
		if (!hasDepth) {
			var r = -0.0f * (float) (count - 1) * 0.5f * xModelTranslation;
			var s = -0.0f * (float) (count - 1) * 0.5f * yModelTranslation;
			var t = -0.09375f * (float) (count - 1) * 0.5f * zModelTranslation;
			matrixStack.translate(r, s, t);
		}

		for (int u = 0; u < count; ++u) {
			matrixStack.push();
			if (u > 0) {
				if (hasDepth) {
					var r = (this.random.nextFloat() * 2.0f - 1.0f) * 0.15f;
					var s = (this.random.nextFloat() * 2.0f - 1.0f) * 0.15f;
					var t = (this.random.nextFloat() * 2.0f - 1.0f) * 0.15f;
					matrixStack.translate(r, s, t);
				} else {
					var r = (this.random.nextFloat() * 2.0f - 1.0f) * 0.15f * 0.5f;
					var s = (this.random.nextFloat() * 2.0f - 1.0f) * 0.15f * 0.5f;
					matrixStack.translate(r, s, 0.0f);
				}
			}
			matrixStack.scale(0.75f, 0.75f, 0.75f);
			this.itemRenderer.renderItem(itemStack,
				ModelTransformationMode.GROUND,
				false,
				matrixStack,
				vertexConsumerProvider,
				i,
				OverlayTexture.DEFAULT_UV,
				bakedModel);
			matrixStack.pop();
			if (hasDepth) continue;
			matrixStack.translate(0.0f * xModelTranslation, 0.0f * yModelTranslation, 0.09375f * zModelTranslation);
		}
		matrixStack.pop();
		super.render(coinEntity, f, g, matrixStack, vertexConsumerProvider, i);
	}

	@Override
	public Identifier getTexture(CoinEntity coinEntity) {
		return SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE;
	}
}
