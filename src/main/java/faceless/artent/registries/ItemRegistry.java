package faceless.artent.registries;

import faceless.artent.Artent;
import faceless.artent.api.item.INamed;
import faceless.artent.api.item.group.ArtentItemGroupBuilder;
import faceless.artent.brewing.api.AlchemicalPotionUtil;
import faceless.artent.brewing.ingridients.Ingredients;
import faceless.artent.objects.ModItemGroups;
import faceless.artent.objects.ModItems;
import faceless.artent.sharpening.item.EnhancerItem;
import net.fabricmc.fabric.api.registry.FuelRegistry;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

public final class ItemRegistry implements IRegistry {

    public void register() {
        // ALCHEMY
        register(ModItems.PhiloStone, ModItemGroups.Main);
        register(ModItems.AlchemicalClock, ModItemGroups.Main);
        register(ModItems.AlchemicalCoal, ModItemGroups.Main);
        FuelRegistry.INSTANCE.add(ModItems.AlchemicalCoal, 2400);
        register(ModItems.Chalk, ModItemGroups.Main);
        register(ModItems.AlchemicalPaper, ModItemGroups.Main);

        // SHARPENING
        register(ModItems.SmithingHammer, ModItemGroups.Main);
        register("stone_of_the_sea", ModItems.StoneOfTheSea, ModItemGroups.Main);
        register("fortitude_spirit_stone", ModItems.FortitudeSpiritStone, ModItemGroups.Main);
        register("amber_sphere", ModItems.AmberSphere, ModItemGroups.Main);

        registerEnhancer(ModItems.EnderInversionUpgrade, ModItemGroups.Main);
        registerEnhancer(ModItems.NetherFireStone, ModItemGroups.Main);
        registerEnhancer(ModItems.GoldenCrossUpgrade, ModItemGroups.Main);
        registerEnhancer(ModItems.PoisonBottleUpgrade, ModItemGroups.Main);
        registerEnhancer(ModItems.VampiresFangUpgrade, ModItemGroups.Main);

        // TRADING
        register(ModItems.DarkBook, ModItemGroups.Main);
        register(ModItems.Coins[0]);
        register(ModItems.Coins[1]);
        register(ModItems.Coins[2]);

        // BREWING
        for (int i = 0; i < 5; i++) {
            register(Ingredients.GetBerryName(i), ModItems.berries[i], ModItemGroups.Potions);
        }
        register("crimson_leaf", ModItems.CrimsonLeaf, ModItemGroups.Potions);
        register("empty_phial", ModItems.EmptyPhial, ModItemGroups.Potions);
        register("empty_phial_explosive", ModItems.EmptyPhialExplosive, ModItemGroups.Potions);
        register("small_concentrate_phial", ModItems.SmallConcentratePhial, ModItemGroups.Potions);
        register("medium_concentrate_phial", ModItems.MediumConcentratePhial, ModItemGroups.Potions);
        register("big_concentrate_phial", ModItems.BigConcentratePhial, ModItemGroups.Potions);
        register("golden_bucket", ModItems.GoldenBucket, ModItemGroups.Potions);
        register("golden_bucket_filled", ModItems.GoldenBucketFilled);

        register(ModItems.PotionPhial);
        AlchemicalPotionUtil.appendPotionStacks(ModItems.PotionPhial, ModItemGroups.Potions);
        register(ModItems.PotionPhialExplosive);
        AlchemicalPotionUtil.appendPotionStacks(ModItems.PotionPhialExplosive, ModItemGroups.Potions);

        register("small_concentrate", ModItems.SmallConcentrate);
        AlchemicalPotionUtil.appendFermentedPotionStacks(ModItems.SmallConcentrate, -1, ModItemGroups.Potions);
        register("medium_concentrate", ModItems.MediumConcentrate);
        AlchemicalPotionUtil.appendFermentedPotionStacks(ModItems.MediumConcentrate, 3, ModItemGroups.Potions);
        register("big_concentrate", ModItems.BigConcentrate);
        AlchemicalPotionUtil.appendFermentedPotionStacks(ModItems.BigConcentrate, 9, ModItemGroups.Potions);

        // staffs
        register(ModItems.CrowStaff, ModItemGroups.Main);
        register(ModItems.StaffOfLight, ModItemGroups.Main);
        register(ModItems.Spellbook1, ModItemGroups.Main);
        register(ModItems.Spellbook2, ModItemGroups.Main);
        register(ModItems.Spellbook3, ModItemGroups.Main);

        register(ModItems.SpellScroll);
        appendSpellScrollStacks(ModItems.SpellScroll, ModItemGroups.Main);

        register(ModItems.LightSword_SPECIAL);
        register("crow_spawn_egg", ModItems.CrowSpawnEgg, ModItemGroups.Main);
        register("mage_spawn_egg", ModItems.MageSpawnEgg, ModItemGroups.Main);
    }

    public void register(String itemId, Item item, ArtentItemGroupBuilder groupBuilder) {
        Registry.register(Registries.ITEM, new Identifier(Artent.MODID, itemId), item);
        groupBuilder.addItem(item);
    }

    public void registerEnhancer(EnhancerItem item, ArtentItemGroupBuilder groupBuilder) {
        Registry.register(Registries.ITEM, new Identifier(Artent.MODID, item.getId()), item);
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

    public static void appendSpellScrollStacks(Item base, ArtentItemGroupBuilder group) {
        List<ItemStack> stacks = new ArrayList<>();
        for (var spell : SpellRegistry.getAllSpells()) {
            var stack = new ItemStack(base);
            var tag = new NbtCompound();
            tag.putString("spell_id", spell.id);
            stack.setNbt(tag);
            stacks.add(stack);
        }
        group.Items.addAll(stacks);
    }

}
