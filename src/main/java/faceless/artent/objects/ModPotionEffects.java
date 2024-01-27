package faceless.artent.objects;

import faceless.artent.Artent;
import faceless.artent.api.AttributeUuids;
import faceless.artent.api.math.Color;
import faceless.artent.brewing.api.ArtentStatusEffect;
import faceless.artent.brewing.api.ConcentrateStatusEffect;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModPotionEffects {
	public static final StatusEffect VAMPIRISM = register("vampirism", new ArtentStatusEffect(StatusEffectCategory.BENEFICIAL, Color.Red));
	public static final StatusEffect HOLY_WATER = register("holy_water", new ArtentStatusEffect(StatusEffectCategory.BENEFICIAL, Color.Red));
	public static final StatusEffect BERSERK = register("berserk", new ArtentStatusEffect(StatusEffectCategory.BENEFICIAL, Color.Red)
		.addAttributeModifier(EntityAttributes.GENERIC_MOVEMENT_SPEED, AttributeUuids.BerserkModifier.toString(), 0.30, EntityAttributeModifier.Operation.MULTIPLY_TOTAL)
		.addAttributeModifier(EntityAttributes.GENERIC_ATTACK_DAMAGE, AttributeUuids.BerserkModifier.toString(), 0.30, EntityAttributeModifier.Operation.MULTIPLY_TOTAL)
		.addAttributeModifier(EntityAttributes.GENERIC_ARMOR_TOUGHNESS, AttributeUuids.BerserkModifier.toString(), 0.30, EntityAttributeModifier.Operation.MULTIPLY_TOTAL)
		.addAttributeModifier(EntityAttributes.GENERIC_ATTACK_KNOCKBACK, AttributeUuids.BerserkModifier.toString(), 0.30, EntityAttributeModifier.Operation.MULTIPLY_TOTAL)
		.addAttributeModifier(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, AttributeUuids.BerserkModifier.toString(), 0.30, EntityAttributeModifier.Operation.MULTIPLY_TOTAL)
		.addAttributeModifier(EntityAttributes.GENERIC_MAX_HEALTH, AttributeUuids.BerserkModifier.toString(), 0.30, EntityAttributeModifier.Operation.MULTIPLY_TOTAL));
	public static final StatusEffect BERSERK_RECOIL = register("berserk_recoil", new ArtentStatusEffect(StatusEffectCategory.HARMFUL, Color.Red)
		.addAttributeModifier(EntityAttributes.GENERIC_MOVEMENT_SPEED, AttributeUuids.BerserkModifier.toString(), -0.5, EntityAttributeModifier.Operation.MULTIPLY_TOTAL)
		.addAttributeModifier(EntityAttributes.GENERIC_ATTACK_DAMAGE, AttributeUuids.BerserkModifier.toString(), -0.5, EntityAttributeModifier.Operation.MULTIPLY_TOTAL)
		.addAttributeModifier(EntityAttributes.GENERIC_ARMOR_TOUGHNESS, AttributeUuids.BerserkModifier.toString(), -0.5, EntityAttributeModifier.Operation.MULTIPLY_TOTAL)
		.addAttributeModifier(EntityAttributes.GENERIC_ATTACK_KNOCKBACK, AttributeUuids.BerserkModifier.toString(), -0.5, EntityAttributeModifier.Operation.MULTIPLY_TOTAL)
		.addAttributeModifier(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, AttributeUuids.BerserkModifier.toString(), -0.5, EntityAttributeModifier.Operation.MULTIPLY_TOTAL)
		.addAttributeModifier(EntityAttributes.GENERIC_MAX_HEALTH, AttributeUuids.BerserkModifier.toString(), -0.5, EntityAttributeModifier.Operation.MULTIPLY_TOTAL));
	public static final StatusEffect STONE_SKIN = register("stone_skin", new ArtentStatusEffect(StatusEffectCategory.BENEFICIAL, Color.Red));
	public static final StatusEffect FREEZING = register("freezing", new ArtentStatusEffect(StatusEffectCategory.BENEFICIAL, Color.Red, false)
		.addAttributeModifier(EntityAttributes.GENERIC_MOVEMENT_SPEED, AttributeUuids.Freezing.toString(), -0.8, EntityAttributeModifier.Operation.MULTIPLY_TOTAL));
	public static final StatusEffect LIQUID_FLAME = register("liquid_flame", new ArtentStatusEffect(StatusEffectCategory.BENEFICIAL, Color.Red));
	public static final StatusEffect ANTIDOTE = register("antidote", new ArtentStatusEffect(StatusEffectCategory.BENEFICIAL, Color.Red));
	public static final StatusEffect FAST_SWIMMING = register("fast_swimming", new ArtentStatusEffect(StatusEffectCategory.BENEFICIAL, Color.Red)
		.addAttributeModifier(EntityAttributes.GENERIC_MOVEMENT_SPEED, AttributeUuids.SwimmingModifier.toString(), 1, EntityAttributeModifier.Operation.MULTIPLY_BASE)
	);
	public static final StatusEffect FEATHER_FALLING = register("feather_falling", new ArtentStatusEffect(StatusEffectCategory.BENEFICIAL, Color.Red));
	public static final StatusEffect FLIGHT = register("flight", new ArtentStatusEffect(StatusEffectCategory.BENEFICIAL, Color.Red));
	public static final StatusEffect SURFACE_TELEPORTATION = register("surface_teleportation", new ArtentStatusEffect(StatusEffectCategory.BENEFICIAL, Color.Red, true));
	public static final StatusEffect LUMBERJACK = register("lumberjack", new ArtentStatusEffect(StatusEffectCategory.BENEFICIAL, Color.Red));
	// TODO use vanilla effects if possible
	public static final StatusEffect REGENERATION = register("regeneration", new ArtentStatusEffect(StatusEffectCategory.BENEFICIAL, Color.Red));

	public static final StatusEffect INSTANT_HEALING = register("instant_healing", new ConcentrateStatusEffect(StatusEffectCategory.BENEFICIAL, Color.Red));
	public static final StatusEffect INSTANT_HARM = register("instant_harm", new ConcentrateStatusEffect(StatusEffectCategory.HARMFUL, Color.Red));
	public static final StatusEffect FERMENTED_VAMPIRISM = register("fermented_vampirism", new ConcentrateStatusEffect(StatusEffectCategory.BENEFICIAL, Color.Red, false));
	public static final StatusEffect FERMENTED_HOLY_WATER = register("fermented_holy_water", new ConcentrateStatusEffect(StatusEffectCategory.BENEFICIAL, Color.Red, false));
	public static final StatusEffect FERMENTED_ANTIDOTE = register("fermented_antidote", new ConcentrateStatusEffect(StatusEffectCategory.BENEFICIAL, Color.Red));
	public static final StatusEffect FERMENTED_SATURATION = register("fermented_saturation", new ConcentrateStatusEffect(StatusEffectCategory.BENEFICIAL, Color.Red));
	public static final StatusEffect FERMENTED_LIQUID_FLAME = register("fermented_liquid_flame", new ConcentrateStatusEffect(StatusEffectCategory.BENEFICIAL, Color.Red));

	public void registerAll() {

	}

	private static StatusEffect register(String id, StatusEffect entry) {
		return Registry.register(Registries.STATUS_EFFECT, new Identifier(Artent.MODID, id), entry);
	}
}