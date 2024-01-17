package faceless.artent.registries;

import faceless.artent.Artent;
import faceless.artent.api.item.INamed;
import faceless.artent.api.item.group.ArtentItemGroupBuilder;
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
		register(ModItems.alchemicalPaper, ModItemGroups.Main);

		register(ModItems.SmithingHammer, ModItemGroups.Main);
		register("catalyst_0", ModItems.Catalyst0, ModItemGroups.Main);
		register("catalyst_1", ModItems.Catalyst1, ModItemGroups.Main);
		register("catalyst_2", ModItems.Catalyst2, ModItemGroups.Main);
		register("enderinversion_upgrade", ModItems.EnderInversionUpgrade, ModItemGroups.Main);
	}

	public void register(String itemId, Item item, ArtentItemGroupBuilder groupBuilder) {
		Registry.register(Registries.ITEM, new Identifier(Artent.MODID, itemId), item);
		groupBuilder.addItem(item);
	}

	public <T extends Item & INamed> void register(T item, ArtentItemGroupBuilder groupBuilder) {
		Registry.register(Registries.ITEM, new Identifier(Artent.MODID, item.getId()), item);
		groupBuilder.addItem(item);
	}

	public void register(String itemId, Item item) {
		Registry.register(Registries.ITEM, new Identifier(Artent.MODID, itemId), item);
	}

	public <T extends Item & INamed> void register(T item) {
		Registry.register(Registries.ITEM, new Identifier(Artent.MODID, item.getId()), item);
	}
}
