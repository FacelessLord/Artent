package faceless.artent.registries;

import faceless.artent.brewing.api.AlchemicalPotion;

import java.util.Hashtable;

import static faceless.artent.objects.AlchemicalPotions.*;

public class AlchemicalPotionRegistry implements IRegistry {
    private static final Hashtable<String, AlchemicalPotion> RegisteredPotions = new Hashtable<>();
    private static final Hashtable<String, AlchemicalPotion> FermentedPotions = new Hashtable<>();

    public static Hashtable<String, AlchemicalPotion> getRegisteredPotions() {
        return RegisteredPotions;
    }

    public static Hashtable<String, AlchemicalPotion> getFermentedPotions() {
        return FermentedPotions;
    }

    @Override
    public void register() {
        register(POISON, INSTANT_HARM);
        register(STRENGTH);
        register(VAMPIRISM, FERMENTED_VAMPIRISM);
        register(HOLY_WATER, FERMENTED_HOLY_WATER);
        register(BERSERK);

        register(STONE_SKIN);
        register(FIRE_RESISTANCE);
        register(FREEZING);
        register(LIQUID_FLAME, FERMENTED_LIQUID_FLAME);
        register(HEALING, INSTANT_HEALING);
        register(ANTIDOTE, FERMENTED_ANTIDOTE);

        register(FAST_SWIMMING);
        register(WATER_BREATHING);
        register(JUMP_BOOST);
        register(FEATHER_FALLING);
        register(NIGHT_VISION);

        register(FLIGHT);
        register(LUCK);
        register(SATURATION, FERMENTED_SATURATION);
        register(SURFACE_TELEPORTATION);
        register(LUMBERJACK);
        register(HASTE);
    }

    public void register(AlchemicalPotion potion) {
        RegisteredPotions.put(potion.id, potion);
    }

    public void register(AlchemicalPotion potion, AlchemicalPotion fermented) {
        RegisteredPotions.put(potion.id, potion);
        FermentedPotions.put(potion.id, fermented);
    }
//
//	public static boolean potionIsRegistered(String id) {
//		return RegisteredPotions.containsKey(id);
//	}

    public static boolean fermentedPotionIsRegistered(String id) {
        return FermentedPotions.containsKey(id);
    }


}
