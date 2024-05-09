package faceless.artent.objects;

import faceless.artent.api.block.ArtentBlock;
import faceless.artent.brewing.block.*;
import faceless.artent.sharpening.block.SharpeningAnvil;
import faceless.artent.spells.block.InscriptionTable;
import faceless.artent.spells.block.InscriptionTablePt2;
import faceless.artent.spells.block.LightBlock;
import faceless.artent.spells.block.VoidBlock;
import faceless.artent.trading.block.Trader;
import faceless.artent.transmutations.block.AlchemicalCircleBlock;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.*;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.BlockItem;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;

public final class ModBlocks {

    /**
     * A shortcut to always return {@code false} a context predicate, used as
     * {@code settings.solidBlock(Blocks::never)}.
     * Copied from new.minecraft.block.Blocks
     */
    private static boolean never(BlockState state, BlockView world, BlockPos pos) {
        return false;
    }

    private static boolean never(BlockState state, BlockView world, BlockPos pos, EntityType<?> type) {
        return false;
    }

    public static final AlchemicalCircleBlock AlchemicalCircle = new AlchemicalCircleBlock(
      FabricBlockSettings
        .create()
        .notSolid()
        .collidable(false)
        .nonOpaque()
        .breakInstantly()
        .dropsNothing());
    public static SharpeningAnvil SharpeningAnvil = new SharpeningAnvil(
      FabricBlockSettings
        .copyOf(Blocks.IRON_BLOCK)
        .mapColor(MapColor.GRAY)
        .nonOpaque()
        .strength(2.0f, 3.0f)
        .sounds(BlockSoundGroup.METAL));

    public static ArtentBlock DungeonSandstone = new ArtentBlock("sandstone_dungeon_block",
                                                                 FabricBlockSettings
                                                                   .copyOf(Blocks.SANDSTONE)
                                                                   .sounds(BlockSoundGroup.STONE));
    public static BrewingCauldron BrewingCauldron = new BrewingCauldron(
      FabricBlockSettings
        .copyOf(Blocks.CAULDRON)
        .mapColor(MapColor.GRAY)
        .requiresTool()
        .nonOpaque()
        .luminance(state -> state.get(faceless.artent.brewing.block.BrewingCauldron.IS_BURNING) ? 15 : 0)
        .strength(2f));
    public static BlockItem BrewingCauldronItem;
    public static BrewingCauldronCopper BrewingCauldronCopper = new BrewingCauldronCopper(
      FabricBlockSettings
        .copyOf(Blocks.CAULDRON)
        .mapColor(MapColor.GRAY)
        .requiresTool()
        .nonOpaque()
        .luminance(state -> state.get(
          faceless.artent.brewing.block.BrewingCauldron.IS_BURNING) ? 15 : 0)
        .strength(2f));
    public static BlockItem BrewingCauldronCopperItem;
    public static Block CauldronFluid = new Block(
      FabricBlockSettings
        .copyOf(Blocks.WATER)
        .mapColor(MapColor.GRAY)
        .nonOpaque()
        .dropsNothing());
    public static MushroomPlantBlock Shroom = new MushroomPlantBlock(null,
                                                                     FabricBlockSettings.copyOf(Blocks.BROWN_MUSHROOM)
                                                                                        .mapColor(MapColor.BROWN)
                                                                                        .nonOpaque()
                                                                                        .noCollision()
                                                                                        .ticksRandomly()
                                                                                        .breakInstantly()
                                                                                        .sounds(BlockSoundGroup.GRASS)
                                                                                        .luminance(state -> 1)
                                                                                        .postProcess((a, b, c) -> true));
    public static BlockItem ShroomItem;
    public static FlowerBlock Shadowveil = new FlowerBlock(StatusEffects.BAD_OMEN,
                                                           5,
                                                           FabricBlockSettings.copyOf(Blocks.ALLIUM)
                                                                              .nonOpaque()
                                                                              .noCollision()
                                                                              .breakInstantly()
                                                                              .sounds(BlockSoundGroup.GRASS));
    public static BlockItem ShadowveilItem;
    public static BerryBush[] berryBush = new BerryBush[]{new BerryBush(0,
                                                                        FabricBlockSettings.copyOf(Blocks.ACACIA_LEAVES)
                                                                                           .mapColor(MapColor.GREEN)
                                                                                           .nonOpaque()
                                                                                           .sounds(BlockSoundGroup.GRASS)), new BerryBush(
      1,
      FabricBlockSettings.copyOf(Blocks.ACACIA_LEAVES)
                         .mapColor(MapColor.GREEN)
                         .nonOpaque()
                         .sounds(BlockSoundGroup.GRASS)), new BerryBush(2,
                                                                        FabricBlockSettings.copyOf(Blocks.ACACIA_LEAVES)
                                                                                           .mapColor(MapColor.GREEN)
                                                                                           .nonOpaque()
                                                                                           .sounds(BlockSoundGroup.GRASS)), new BerryBush(
      3,
      FabricBlockSettings.copyOf(Blocks.ACACIA_LEAVES)
                         .mapColor(MapColor.GREEN)
                         .nonOpaque()
                         .sounds(BlockSoundGroup.GRASS)),};
    public static BlockItem[] berryBushItem = new BlockItem[berryBush.length];

