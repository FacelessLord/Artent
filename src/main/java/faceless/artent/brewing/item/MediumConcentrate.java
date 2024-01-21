package faceless.artent.brewing.item;

import faceless.artent.api.item.INamed;
import faceless.artent.brewing.api.AlchemicalPotionUtil;
import faceless.artent.objects.ModItems;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsage;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class MediumConcentrate extends Item implements INamed {
	public MediumConcentrate(Settings settings) {
		super(settings);
	}

	@Override
	public Text getName(ItemStack stack) {
		var key = stack.getOrCreateNbt().getString("potionKey");
		return Text.translatable("item.concentrate.medium." + key);
	}

	@Override
	public String getId() {
		return "medium_concentrate";
	}

	@Override
	public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
		AlchemicalPotionUtil.createFermentedEffectTooltip(stack, tooltip);
		var amount = stack.getOrCreateNbt().getInt("amount");
		tooltip.add(Text.translatable(amount + "/" + 3));
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
	public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
		PlayerEntity playerEntity = user instanceof PlayerEntity ? (PlayerEntity) user : null;
		return AlchemicalPotionUtil.drinkFermentedPotion(stack, world, playerEntity, ModItems.MediumConcentratePhial);
	}
}