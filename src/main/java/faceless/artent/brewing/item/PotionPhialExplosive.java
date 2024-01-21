package faceless.artent.brewing.item;

import faceless.artent.api.item.INamed;
import faceless.artent.brewing.api.AlchemicalPotionUtil;
import faceless.artent.brewing.entity.ThrowablePotionPhialEntity;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stat.Stats;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class PotionPhialExplosive extends Item implements INamed {
	public PotionPhialExplosive(Settings settings) {
		super(settings);
	}

	@Override
	public String getId() {
		return "potion_phial_explosive";
	}

	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
		ItemStack itemStack = user.getStackInHand(hand);
		if (!world.isClient) {
			ThrowablePotionPhialEntity potionEntity = new ThrowablePotionPhialEntity(world, user);
			potionEntity.setItem(itemStack);
			potionEntity.setVelocity(user, user.getPitch(), user.getYaw(), -20.0f, 0.5f, 1.0f);
			world.spawnEntity(potionEntity);
		}
		user.incrementStat(Stats.USED.getOrCreateStat(this));
		if (!user.getAbilities().creativeMode) {
			itemStack.decrement(1);
		}
		return TypedActionResult.success(itemStack, world.isClient());
	}

	@Override
	public Text getName(ItemStack stack) {
		var key = stack.getOrCreateNbt().getString("potionKey");
		return Text.translatable("item.potion." + key + ".explosive");
	}

	@Override
	public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
		var tickRate = world == null ? 20.0f : world.getTickManager().getTickRate();
		AlchemicalPotionUtil.buildTooltip(stack, tooltip, 1.0f, tickRate);
	}
}