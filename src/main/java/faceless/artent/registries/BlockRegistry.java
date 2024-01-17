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
		register(ModBlocks.AlchemicalCircle);
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
