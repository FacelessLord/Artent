package faceless.artent.mobs;

import faceless.artent.Artent;
import faceless.artent.mobs.entity.CrowEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class CrowEntityRenderer extends GeoEntityRenderer<CrowEntity> {

    public CrowEntityRenderer(EntityRendererFactory.Context context) {
        super(context, new DefaultedEntityGeoModel<>(new Identifier(Artent.MODID, "crow")));
    }
}
