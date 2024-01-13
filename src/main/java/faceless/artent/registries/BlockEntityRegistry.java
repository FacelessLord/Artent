package faceless.artent.registries;

import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import faceless.artent.Artent;
import faceless.artent.objects.ModBlockEntities;
import faceless.artent.objects.ModBlocks;
import faceless.artent.transmutations.world.AlchemicalCircleEntity;

public class BlockEntityRegistry implements IRegistry {
	@Override
	public void register() {
		ModBlockEntities.alchemicalCircleEntity = register(AlchemicalCircleEntity::new, "alchemical_circle",
				ModBlocks.alchemicalCircle);
	}

	public <T extends BlockEntity> BlockEntityType<T> register(FabricBlockEntityTypeBuilder.Factory<T> item, String id,
			Block block) {
		return Registry.register(Registries.BLOCK_ENTITY_TYPE, new Identifier(Artent.MODID, id),
				FabricBlockEntityTypeBuilder.create(item, block).build(null));
	}
}
