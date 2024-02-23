package faceless.artent.objects;

import faceless.artent.brewing.entity.ThrowablePotionPhialEntity;
import faceless.artent.mobs.entity.CrowEntity;
import faceless.artent.trading.entity.CoinEntity;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;

public class ModEntities {
	public static EntityType<? extends ThrowablePotionPhialEntity> POTION_PHIAL =
		FabricEntityTypeBuilder
			.<ThrowablePotionPhialEntity>create(
				SpawnGroup.MISC,
				ThrowablePotionPhialEntity::new)
			.dimensions(EntityDimensions.fixed(
				0.25f,
				0.25f))
			.trackRangeBlocks(4)
			.trackedUpdateRate(10)
			.build();
	public static EntityType<? extends CoinEntity> COIN_ENTITY =
		FabricEntityTypeBuilder
			.<CoinEntity>create(SpawnGroup.MISC,
				CoinEntity::new)
			.dimensions(EntityDimensions.fixed(0.25f, 0.25f))
			.trackRangeBlocks(4)
			.trackedUpdateRate(10)
			.build();
	public static EntityType<? extends CrowEntity> CROW_ENTITY =
		FabricEntityTypeBuilder
			.<CrowEntity>create(SpawnGroup.MISC,
				CrowEntity::new)
			.dimensions(EntityDimensions.fixed(1, 2))
			.trackRangeChunks(10)
			.build();
}