package faceless.artent.sharpening;

import faceless.artent.objects.ModBlocks;
import faceless.artent.sharpening.block.SharpeningAnvil;
import faceless.artent.sharpening.blockEntities.SharpeningAnvilBlockEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Direction;
import org.joml.AxisAngle4f;
import org.joml.Quaternionf;

@Environment(EnvType.CLIENT)
public class SharpeningAnvilRenderer implements BlockEntityRenderer<SharpeningAnvilBlockEntity> {
	public BlockEntityRendererFactory.Context renderContext;

	public SharpeningAnvilRenderer(BlockEntityRendererFactory.Context ctx) {
		renderContext = ctx;
	}

	@Override
	public void render(SharpeningAnvilBlockEntity entity, float tickDelta, MatrixStack matrices,
					   VertexConsumerProvider vertexConsumers, int light, int overlay) {
		var world = entity.getWorld();
		if (world == null)
			return;

		var blockState = world.getBlockState(entity.getPos());
		var block = blockState.getBlock();
		if (block != ModBlocks.SharpeningAnvil) {
			return;
		}

		matrices.push();

		var inventory = entity.inventory;
		// Drawing inventory in order [tool, hammer, catalyst]
		var facing = blockState.get(SharpeningAnvil.FACING);
		applyFacingRotation(matrices, facing);
		matrices.scale(0.5f, 0.5f, 0.5f);
		// Tool
		if (!inventory.getStack(0).isEmpty()) {
			matrices.push();
			matrices.multiply(new Quaternionf(new AxisAngle4f((float) Math.PI / 2, 0, 1, 0)));
			matrices.multiply(new Quaternionf(new AxisAngle4f((float) Math.PI / 2, -1, 0, 0)));
			matrices.translate(-1.75f, -1.125f, 1.375f - 1f / 16f);
			MinecraftClient
				.getInstance()
				.getItemRenderer()
				.renderItem(
					inventory.getStack(0),
					ModelTransformationMode.GROUND,
					light,
					OverlayTexture.DEFAULT_UV,
					matrices,
					vertexConsumers,
					entity.getWorld(),
					(int) entity.getWorld().getTime());
			matrices.pop();
		}
		// Catalyst
		if (!inventory.getStack(1).isEmpty()) {
			matrices.push();
			matrices.multiply(new Quaternionf(new AxisAngle4f((float) Math.PI / 2, 0, 1, 0)));
			matrices.multiply(new Quaternionf(new AxisAngle4f((float) Math.PI / 2, -1, 0, 0)));
			matrices.translate(-0.75f, -1.125f, 1.375f - 1f / 16f);
			MinecraftClient
				.getInstance()
				.getItemRenderer()
				.renderItem(
					inventory.getStack(1),
					ModelTransformationMode.GROUND,
					light,
					OverlayTexture.DEFAULT_UV,
					matrices,
					vertexConsumers,
					entity.getWorld(),
					(int) entity.getWorld().getTime());
			matrices.pop();
		}
		// Hammer
		if (!inventory.getStack(2).isEmpty()) {
			matrices.push();
			matrices.multiply(new Quaternionf(new AxisAngle4f((float) Math.PI / 2, 0, 0, 1)));
			matrices.translate(1.375f - 1f / 16f, -1.5, 1.25);
			MinecraftClient
				.getInstance()
				.getItemRenderer()
				.renderItem(
					inventory.getStack(2),
					ModelTransformationMode.GROUND,
					light,
					OverlayTexture.DEFAULT_UV,
					matrices,
					vertexConsumers,
					entity.getWorld(),
					(int) entity.getWorld().getTime());
			matrices.pop();
		}
		matrices.pop();
	}

	private static void applyFacingRotation(MatrixStack matrices, Direction facing) {
		matrices.translate(0.5f, 0.5f, 0.5f);
		switch (facing) {
//			case NORTH -> ;matrices.multiply(new Quaternionf(new AxisAngle4f((float) Math.PI / 2, -1, 0, 0)));
			case SOUTH -> matrices.multiply(new Quaternionf(new AxisAngle4f((float) Math.PI, 0, 1, 0)));
			case EAST -> matrices.multiply(new Quaternionf(new AxisAngle4f((float) Math.PI * 3 / 2, 0, 1, 0)));
			case WEST -> matrices.multiply(new Quaternionf(new AxisAngle4f((float) Math.PI / 2, 0, 1, 0)));
		}
		matrices.translate(-0.5f, -0.5f, -0.5f);
	}
}
