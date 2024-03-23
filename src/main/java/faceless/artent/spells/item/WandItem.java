package faceless.artent.spells.item;

import faceless.artent.api.item.ArtentItem;
import faceless.artent.playerData.api.DataUtil;
import faceless.artent.spells.api.Spell;
import faceless.artent.spells.api.SpellActionResultType;
import faceless.artent.spells.spells.MakeLight;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.world.World;

public class WandItem extends ArtentItem {

	public WandItem(Settings settings, String itemId) {
		super(settings, itemId);
	}

	public int getMaxUseTime(ItemStack stack) {
		return 72000;
	}

	public UseAction getUseAction(ItemStack stack) {
		return UseAction.BOW;
	}

	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
		var stack = player.getStackInHand(hand);
		var spell = new MakeLight();
		if (world.random.nextFloat() < spell.getRecoilChance(player, world)) {
			spell.onRecoil(DataUtil.asCaster(player), world, stack, getMaxUseTime(stack));
			return TypedActionResult.fail(stack);
		}

		if ((spell.type & Spell.ActionType.SingleCast) > 0) {
			spell.action(DataUtil.asCaster(player), player.getWorld(), stack, 0);
			player.setCurrentHand(hand);
			return TypedActionResult.consume(stack);
		}
		return TypedActionResult.success(stack);
	}

	public void usageTick(World world, LivingEntity living, ItemStack stack, int remainingUseTicks) {
		if (living instanceof PlayerEntity player) {
			var spell = new MakeLight();
			var actionTime = getMaxUseTime(stack) - remainingUseTicks;
			var caster = DataUtil.asCaster(player);

			if ((spell.type & Spell.ActionType.Tick) > 0) {
				var result = spell.spellTick(caster,
					player.getWorld(),
					stack,
					actionTime);
				if (result.type == SpellActionResultType.Stop || result.type == SpellActionResultType.Recoil) {
					player.clearActiveItem();
				}
				if (result.manaToConsume > 0) {
					caster.consumeMana(result.manaToConsume);
				}
				if (result.type == SpellActionResultType.Recoil) {
					spell.onRecoil(caster, world, stack, actionTime);
				}
			}
		}
	}

	@Override
	public ActionResult useOnBlock(ItemUsageContext context) {
		if (context.shouldCancelInteraction())
			return ActionResult.FAIL;

		var player = context.getPlayer();

		if (player == null || player.getWorld() == null)
			return ActionResult.FAIL;

		var stack = context.getStack();
		var side = context.getSide();
		var blockPos = context.getBlockPos();
		var spell = new MakeLight();

		if ((spell.type & Spell.ActionType.BlockCast) > 0) {
			spell.blockCast(DataUtil.asCaster(player), player.getWorld(), stack, blockPos, side, 0);
			return ActionResult.SUCCESS;
		}
		return ActionResult.PASS;
	}
}
