package faceless.artent.objects;

import faceless.artent.brewing.BrewingAutomata;
import faceless.artent.brewing.api.BrewingIngredient;
import faceless.artent.registries.BrewingRegistry;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

public class BrewingRecipes {

    public static final BrewingIngredient Shroom = new BrewingIngredient(ModBlocks.ShroomItem, -1);
    public static final BrewingIngredient Shadowveil = new BrewingIngredient(ModBlocks.ShadowveilItem, -1);
    public static final BrewingIngredient CrimsonwoodBerry = new BrewingIngredient(ModItems.berries[4], -1);
    public static final BrewingIngredient CrimsonLeaf = new BrewingIngredient(ModItems.CrimsonLeaf, -1);
    public static final BrewingIngredient Blackberry = new BrewingIngredient(ModItems.berries[0], -1);
    public static final BrewingIngredient Blueberry = new BrewingIngredient(ModItems.berries[1], -1);
    public static final BrewingIngredient Cloudberry = new BrewingIngredient(ModItems.berries[2], -1);
    public static final BrewingIngredient Raspberry = new BrewingIngredient(ModItems.berries[3], -1);
    public static final BrewingIngredient Bone = new BrewingIngredient(Items.BONE, -1);
    public static final BrewingIngredient GlowstoneDust = new BrewingIngredient(Items.GLOWSTONE_DUST, -1);
    public static final BrewingIngredient Redstone = new BrewingIngredient(Items.REDSTONE, -1);
    public static final BrewingIngredient Apple = new BrewingIngredient(Items.APPLE, -1);
    public static final BrewingIngredient BrownMushroom = new BrewingIngredient(Items.BROWN_MUSHROOM, -1);
    public static final BrewingIngredient RedMushroom = new BrewingIngredient(Items.RED_MUSHROOM, -1);
    public static final BrewingIngredient ChorusPlant = new BrewingIngredient(Items.CHORUS_FRUIT, -1);
    public static final BrewingIngredient BlazePowder = new BrewingIngredient(Items.BLAZE_POWDER, -1);
    public static final BrewingIngredient EnderPearl = new BrewingIngredient(Items.ENDER_PEARL, -1);
    public static final BrewingIngredient GlisteningMelon = new BrewingIngredient(Items.GLISTERING_MELON_SLICE, -1);
    public static final BrewingIngredient Kelp = new BrewingIngredient(Items.KELP, -1);
    public static final BrewingIngredient Slime = new BrewingIngredient(Items.SLIME_BALL, -1);
    public static final BrewingIngredient Snowball = new BrewingIngredient(Items.SNOWBALL, -1);
    public static final BrewingIngredient RabbitsFoot = new BrewingIngredient(Items.RABBIT_FOOT, -1);
    public static final BrewingIngredient PufferFish = new BrewingIngredient(Items.PUFFERFISH, -1);
    public static final BrewingIngredient Map = new BrewingIngredient(Items.MAP, -1);
    public static final BrewingIngredient DragonBreath = new BrewingIngredient(Items.DRAGON_BREATH, -1);
    public static final BrewingIngredient NetherWart = new BrewingIngredient(Items.NETHER_WART, -1);
    public static final BrewingIngredient SpiderEye = new BrewingIngredient(Items.SPIDER_EYE, -1);
    public static final BrewingIngredient FermentedSpiderEye = new BrewingIngredient(Items.FERMENTED_SPIDER_EYE, -1);
    public static final BrewingIngredient Allium = new BrewingIngredient(Items.ALLIUM, -1);
    public static final BrewingIngredient MagmaCream = new BrewingIngredient(Items.MAGMA_CREAM, -1);


    public static BrewingAutomata RecipeAutomata;

    public static boolean IsIngredient(ItemStack stack) {
        return BrewingRegistry.Ingredients.containsKey(new BrewingIngredient(stack.getItem(), -1));
    }

    public static BrewingIngredient AsIngredient(ItemStack stack) {
        var ignoreMetaIngredient = new BrewingIngredient(stack.getItem(), -1);
        if (BrewingRegistry.Ingredients.containsKey(ignoreMetaIngredient)) return ignoreMetaIngredient;
        return null;
    }
}