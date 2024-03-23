package faceless.artent.registries;

import faceless.artent.Artent;
import faceless.artent.mobs.entity.CrowEntity;
import faceless.artent.objects.ModEntities;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class EntityRegistry implements IRegistry {
	@Override
	public void register() {
		register("potion_phial_entity", ModEntities.POTION_PHIAL);
		register("lightbulb", ModEntities.LIGHTBULB);
		register("coin", ModEntities.COIN_ENTITY);
		register("crow", ModEntities.CROW_ENTITY);
		FabricDefaultAttributeRegistry.register(ModEntities.CROW_ENTITY, CrowEntity.createLivingAttributes());
	}

	private static <T extends Entity> void register(String id, EntityType<T> type) {
		Registry.register(Registries.ENTITY_TYPE, new Identifier(Artent.MODID, id), type);
	}
}
