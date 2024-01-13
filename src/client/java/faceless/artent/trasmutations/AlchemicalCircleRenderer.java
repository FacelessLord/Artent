package faceless.artent.trasmutations;

import faceless.artent.api.Color;
import faceless.artent.objects.ModBlocks;
import faceless.artent.transmutations.CirclePart;
import faceless.artent.transmutations.State;
import faceless.artent.transmutations.world.AlchemicalCircleBlock;
import faceless.artent.transmutations.world.AlchemicalCircleEntity;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.BlockModelRenderer;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;

import java.util.List;

import com.mojang.blaze3d.systems.RenderSystem;

public class AlchemicalCircleRenderer implements BlockEntityRenderer<AlchemicalCircleEntity> {
	public BlockEntityRendererFactory.Context renderContext;

	public AlchemicalCircleRenderer(BlockEntityRendererFactory.Context ctx) {
		renderContext = ctx;
	}

	@Override
	public void render(AlchemicalCircleEntity entity, float tickDelta, MatrixStack matrices,
			VertexConsumerProvider vertexConsumers, int light, int overlay) {
		List<CirclePart> parts = entity.parts;
		var block = entity.getWorld().getBlockState(entity.getPos()).getBlock();  
		if (block != ModBlocks.alchemicalCircle) {
			return;
		}
		
		BlockState state = entity.getWorld().getBlockState(entity.getPos()).getBlock().getDefaultState();
		BlockRenderManager renderManager = renderContext.getRenderManager();
		
		matrices.push();
		Color c = new Color();
		if (entity.transmutation != null) {
			if (entity.state == State.Preparation) {
				c = entity.transmutation.getPreparationColor();
			}

			if (entity.state == State.Action) {
				entity.transmutation.getRenderAction().accept(entity, entity.alchemist, entity.actionTime);
				c = entity.transmutation.getActionColor();
			}
		}

		RenderSystem.clearColor(c.getRedF(), c.getGreenF(), c.getBlueF(), 1f);
		
		matrices.translate(0, 0.01f, 0);
		for (CirclePart part : parts) {
			BakedModel model = renderManager
					.getModel(state.with(AlchemicalCircleBlock.circleType, part.part.id + (part.reverse && part.part.itemTexture != part.part.itemTextureRev ? 1 : 0)));

			BlockModelRenderer renderer = renderManager.getModelRenderer();
			renderer
					.render(
							matrices.peek(),
							vertexConsumers.getBuffer(RenderLayer.getCutoutMipped()),
							state,
							model,
							c.getRedF(),
							c.getGreenF(),
							c.getBlueF(),
							light,
							overlay);
		}

		if (!entity.inventory.get(0).isEmpty()) {
			matrices.translate(0.5, 0.25, 0.5);
			MinecraftClient
					.getInstance()
					.getItemRenderer()
					.renderItem(
							entity.inventory.get(0),
							ModelTransformationMode.GROUND,
							light,
							OverlayTexture.DEFAULT_UV,
							matrices,
							vertexConsumers,
							entity.getWorld(),
							(int) entity.getWorld().getTime());
		}
		matrices.pop();
	}
}
