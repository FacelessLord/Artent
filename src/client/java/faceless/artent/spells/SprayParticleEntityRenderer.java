package faceless.artent.spells;

import faceless.artent.Artent;
import faceless.artent.spells.entity.SprayElementEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

public class SprayParticleEntityRenderer extends EntityRenderer<SprayElementEntity> {


    public SprayParticleEntityRenderer(EntityRendererFactory.Context context) {
        super(context);
    }

    @Override
    public void render(
      SprayElementEntity entity,
      float yaw,
      float tickDelta,
      MatrixStack matrixStack,
      VertexConsumerProvider vertexConsumerProvider,
      int light
    ) {
    }

    @Override
    public Identifier getTexture(SprayElementEntity sprayElement) {
        return new Identifier(Artent.MODID, "textures/entity/spray_particle.png");
    }
}
