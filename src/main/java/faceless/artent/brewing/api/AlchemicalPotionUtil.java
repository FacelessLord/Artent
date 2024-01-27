package faceless.artent.brewing.api;

import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import faceless.artent.api.item.group.ArtentItemGroupBuilder;
import faceless.artent.api.math.Color;
import faceless.artent.registries.AlchemicalPotionRegistry;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.AttributeModifierCreator;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffectUtil;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.stat.Stats;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AlchemicalPotionUtil {
	private static final Text NONE_TEXT = Text.translatable("effect.none").formatted(Formatting.GRAY);

	public static void buildTooltip(ItemStack stack, List<Text> list, float durationMultiplier, float tickRate) {
		var potion = getPotion(stack);
		List<StatusEffectInstance> effects = potion.getEffects();
		ArrayList<Pair<EntityAttribute, EntityAttributeModifier>> modifiers = Lists.newArrayList();
		if (effects.isEmpty()) {
			list.add(NONE_TEXT);
		} else {
			for (StatusEffectInstance statusEffectInstance : effects) {
				MutableText mutableText = Text.translatable(statusEffectInstance.getTranslationKey());
				StatusEffect statusEffect = statusEffectInstance.getEffectType();
				Map<EntityAttribute, AttributeModifierCreator> map = statusEffect.getAttributeModifiers();
				if (!map.isEmpty()) {
					for (Map.Entry<EntityAttribute, AttributeModifierCreator> entry : map.entrySet()) {
						modifiers.add(new Pair<>(entry.getKey(), entry.getValue().createAttributeModifier(statusEffectInstance.getAmplifier())));
					}
				}
				if (statusEffectInstance.getAmplifier() > 0) {
					mutableText = Text.translatable("potion.withAmplifier", mutableText, Text.translatable("potion.potency." + statusEffectInstance.getAmplifier()));
				}
				if (!statusEffectInstance.isDurationBelow(20)) {
					mutableText = Text.translatable("potion.withDuration", mutableText, StatusEffectUtil.getDurationText(statusEffectInstance, durationMultiplier, tickRate));
				}
				list.add(mutableText.formatted(statusEffect.getCategory().getFormatting()));
			}
		}
		if (!modifiers.isEmpty()) {
			list.add(ScreenTexts.EMPTY);
			list.add(Text.translatable("potion.whenDrank").formatted(Formatting.DARK_PURPLE));
			for (Pair<EntityAttribute, EntityAttributeModifier> pair : modifiers) {
				EntityAttributeModifier entityAttributeModifier = pair.getSecond();
				double d = entityAttributeModifier.getValue();
				double e = entityAttributeModifier.getOperation() == EntityAttributeModifier.Operation.MULTIPLY_BASE || entityAttributeModifier.getOperation() == EntityAttributeModifier.Operation.MULTIPLY_TOTAL ? entityAttributeModifier.getValue() * 100.0 : entityAttributeModifier.getValue();
				if (d > 0.0) {
					list.add(Text.translatable("attribute.modifier.plus." + entityAttributeModifier.getOperation().getId(), ItemStack.MODIFIER_FORMAT.format(e), Text.translatable(pair.getFirst().getTranslationKey())).formatted(Formatting.BLUE));
					continue;
				}
				if (!(d < 0.0)) continue;
				list.add(Text.translatable("attribute.modifier.take." + entityAttributeModifier.getOperation().getId(), ItemStack.MODIFIER_FORMAT.format(e *= -1.0), Text.translatable(pair.getFirst().getTranslationKey())).formatted(Formatting.RED));
			}
		}
	}


	public static AlchemicalPotion getPotion(ItemStack stack) {
		var key = stack.getOrCreateNbt().getString("potionKey");
		return AlchemicalPotionRegistry.getRegisteredPotions().getOrDefault(key, null);
	}

	public static AlchemicalPotion getFermentedPotion(ItemStack stack) {
		var key = stack.getOrCreateNbt().getString("potionKey");
		return AlchemicalPotionRegistry.getFermentedPotions().getOrDefault(key, null);
	}

	public static int getColor(ItemStack itemStack) {
		var potion = getPotion(itemStack);
		return potion != null ? potion.color.addNoMixing(new Color(40, 40, 40)).toHex() : 0;
	}

	public static List<StatusEffectInstance> getPotionEffects(ItemStack itemStack) {
		return getPotion(itemStack).getEffects();
	}

	public static ItemStack setPotion(ItemStack stack, AlchemicalPotion potion) {
		var tag = stack.getOrCreateNbt();
		tag.putString("potionKey", potion.id);
		return stack;
	}

	public static void setConcentrateAmount(ItemStack stack, int amount) {
		var tag = stack.getOrCreateNbt();
		tag.putInt("amount", amount);
	}

	public static int getConcentrateAmount(ItemStack stack) {
		var tag = stack.getNbt();
		return tag == null ? 0 : tag.getInt("amount");
	}

	public static ItemStack setFermentedPotion(ItemStack stack, String potionKey, int amount) {
		setFermentedPotion(stack, potionKey);
		setConcentrateAmount(stack, amount);
		return stack;
	}

	public static ItemStack setFermentedPotion(ItemStack stack, String potionKey) {
		var tag = stack.getOrCreateNbt();
		tag.putString("potionKey", potionKey);
		return stack;
	}

	public static void createEffectTooltip(ItemStack stack, List<Text> tooltip) {
		var potion = AlchemicalPotionUtil.getPotion(stack);
		if (potion != null) {
			potion.getEffects().stream()
				.map(sei -> Text.translatable(sei.getTranslationKey()).formatted(sei.getEffectType().getCategory().getFormatting()))
				.forEach(tooltip::add);
		} else
			tooltip.add(Text.translatable("text.artent.potion.unidentified"));
	}

	public static void createFermentedEffectTooltip(ItemStack stack, List<Text> tooltip) {
		var potion = AlchemicalPotionUtil.getFermentedPotion(stack);
		if (potion != null) {
			potion.getEffects().stream()
				.map(sei -> Text.translatable(sei.getTranslationKey()).formatted(sei.getEffectType().getCategory().getFormatting()))
				.forEach(tooltip::add);
		} else
			tooltip.add(Text.translatable("text.artent.potion.unidentified"));
	}

	public static void applyPotionEffects(LivingEntity user, PlayerEntity playerEntity, AlchemicalPotion potion) {
		List<StatusEffectInstance> list = potion.getEffects();
		for (StatusEffectInstance statusEffectInstance : list) {
			if (statusEffectInstance.getEffectType().isInstant()) {
				statusEffectInstance.getEffectType().applyInstantEffect(playerEntity, playerEntity, user, statusEffectInstance.getAmplifier(), 1.0);
				continue;
			}
			user.addStatusEffect(new StatusEffectInstance(statusEffectInstance));
		}
	}

	public static ItemStack drinkFermentedPotion(ItemStack stack, World world, PlayerEntity playerEntity, Item phial) {
		if (playerEntity instanceof ServerPlayerEntity serverPlayer) {
			Criteria.CONSUME_ITEM.trigger(serverPlayer, stack);
		}
		if (!world.isClient) {
			var potion = AlchemicalPotionUtil.getFermentedPotion(stack);
			if (potion != null)
				AlchemicalPotionUtil.applyPotionEffects(playerEntity, playerEntity, potion);
		}
		world.emitGameEvent(playerEntity, GameEvent.DRINK, playerEntity.getPos());
		playerEntity.incrementStat(Stats.USED.getOrCreateStat(stack.getItem()));
		if (!playerEntity.getAbilities().creativeMode) {
			var amount = stack.getOrCreateNbt().getInt("amount");
			if (amount > 1)
				stack.getOrCreateNbt().putInt("amount", amount - 1);
			else {
				stack.decrement(1);
				if (stack.getCount() == 0)
					return new ItemStack(phial);
			}
		}
		return stack;
	}

	public static void appendFermentedPotionStacks(Item base, int size, ArtentItemGroupBuilder group) {
		List<ItemStack> stacks = new ArrayList<>();
		for (var key : AlchemicalPotionRegistry.getRegisteredPotions().keySet()) {
			if (AlchemicalPotionRegistry.fermentedPotionIsRegistered(key)) {
				var stack = new ItemStack(base);
				var tag = new NbtCompound();
				tag.putString("potionKey", key);
				if (size != -1)
					tag.putInt("amount", size);
				stack.setNbt(tag);
				stacks.add(stack);
			}
		}
		group.Items.addAll(stacks);
	}

	public static void appendPotionStacks(Item base, ArtentItemGroupBuilder group) {
		List<ItemStack> stacks = new ArrayList<>();
		for (var key : AlchemicalPotionRegistry.getRegisteredPotions().keySet()) {
			var stack = new ItemStack(base);
			var tag = new NbtCompound();
			tag.putString("potionKey", key);
			stack.setNbt(tag);
			stacks.add(stack);
		}
		group.Items.addAll(stacks);
	}
}