package faceless.artent.registries;

import faceless.artent.objects.ModEntities;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class EntityRegistry implements IRegistry {
	@Override
	public void register() {
		register("potion_phial_entity", ModEntities.POTION_PHIAL);
	}

	private static <T extends Entity> EntityType<T> register(String id, EntityType<T> type) {
		return Registry.register(Registries.ENTITY_TYPE, id, type);
	}
}
