package faceless.artent.objects;

import faceless.artent.api.item.ArtentItem;
import faceless.artent.brewing.item.*;
import faceless.artent.spells.item.SpellBook;
import faceless.artent.spells.item.StaffOfLight;
import faceless.artent.item.CrowStaff;
import faceless.artent.sharpening.item.EnhancerItem;
import faceless.artent.sharpening.item.SmithingHammer;
import faceless.artent.sharpening.item.upgrades.*;
import faceless.artent.trading.item.DarkBook;
import faceless.artent.transmutations.item.AlchemicalClock;
import faceless.artent.transmutations.item.AlchemicalCoal;
import faceless.artent.transmutations.item.AlchemicalPaper;
import faceless.artent.transmutations.item.Chalk;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.FoodComponent;
import net.minecraft.item.Item;

public final class ModItems {
	public static final ArtentItem PhiloStone = new ArtentItem("philosophers_stone");
	public static final Chalk Chalk = new Chalk(new FabricItemSettings().maxDamage(128));
	public static final AlchemicalClock AlchemicalClock = new AlchemicalClock();
	public static final AlchemicalCoal AlchemicalCoal = new AlchemicalCoal();
	public static final AlchemicalPaper AlchemicalPaper = new AlchemicalPaper();

	public static SmithingHammer SmithingHammer = new SmithingHammer(new FabricItemSettings().maxCount(1)
																		 .maxDamage(512));
	public static Item StoneOfTheSea = new Item(new FabricItemSettings().maxCount(64));
	public static Item FortitudeSpiritStone = new Item(new FabricItemSettings().maxCount(64));
	public static Item AmberSphere = new Item(new FabricItemSettings().maxCount(64));

	public static Item[] Catalysts = new Item[]{ StoneOfTheSea, FortitudeSpiritStone, AmberSphere };

	public static EnhancerItem EnderInversionUpgrade = new EnderInversion(new FabricItemSettings().maxCount(1));
	public static EnhancerItem NetherFireStone = new NetherFireStone(new FabricItemSettings().maxCount(1));
	public static EnhancerItem GoldenCrossUpgrade = new GoldenCross(new FabricItemSettings().maxCount(1));
	public static EnhancerItem PoisonBottleUpgrade = new PoisonBottle(new FabricItemSettings().maxCount(1));
	public static EnhancerItem VampiresFangUpgrade = new VampiresFang(new FabricItemSettings().maxCount(1));

	public static Item EmptyPhial = new Item(new Item.Settings().maxCount(64));
	public static Item EmptyPhialExplosive = new Item(new Item.Settings().maxCount(64));
	public static PotionPhial PotionPhial = new PotionPhial(new Item.Settings().maxCount(64));
	public static PotionPhialExplosive PotionPhialExplosive = new PotionPhialExplosive(new Item.Settings().maxCount(64));
	public static final FoodComponent Berry = new FoodComponent.Builder().hunger(2).saturationModifier(0.1f).build();
	public static Item CrimsonLeaf = new Item(new Item.Settings().maxCount(64));
	public static Item GoldenBucket = new Item(new Item.Settings().maxCount(1));
	public static Item GoldenBucketFilled = new FilledGoldenBucket(new Item.Settings().maxCount(1));
	public static Item SmallConcentratePhial = new Item(new Item.Settings().maxCount(64));
	public static Item SmallConcentrate = new SmallConcentrate(new Item.Settings().maxCount(64));
	public static Item MediumConcentratePhial = new Item(new Item.Settings().maxCount(64));
	public static Item MediumConcentrate = new MediumConcentrate(new Item.Settings().maxCount(1));
	public static Item BigConcentratePhial = new Item(new Item.Settings().maxCount(64));
	public static Item BigConcentrate = new BigConcentrate(new Item.Settings().maxCount(1));
	public static DarkBook DarkBook = new DarkBook(new Item.Settings().maxCount(1));
	public static CrowStaff CrowStaff = new CrowStaff(new Item.Settings().maxCount(1));
	public static SpellBook Spellbook1 = new SpellBook(1, new Item.Settings().maxCount(1));
	public static SpellBook Spellbook2 = new SpellBook(2, new Item.Settings().maxCount(1));
	public static SpellBook Spellbook3 = new SpellBook(3, new Item.Settings().maxCount(1));
	public static StaffOfLight StaffOfLight = new StaffOfLight(new FabricItemSettings().maxCount(1));

	public static final ArtentItem[] Coins = new ArtentItem[]{
		new ArtentItem("coin_bronze"),
		new ArtentItem("coin_silver"),
		new ArtentItem("coin_gold") };

	public static Item[] berries = new Item[]{
		new Item(new Item.Settings().maxCount(64).food(Berry)),
		new Item(new Item.Settings().maxCount(64).food(Berry)),
		new Item(new Item.Settings().maxCount(64).food(Berry)),
		new Item(new Item.Settings().maxCount(64).food(Berry)),
		new Item(new Item.Settings().maxCount(64).food(Berry)),
	};
}
