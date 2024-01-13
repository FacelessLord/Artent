package faceless.artent.registries;

import faceless.artent.Artent;
import faceless.artent.item.ArtentItem;
import faceless.artent.item.group.ArtentItemGroupBuilder;
import faceless.artent.objects.ModItemGroups;
import faceless.artent.objects.ModItems;
import net.fabricmc.fabric.api.registry.FuelRegistry;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public final class ItemRegistry implements IRegistry {

	public void register() {
		register(ModItems.PhiloStone, ModItemGroups.Main);
		register(ModItems.alchemicalClock, ModItemGroups.Main);
		register(ModItems.alchemicalCoal, ModItemGroups.Main);
		FuelRegistry.INSTANCE.add(ModItems.alchemicalCoal, 2400);
		register(ModItems.chalk, ModItemGroups.Main);
	}

	public void register(String itemId, Item item, ArtentItemGroupBuilder groupBuilder) {
		Registry.register(Registries.ITEM, new Identifier(Artent.MODID, itemId), item);
		groupBuilder.addItem(item);
	}

	public void register(ArtentItem item, ArtentItemGroupBuilder groupBuilder) {
		Registry.register(Registries.ITEM, new Identifier(Artent.MODID, item.Id), item);
		groupBuilder.addItem(item);
	}

	public void register(String itemId, Item item) {
		Registry.register(Registries.ITEM, new Identifier(Artent.MODID, itemId), item);
	}

	public void register(ArtentItem item) {
		Registry.register(Registries.ITEM, new Identifier(Artent.MODID, item.Id), item);
	}
}
