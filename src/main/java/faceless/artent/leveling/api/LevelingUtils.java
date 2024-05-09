package faceless.artent.leveling.api;

import faceless.artent.objects.ModEntities;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;

import java.util.Hashtable;

public class LevelingUtils {
    public static Hashtable<String, Integer> MOB_BASE_LEVEL = new Hashtable<>();

    public static <T extends LivingEntity> void setMobBaseLevel(EntityType<T> type, int level) {
        MOB_BASE_LEVEL.put(type.toString(), level);
    }

    public static void setupMobBaseLevels() {
        setMobBaseLevel(EntityType.ZOMBIE, 5);
        setMobBaseLevel(EntityType.SKELETON, 5);
        setMobBaseLevel(EntityType.SPIDER, 5);
        setMobBaseLevel(EntityType.CREEPER, 7);
        setMobBaseLevel(EntityType.DROWNED, 7);
        setMobBaseLevel(EntityType.AXOLOTL, 2);
        setMobBaseLevel(EntityType.BAT, 1);
        setMobBaseLevel(EntityType.BEE, 1);
        setMobBaseLevel(EntityType.BLAZE, 15);
        setMobBaseLevel(EntityType.CAMEL, 3);
        setMobBaseLevel(EntityType.CAVE_SPIDER, 6);
        setMobBaseLevel(EntityType.CHICKEN, 1);
        setMobBaseLevel(EntityType.COD, 1);
        setMobBaseLevel(EntityType.COW, 3);
        setMobBaseLevel(EntityType.DOLPHIN, 3);
        setMobBaseLevel(EntityType.DONKEY, 3);
        setMobBaseLevel(EntityType.GOAT, 4);
        setMobBaseLevel(EntityType.HORSE, 4);
        setMobBaseLevel(EntityType.ELDER_GUARDIAN, 20);
        setMobBaseLevel(EntityType.GUARDIAN, 15);
        setMobBaseLevel(EntityType.ENDER_DRAGON, 400);
        setMobBaseLevel(EntityType.ENDERMAN, 15);
        setMobBaseLevel(EntityType.EVOKER, 20);
        setMobBaseLevel(EntityType.ENDERMITE, 1);
        setMobBaseLevel(EntityType.SILVERFISH, 1);
        setMobBaseLevel(EntityType.FOX, 4);
        setMobBaseLevel(EntityType.FROG, 1);
        setMobBaseLevel(EntityType.GHAST, 15);
        setMobBaseLevel(EntityType.GIANT, 15);
        setMobBaseLevel(EntityType.GLOW_SQUID, 2);
        setMobBaseLevel(EntityType.SQUID, 2);
        setMobBaseLevel(EntityType.HOGLIN, 10);
        setMobBaseLevel(EntityType.HUSK, 10);
        setMobBaseLevel(EntityType.ILLUSIONER, 15);
        setMobBaseLevel(EntityType.IRON_GOLEM, 15);
        setMobBaseLevel(EntityType.LLAMA, 6);
        setMobBaseLevel(EntityType.MAGMA_CUBE, 8);
        setMobBaseLevel(EntityType.MOOSHROOM, 4);
        setMobBaseLevel(EntityType.MULE, 4);
        setMobBaseLevel(EntityType.OCELOT, 4);
        setMobBaseLevel(EntityType.PANDA, 4);
        setMobBaseLevel(EntityType.PARROT, 1);
        setMobBaseLevel(EntityType.PHANTOM, 8);
        setMobBaseLevel(EntityType.PIG, 4);
        setMobBaseLevel(EntityType.PIGLIN, 10);
        setMobBaseLevel(EntityType.PIGLIN_BRUTE, 15);
        setMobBaseLevel(EntityType.ZOMBIFIED_PIGLIN, 12);
        setMobBaseLevel(EntityType.POLAR_BEAR, 7);
        setMobBaseLevel(EntityType.PUFFERFISH, 1);
        setMobBaseLevel(EntityType.RABBIT, 1);
        setMobBaseLevel(EntityType.RAVAGER, 18);
        setMobBaseLevel(EntityType.SALMON, 18);
        setMobBaseLevel(EntityType.PILLAGER, 15);
        setMobBaseLevel(EntityType.SHEEP, 4);
        setMobBaseLevel(EntityType.SHULKER, 20);
        setMobBaseLevel(EntityType.SLIME, 3);
        setMobBaseLevel(EntityType.SNIFFER, 3);
        setMobBaseLevel(EntityType.SNOW_GOLEM, 3);
        setMobBaseLevel(EntityType.STRAY, 8);
        setMobBaseLevel(EntityType.STRIDER, 3);
        setMobBaseLevel(EntityType.TADPOLE, 1);
        setMobBaseLevel(EntityType.TRADER_LLAMA, 4);
        setMobBaseLevel(EntityType.TROPICAL_FISH, 1);
        setMobBaseLevel(EntityType.TURTLE, 4);
        setMobBaseLevel(EntityType.VEX, 4);
        setMobBaseLevel(EntityType.VILLAGER, 4);
        setMobBaseLevel(EntityType.VINDICATOR, 18);
        setMobBaseLevel(EntityType.WANDERING_TRADER, 4);
        setMobBaseLevel(EntityType.WARDEN, 30);
        setMobBaseLevel(EntityType.WITCH, 15);
        setMobBaseLevel(EntityType.WITHER_SKELETON, 15);
        setMobBaseLevel(EntityType.WOLF, 4);
        setMobBaseLevel(EntityType.ZOGLIN, 10);
        setMobBaseLevel(EntityType.ZOMBIE_VILLAGER, 5);
        setMobBaseLevel(ModEntities.CROW_ENTITY, 2);
    }

    public static <T extends LivingEntity> int getMobBaseLevel(T mob) {
        var mobKey = mob.getType().toString();
        if (MOB_BASE_LEVEL.containsKey(mobKey))
            return MOB_BASE_LEVEL.get(mobKey);
        return 5;
    }

    public static double getExperienceScalingByMobLevel(int playerLevel, int mobLevel) {
        if (playerLevel > mobLevel)
            return 2 * sigma((mobLevel - playerLevel) / 2d);
        else return Math.exp((mobLevel - playerLevel) / 4d);
    }

    public static int getSameLevelMobsToLevel(int playerLevel) {
        if (playerLevel < 5)
            return 5;
        if (playerLevel < 10)
            return 20;
        if (playerLevel < 20)
            return 40;
        if (playerLevel < 35)
            return 60;
        if (playerLevel < 70)
            return 80;
        return 100;
    }

    public static float getSpecialMobExperienceScaling(SpecialMobType type) {
        return switch (type) {
            case Common -> 1;
            case Cursed -> 1.3f;
            case Demonic -> 1.7f;
            case Eldritch -> 2.3f;
            case Wounded -> 0.7f;
            default -> 1;
        };
    }

    public static double sigma(double x) {
        var e = Math.exp(x);
        return e / (1 + e);
    }
}
