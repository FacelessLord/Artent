package faceless.artent.objects;

import faceless.artent.brewing.entity.ThrowablePotionPhialEntity;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;

public class ModEntities {
	public static EntityType<? extends ThrowablePotionPhialEntity> POTION_PHIAL = FabricEntityTypeBuilder.<ThrowablePotionPhialEntity>create(SpawnGroup.MISC, ThrowablePotionPhialEntity::new)
		.dimensions(EntityDimensions.fixed(0.25f, 0.25f))
		.trackRangeBlocks(4)
		.trackedUpdateRate(10)
		.build();
}