package faceless.artent.brewing;

import com.mojang.blaze3d.systems.RenderSystem;
import faceless.artent.brewing.blockEntities.BrewingCauldronBlockEntity;
import faceless.artent.objects.ModBlocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.TexturedRenderLayers;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import org.lwjgl.opengl.GL11;

public class BrewingCauldronRenderer implements BlockEntityRenderer<BrewingCauldronBlockEntity> {

	public BrewingCauldronRenderer(BlockEntityRendererFactory.Context ctx) {
	}

	@Override
	public void render(BrewingCauldronBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
		var waterLevel = entity.waterAmount;
		var waterColor = entity.color;
		var world = entity.getWorld();
		if (world == null)
			return;

		BlockRenderManager blockRenderManager = MinecraftClient.getInstance().getBlockRenderManager();
		var state = ModBlocks.CauldronFluid.getDefaultState();

		matrices.push();
		RenderSystem.enableBlend();
		RenderSystem.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		RenderSystem.clearColor(waterColor.getRedF(), waterColor.getGreenF(), waterColor.getBlueF(), 0.25f);
		matrices.translate(2 / 16d, 4 / 16d - 1 / 128f, 2 / 16d);
		matrices.scale(12f / 16f, waterLevel / 1000f * 15f / 32f, 12f / 16f);

		blockRenderManager.renderBlock(state, entity.getPos(), entity.getWorld(), matrices,
			vertexConsumers.getBuffer(TexturedRenderLayers.getEntityTranslucentCull()), false, world.random);
		RenderSystem.disableBlend();
		matrices.pop();
	}
}