    public static PillarBlock CrimsonwoodLog =
      new PillarBlock(FabricBlockSettings
                        .copyOf(Blocks.OAK_LOG)
                        .mapColor(MapColor.SPRUCE_BROWN)
                        .strength(2.0f)
                        .sounds(BlockSoundGroup.WOOD));
    public static BlockItem CrimsonwoodLogItem;
    public static CrimsonwoodLeaves CrimsonwoodLeaves =
      new CrimsonwoodLeaves(FabricBlockSettings
                              .copyOf(Blocks.ACACIA_LEAVES)
                              .strength(0.2f)
                              .ticksRandomly()
                              .sounds(BlockSoundGroup.GRASS)
                              .allowsSpawning(ModBlocks::never)
                              .suffocates(ModBlocks::never)
                              .blockVision(ModBlocks::never));
    public static BlockItem CrimsonwoodLeavesItem;

    //	public static SaplingBlock CrimsonwoodSapling2 = new CrimsonwoodSapling(new CrimsonwoodSaplingGenerator(), FabricBlockSettings
//		.of(Material.PLANT)
//		.noCollision()
//		.ticksRandomly()
//		.breakInstantly()
//		.sounds(BlockSoundGroup.GRASS));
//	public static BlockItem CrimsonwoodSaplingItem = new BlockItem(CrimsonwoodSapling, new FabricItemSettings().group(Core.General));
    public static Block CrimsonwoodPlanks =
      new Block(FabricBlockSettings
                  .copyOf(Blocks.OAK_PLANKS)
                  .mapColor(MapColor.BROWN)
                  .strength(2.0f, 3.0f)
                  .sounds(BlockSoundGroup.WOOD));
    public static BlockItem CrimsonwoodPlanksItem;
    public static FermentingBarrel FermentingBarrel =
      new FermentingBarrel(FabricBlockSettings
                             .copyOf(Blocks.OAK_PLANKS)
                             .mapColor(MapColor.BROWN)
                             .nonOpaque()
                             .strength(2.0f, 3.0f)
                             .sounds(BlockSoundGroup.WOOD));
    public static BlockItem FermentingBarrelItem;

    public static Trader Trader =
      new Trader(FabricBlockSettings
                   .copyOf(Blocks.IRON_BLOCK)
                   .mapColor(MapColor.WHITE)
                   .nonOpaque()
                   .strength(2.0f, 3.0f)
                   .sounds(BlockSoundGroup.WOOD));
    public static BlockItem TraderItem;
    public static faceless.artent.spells.block.LightBlock LightBlock =
      new LightBlock(
        FabricBlockSettings
          .copyOf(Blocks.AIR)
          .air()
          .notSolid()
          .nonOpaque()
          .noCollision()
          .collidable(false)
          .pistonBehavior(PistonBehavior.DESTROY)
          .luminance(15));
    public static faceless.artent.spells.block.VoidBlock VoidBlock =
      new VoidBlock(
        FabricBlockSettings
          .copyOf(Blocks.STONE)
          .nonOpaque()
          .noCollision()
          .solid()
          .collidable(false)
          .luminance(15));

    public static InscriptionTable InscriptionTable =
      new InscriptionTable(FabricBlockSettings
                             .copyOf(Blocks.OAK_PLANKS)
                             .nonOpaque()
                             .strength(2.0f, 3.0f)
                             .sounds(BlockSoundGroup.WOOD));
    public static BlockItem InscriptionTableItem;

    public static InscriptionTablePt2 InscriptionTable2 =
      new InscriptionTablePt2(FabricBlockSettings
                                .copyOf(Blocks.OAK_PLANKS)
                                .nonOpaque()
                                .strength(2.0f, 3.0f)
                                .sounds(BlockSoundGroup.WOOD));
}
