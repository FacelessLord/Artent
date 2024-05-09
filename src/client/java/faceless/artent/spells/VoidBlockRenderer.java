package faceless.artent.spells;

import faceless.artent.spells.blockEntity.VoidBlockEntity;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Direction;
import org.joml.Matrix4f;

public class VoidBlockRenderer implements BlockEntityRenderer<VoidBlockEntity> {
    public BlockEntityRendererFactory.Context renderContext;

    public VoidBlockRenderer(BlockEntityRendererFactory.Context ctx) {
        renderContext = ctx;
    }

    @Override
    public void render(
      VoidBlockEntity entity,
      float tickDelta,
      MatrixStack matrixStack,
      VertexConsumerProvider vertexConsumerProvider,
      int light,
      int overlay
    ) {
        Matrix4f matrix4f = matrixStack.peek().getPositionMatrix();
        this.renderSides(entity, matrix4f, vertexConsumerProvider.getBuffer(this.getLayer()));
    }

    private void renderSides(VoidBlockEntity entity, Matrix4f matrix, VertexConsumer vertexConsumer) {
        var d = 0.01f;
        var b = 1 - d;
        var c = 1 + d;
        this.renderSide(entity, matrix, vertexConsumer, -d, c, -d, c, d, d, d, d, Direction.SOUTH);
        this.renderSide(entity, matrix, vertexConsumer, -d, c, c, -d, b, b, b, b, Direction.NORTH);
        this.renderSide(entity, matrix, vertexConsumer, d, d, c, -d, -d, c, c, -d, Direction.EAST);
        this.renderSide(entity, matrix, vertexConsumer, b, b, -d, c, -d, c, c, -d, Direction.WEST);
        this.renderSide(entity, matrix, vertexConsumer, -d, c, b, b, -d, -d, c, c, Direction.DOWN);
        this.renderSide(entity, matrix, vertexConsumer, -d, c, d, d, c, c, -d, -d, Direction.UP);
    }

    private void renderSide(
      VoidBlockEntity entity,
      Matrix4f model,
      VertexConsumer vertices,
      float x1,
      float x2,
      float y1,
      float y2,
      float z1,
      float z2,
      float z3,
      float z4,
      Direction side
    ) {
        if (entity.shouldDrawSide(side)) {
            vertices.vertex(model, x1, y1, z1).next();
            vertices.vertex(model, x2, y1, z2).next();
            vertices.vertex(model, x2, y2, z3).next();
            vertices.vertex(model, x1, y2, z4).next();
        }
    }

    protected RenderLayer getLayer() {
        return RenderLayer.getEndPortal();
    }
}
