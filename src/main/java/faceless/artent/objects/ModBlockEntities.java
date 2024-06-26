package faceless.artent.objects;

import faceless.artent.brewing.blockEntities.BrewingCauldronBlockEntity;
import faceless.artent.brewing.blockEntities.FermentingBarrelBlockEntity;
import faceless.artent.sharpening.blockEntity.SharpeningAnvilBlockEntity;
import faceless.artent.spells.blockEntity.InscriptionTableBlockEntity;
import faceless.artent.spells.blockEntity.VoidBlockEntity;
import faceless.artent.trading.blockEntities.TraderBlockEntity;
import faceless.artent.transmutations.blockEntities.AlchemicalCircleEntity;
import net.minecraft.block.entity.BlockEntityType;

public class ModBlockEntities {
    public static BlockEntityType<AlchemicalCircleEntity> AlchemicalCircle;
    public static BlockEntityType<SharpeningAnvilBlockEntity> SharpeningAnvil;
    public static BlockEntityType<BrewingCauldronBlockEntity> BrewingCauldron;
    public static BlockEntityType<FermentingBarrelBlockEntity> FermentingBarrel;
    public static BlockEntityType<TraderBlockEntity> Trader;
    public static BlockEntityType<InscriptionTableBlockEntity> InscriptionTable;
    public static BlockEntityType<VoidBlockEntity> VoidBlock;
}
