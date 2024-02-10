package faceless.artent.mobs;

import faceless.artent.Artent;
import faceless.artent.mobs.entity.WindmillEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class WindmillEntityRenderer extends GeoEntityRenderer<WindmillEntity> {

	public WindmillEntityRenderer(EntityRendererFactory.Context context) {
		super(context, new DefaultedEntityGeoModel<>(new Identifier(Artent.MODID, "windmill")));
	}
}
