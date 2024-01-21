package faceless.artent.brewing.item;

import faceless.artent.api.item.INamed;
import faceless.artent.brewing.api.AlchemicalPotionUtil;
import faceless.artent.objects.ModItems;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsage;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.stat.Stats;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class PotionPhial extends Item implements INamed {
	public PotionPhial(Settings settings) {
		super(settings);
	}

	@Override
	public String getId() {
		return "potion_phial";
	}

	@Override
	public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
		PlayerEntity playerEntity = user instanceof PlayerEntity ? (PlayerEntity) user : null;
		if (playerEntity instanceof ServerPlayerEntity) {
			Criteria.CONSUME_ITEM.trigger((ServerPlayerEntity) playerEntity, stack);
		}
		if (!world.isClient) {
			var potion = AlchemicalPotionUtil.getPotion(stack);
			AlchemicalPotionUtil.applyPotionEffects(user, playerEntity, potion);
		}
		if (playerEntity != null) {
			playerEntity.incrementStat(Stats.USED.getOrCreateStat(this));
			if (!playerEntity.getAbilities().creativeMode) {
				ItemUsage.exchangeStack(stack, playerEntity, new ItemStack(ModItems.EmptyPhial));
			}
		}
		world.emitGameEvent(user, GameEvent.DRINK, user.getPos());
		return stack;
	}

	@Override
	public int getMaxUseTime(ItemStack stack) {
		return 32;
	}

	@Override
	public UseAction getUseAction(ItemStack stack) {
		return UseAction.DRINK;
	}

	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
		return ItemUsage.consumeHeldItem(world, user, hand);
	}

	@Override
	public Text getName(ItemStack stack) {
		var key = stack.getOrCreateNbt().getString("potionKey");
		return Text.translatable("item.potion." + key);
	}

	@Override
	public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
		var tickRate = world == null ? 20.0f : world.getTickManager().getTickRate();
		AlchemicalPotionUtil.buildTooltip(stack, tooltip, 1.0f, tickRate);
	}
}