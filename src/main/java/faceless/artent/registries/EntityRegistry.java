package faceless.artent.registries;

import faceless.artent.Artent;
import faceless.artent.mobs.entity.CrowEntity;
import faceless.artent.objects.ModEntities;
import faceless.artent.spells.entity.MageEntity;
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
		register("spell_particle", ModEntities.SPELL_PARTICLE);
		register("coin", ModEntities.COIN_ENTITY);
		register("crow", ModEntities.CROW_ENTITY);
		register("light_sword", ModEntities.LIGHT_SWORD);
		register("spray_element", ModEntities.SPRAY_ELEMENT_ENTITY);
		register("mage", ModEntities.MAGE_ENTITY);
		FabricDefaultAttributeRegistry.register(ModEntities.CROW_ENTITY, CrowEntity.createLivingAttributes());
		FabricDefaultAttributeRegistry.register(ModEntities.MAGE_ENTITY, MageEntity.createLivingAttributes());
	}

	private static <T extends Entity> void register(String id, EntityType<T> type) {
		Registry.register(Registries.ENTITY_TYPE, new Identifier(Artent.MODID, id), type);
	}
}
