package faceless.artent.spells;

import faceless.artent.objects.ModItems;
import faceless.artent.spells.entity.LightSwordProjectileEntity;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.random.Random;

public class LightSwordProjectileEntityRenderer extends EntityRenderer<LightSwordProjectileEntity> {
    private final ItemRenderer itemRenderer;
    private final Random random = Random.create();

    public LightSwordProjectileEntityRenderer(EntityRendererFactory.Context context) {
        super(context);
        this.itemRenderer = context.getItemRenderer();
    }

    @Override
    public void render(
      LightSwordProjectileEntity swordProjectile,
      float f,
      float g,
      MatrixStack matrixStack,
      VertexConsumerProvider vertexConsumerProvider,
      int i
    ) {
        matrixStack.push();
        var stack = new ItemStack(ModItems.LightSword_SPECIAL);
        BakedModel bakedModel = this.itemRenderer.getModel(stack,
                                                           swordProjectile.getWorld(),
                                                           null,
                                                           swordProjectile.getId());
        matrixStack.scale(1.5f, 1.5f, 1.5f);

        var velocity = swordProjectile.getVelocity();
        var pitch = (float) Math.atan2(velocity.y, Math.hypot(velocity.x, velocity.z));
        var yaw = (float) Math.atan2(velocity.z, -velocity.x);

        matrixStack.translate(0, 0.16f, 0);
        matrixStack.multiply(RotationAxis.POSITIVE_Y.rotation(yaw));
        matrixStack.multiply(RotationAxis.POSITIVE_Z.rotation(-pitch));

        matrixStack.translate(0, -0.0875f, 0);
        matrixStack.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(-45));
        this.itemRenderer.renderItem(stack,
                                     ModelTransformationMode.GROUND,
                                     false,
                                     matrixStack,
                                     vertexConsumerProvider,
                                     i,
                                     OverlayTexture.DEFAULT_UV,
                                     bakedModel);
        super.render(swordProjectile, f, g, matrixStack, vertexConsumerProvider, i);
        matrixStack.pop();
    }

    @Override
    public Identifier getTexture(LightSwordProjectileEntity coinEntity) {
        return SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE;
    }
}
