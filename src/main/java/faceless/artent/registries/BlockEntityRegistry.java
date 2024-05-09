package faceless.artent.registries;

import faceless.artent.Artent;
import faceless.artent.brewing.blockEntities.BrewingCauldronBlockEntity;
import faceless.artent.brewing.blockEntities.FermentingBarrelBlockEntity;
import faceless.artent.objects.ModBlockEntities;
import faceless.artent.objects.ModBlocks;
import faceless.artent.sharpening.blockEntity.SharpeningAnvilBlockEntity;
import faceless.artent.spells.blockEntity.InscriptionTableBlockEntity;
import faceless.artent.spells.blockEntity.VoidBlockEntity;
import faceless.artent.trading.blockEntities.TraderBlockEntity;
import faceless.artent.transmutations.blockEntities.AlchemicalCircleEntity;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class BlockEntityRegistry implements IRegistry {
    @Override
    public void register() {
        ModBlockEntities.AlchemicalCircle = register(AlchemicalCircleEntity::new, "alchemical_circle",
                                                     ModBlocks.AlchemicalCircle);
        ModBlockEntities.SharpeningAnvil = register(SharpeningAnvilBlockEntity::new, "sharpening_anvil",
                                                    ModBlocks.SharpeningAnvil);
        ModBlockEntities.BrewingCauldron = register(BrewingCauldronBlockEntity::new,
                                                    "cauldron_entity",
                                                    ModBlocks.BrewingCauldron,
                                                    ModBlocks.BrewingCauldronCopper);
        ModBlockEntities.FermentingBarrel = register(FermentingBarrelBlockEntity::new,
                                                     "fermenting_barrel",
                                                     ModBlocks.FermentingBarrel);
        ModBlockEntities.Trader = register(TraderBlockEntity::new, "trader", ModBlocks.Trader);
        ModBlockEntities.InscriptionTable = register(InscriptionTableBlockEntity::new,
                                                     "inscription_table",
                                                     ModBlocks.InscriptionTable);
        ModBlockEntities.VoidBlock = register(VoidBlockEntity::new, "void", ModBlocks.VoidBlock);
    }

    public <T extends BlockEntity> BlockEntityType<T> register(
      FabricBlockEntityTypeBuilder.Factory<T> item, String id,
      Block... block
    ) {
        return Registry.register(Registries.BLOCK_ENTITY_TYPE, new Identifier(Artent.MODID, id),
                                 FabricBlockEntityTypeBuilder.create(item, block).build(null));
    }
}
