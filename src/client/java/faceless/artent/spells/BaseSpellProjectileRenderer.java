package faceless.artent.spells;

import faceless.artent.spells.entity.BaseSpellProjectile;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

public class BaseSpellProjectileRenderer extends EntityRenderer<BaseSpellProjectile> {

    public BaseSpellProjectileRenderer(EntityRendererFactory.Context context) {
        super(context);
    }

    @Override
    public void render(
      BaseSpellProjectile lightbulb,
      float f,
      float g,
      MatrixStack matrixStack,
      VertexConsumerProvider vertexConsumerProvider,
      int i
    ) {
    }

    @Override
    public Identifier getTexture(BaseSpellProjectile coinEntity) {
        return SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE;
    }
}
