package faceless.artent.registries;

import faceless.artent.Artent;
import faceless.artent.block.ArtentBlock;
import faceless.artent.item.group.ArtentItemGroupBuilder;
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
		register(ModBlocks.alchemicalCircle.getBlockId(), ModBlocks.alchemicalCircle, ModItemGroups.Main);
	}

	public void register(String blockId, Block block, ArtentItemGroupBuilder groupBuilder) {
		Registry.register(Registries.BLOCK, new Identifier(Artent.MODID, blockId), block);
		var blockItem = new BlockItem(block, new FabricItemSettings());
		Registry.register(Registries.ITEM, new Identifier(Artent.MODID, blockId), blockItem);
		groupBuilder.addItem(blockItem);
	}

	public void register(ArtentBlock block, ArtentItemGroupBuilder groupBuilder) {
		Registry.register(Registries.BLOCK, new Identifier(Artent.MODID, block.Id), block);
		var blockItem = new BlockItem(block, new FabricItemSettings());
		Registry.register(Registries.ITEM, new Identifier(Artent.MODID, block.Id), blockItem);
		groupBuilder.addItem(blockItem);
	}

	public void register(String blockId, Block block) {
		Registry.register(Registries.BLOCK, new Identifier(Artent.MODID, blockId), block);
		var blockItem = new BlockItem(block, new FabricItemSettings());
		Registry.register(Registries.ITEM, new Identifier(Artent.MODID, blockId), blockItem);
	}

	public void register(ArtentBlock block) {
		Registry.register(Registries.BLOCK, new Identifier(Artent.MODID, block.Id), block);
		var blockItem = new BlockItem(block, new FabricItemSettings());
		Registry.register(Registries.ITEM, new Identifier(Artent.MODID, block.Id), blockItem);
	}
}
