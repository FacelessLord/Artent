package faceless.artent.spells;

import faceless.artent.Artent;
import faceless.artent.spells.entity.MageEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class MageEntityRenderer extends GeoEntityRenderer<MageEntity> {

	public MageEntityRenderer(EntityRendererFactory.Context renderManager) {
		super(renderManager, new DefaultedEntityGeoModel<>(new Identifier(Artent.MODID, "mage")));
	}
}
