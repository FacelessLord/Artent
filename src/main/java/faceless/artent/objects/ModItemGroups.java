package faceless.artent.objects;

import faceless.artent.item.group.ArtentItemGroupBuilder;
import net.minecraft.item.ItemStack;

public class ModItemGroups {

	public static final ArtentItemGroupBuilder Main = new ArtentItemGroupBuilder(
			() -> new ItemStack(ModItems.PhiloStone),
			"main");
}
