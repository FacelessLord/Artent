package faceless.artent.objects;

import faceless.artent.brewing.entity.ThrowablePotionPhialEntity;
import faceless.artent.mobs.entity.CrowEntity;
import faceless.artent.spells.entity.LightSwordProjectileEntity;
import faceless.artent.spells.entity.SpellParticleEntity;
import faceless.artent.trading.entity.CoinEntity;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.world.World;

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
			.dimensions(EntityDimensions.fixed(1, 1))
			.trackRangeChunks(10)
			.build();
	public static EntityType<? extends SpellParticleEntity> SPELL_PARTICLE =
		FabricEntityTypeBuilder
			.create(SpawnGroup.MISC,
				SpellParticleEntity::new)
			.dimensions(EntityDimensions.fixed(0.5f, 0.5f))
			.trackRangeChunks(10)
			.build();
	public static EntityType<? extends LightSwordProjectileEntity> LIGHT_SWORD =
		FabricEntityTypeBuilder
			.create(SpawnGroup.MISC,
					(EntityType<LightSwordProjectileEntity> type, World world) -> new LightSwordProjectileEntity(type, world))
			.dimensions(EntityDimensions.fixed(0.5f, 0.5f))
			.trackRangeChunks(10)
			.build();
}