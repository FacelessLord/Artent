package faceless.artent.registries;

import faceless.artent.Artent;
import faceless.artent.objects.ModPotionEffects;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class StatusEffectsRegistry implements IRegistry {
    @Override
    public void register() {
        register("vampirism", ModPotionEffects.VAMPIRISM);
        register("holy_water", ModPotionEffects.HOLY_WATER);
        register("berserk", ModPotionEffects.BERSERK);
        register("berserk_recoil", ModPotionEffects.BERSERK_RECOIL);
        register("stone_skin", ModPotionEffects.STONE_SKIN);
        register("freezing", ModPotionEffects.FREEZING);
        register("liquid_flame", ModPotionEffects.LIQUID_FLAME);
        register("antidote", ModPotionEffects.ANTIDOTE);
        register("fast_swimming", ModPotionEffects.FAST_SWIMMING);
        register("feather_falling", ModPotionEffects.FEATHER_FALLING);
        register("flight", ModPotionEffects.FLIGHT);
        register("surface_teleportation", ModPotionEffects.SURFACE_TELEPORTATION);
        register("lumberjack", ModPotionEffects.LUMBERJACK);
        register("fermented_vampirism", ModPotionEffects.FERMENTED_VAMPIRISM);
        register("fermented_holy_water", ModPotionEffects.FERMENTED_HOLY_WATER);
        register("fermented_antidote", ModPotionEffects.FERMENTED_ANTIDOTE);
        register("fermented_saturation", ModPotionEffects.FERMENTED_SATURATION);
        register("fermented_liquid_flame", ModPotionEffects.FERMENTED_LIQUID_FLAME);
    }

    private static void register(String id, StatusEffect entry) {
        Registry.register(Registries.STATUS_EFFECT, new Identifier(Artent.MODID, id), entry);
    }
}
