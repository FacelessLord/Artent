package faceless.artent.objects;

import faceless.artent.api.item.ArtentItem;
import faceless.artent.sharpening.item.EnhancerItem;
import faceless.artent.sharpening.item.SmithingHammer;
import faceless.artent.transmutations.item.AlchemicalClock;
import faceless.artent.transmutations.item.AlchemicalCoal;
import faceless.artent.transmutations.item.AlchemicalPaper;
import faceless.artent.transmutations.item.Chalk;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.Item;
import net.minecraft.util.Formatting;

public final class ModItems {

	public static final ArtentItem PhiloStone = new ArtentItem("philosophers_stone");
	public static final Chalk chalk = new Chalk();
	public static final AlchemicalClock alchemicalClock = new AlchemicalClock();
	public static final AlchemicalCoal alchemicalCoal = new AlchemicalCoal();
	public static final AlchemicalPaper alchemicalPaper = new AlchemicalPaper();

	public static SmithingHammer SmithingHammer = new SmithingHammer(new FabricItemSettings().maxCount(1));
	public static Item Catalyst0 = new Item(new FabricItemSettings().maxCount(64));
	public static Item Catalyst1 = new Item(new FabricItemSettings().maxCount(64));
	public static Item Catalyst2 = new Item(new FabricItemSettings().maxCount(64));
	public static EnhancerItem EnderInversionUpgrade = new EnhancerItem(Formatting.AQUA, "artent.upgrade.ender_inversion", new FabricItemSettings().maxCount(1));
}
