package faceless.artent.registries;

import faceless.artent.Artent;
import faceless.artent.api.item.INamed;
import faceless.artent.api.item.group.ArtentItemGroupBuilder;
import faceless.artent.objects.ModBlocks;
import faceless.artent.objects.ModItemGroups;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class BlockRegistry implements IRegistry {

	@Override
	public void register() {
		// ALCHEMY
		register(ModBlocks.AlchemicalCircle);
		register(ModBlocks.DungeonSandstone, ModItemGroups.Main);

		// BREWING
		ModBlocks.BrewingCauldronItem = register(ModBlocks.BrewingCauldron, ModItemGroups.Potions);
		ModBlocks.BrewingCauldronCopperItem = register(ModBlocks.BrewingCauldronCopper, ModItemGroups.Potions);
		ModBlocks.ShroomItem = register("shroom", ModBlocks.Shroom, ModItemGroups.Potions);
		ModBlocks.ShadowveilItem = register("shadowveil", ModBlocks.Shadowveil, ModItemGroups.Potions);
		ModBlocks.CrimsonwoodLogItem = register("crimsonwood_log", ModBlocks.CrimsonwoodLog, ModItemGroups.Potions);
		ModBlocks.CrimsonwoodPlanksItem = register("crimsonwood_planks", ModBlocks.CrimsonwoodPlanks, ModItemGroups.Potions);
		ModBlocks.CrimsonwoodLeavesItem = register(ModBlocks.CrimsonwoodLeaves, ModItemGroups.Potions);
		// TODO sapling
//		register(ModBlocks.CrimsonwoodSapling, ModBlocks.CrimsonwoodSaplingItem, "crimsonwood_sapling");
		for (int i = 0; i < 4; i++) {
			ModBlocks.berryBushItem[i] = register(ModBlocks.berryBush[i], ModItemGroups.Potions);
		}

		register("cauldron_fluid", ModBlocks.CauldronFluid);
		ModBlocks.FermentingBarrelItem = register(ModBlocks.FermentingBarrel, ModItemGroups.Potions);

		// SHARPENING
		ModBlocks.SharpeningAnvil.Item = register(ModBlocks.SharpeningAnvil, ModItemGroups.Main);
	}

	public BlockItem register(String blockId, Block block, ArtentItemGroupBuilder groupBuilder) {
		Registry.register(Registries.BLOCK, new Identifier(Artent.MODID, blockId), block);
		var blockItem = new BlockItem(block, new FabricItemSettings());
		Registry.register(Registries.ITEM, new Identifier(Artent.MODID, blockId), blockItem);
		groupBuilder.addItem(blockItem);
		return blockItem;
	}

	public <T extends Block & INamed> BlockItem register(T block, ArtentItemGroupBuilder groupBuilder) {
		Registry.register(Registries.BLOCK, new Identifier(Artent.MODID, block.getId()), block);
		var blockItem = new BlockItem(block, new FabricItemSettings());
		Registry.register(Registries.ITEM, new Identifier(Artent.MODID, block.getId()), blockItem);
		groupBuilder.addItem(blockItem);
		return blockItem;
	}

	public BlockItem register(String blockId, Block block) {
		Registry.register(Registries.BLOCK, new Identifier(Artent.MODID, blockId), block);
		var blockItem = new BlockItem(block, new FabricItemSettings());
		Registry.register(Registries.ITEM, new Identifier(Artent.MODID, blockId), blockItem);
		return blockItem;
	}

	public <T extends Block & INamed> BlockItem register(T block) {
		Registry.register(Registries.BLOCK, new Identifier(Artent.MODID, block.getId()), block);
		var blockItem = new BlockItem(block, new FabricItemSettings());
		Registry.register(Registries.ITEM, new Identifier(Artent.MODID, block.getId()), blockItem);
		return blockItem;
	}
}
