package faceless.artent.objects;

import faceless.artent.api.item.group.ArtentItemGroupBuilder;
import net.minecraft.item.ItemStack;

public class ModItemGroups {

	public static final ArtentItemGroupBuilder Main = new ArtentItemGroupBuilder(
		() -> new ItemStack(ModItems.PhiloStone),
		"main");
	public static final ArtentItemGroupBuilder Potions = new ArtentItemGroupBuilder(
		() -> new ItemStack(ModItems.PhiloStone),
		"potions");
}
