package faceless.artent.spells.item;

import faceless.artent.api.item.ArtentItem;
import faceless.artent.playerData.api.DataUtil;
import faceless.artent.sharpening.api.ISharpenable;
import faceless.artent.spells.api.*;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class WandItem extends ArtentItem implements ISharpenable {
	public List<Affinity> affinities = new ArrayList<>();

	public WandItem(Settings settings, String itemId) {
		super(settings, itemId);
	}

	public int getMaxUseTime(ItemStack stack) {
		return 72000;
	}

	public UseAction getUseAction(ItemStack stack) {
		return UseAction.BOW;
	}

	public static Hand getOppositeHand(Hand hand) {
		return switch (hand) {
			case MAIN_HAND -> Hand.OFF_HAND;
			case OFF_HAND -> Hand.MAIN_HAND;
		};
	}

	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
		var stack = player.getStackInHand(hand);
		var spell = getActiveEntitySpell(player);
		if (spell == null)
			return TypedActionResult.fail(stack);

		if (world.random.nextFloat() < spell.getRecoilChance(player, world)) {
			spell.onRecoil(DataUtil.asCaster(player), world, stack, getMaxUseTime(stack));
			return TypedActionResult.fail(stack);
		}

		if ((spell.type & (Spell.ActionType.SingleCast | Spell.ActionType.Tick)) > 0) {
			player.setCurrentHand(hand);
			return TypedActionResult.consume(stack);
		}
		return TypedActionResult.success(stack);
	}

	public static Spell getSelectedSpell(PlayerEntity player, Hand hand) {
		var book = player.getStackInHand(getOppositeHand(hand));
		if (book.isEmpty() || !(book.getItem() instanceof ISpellInventoryItem))
			return null;

		var bookInventory = new ItemSpellInventory(book);

		var casterInfo = DataUtil.getCasterInfo(player);
		var spellIndex = casterInfo.getSpellBookIndex() % bookInventory.getSize();
		var scrollStack = bookInventory.getSpell(spellIndex);
		if (scrollStack == null)
			return null;

		return scrollStack.spell;
	}


	@Override
	public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
		if (!(user instanceof ICaster caster)) return;

		var spell = getActiveEntitySpell(user);
		if (spell == null) return;
		if ((spell.type & Spell.ActionType.SingleCast) == 0)
			return;

		var actionTime = getMaxUseTime(stack) - remainingUseTicks; // TODO minimal spellcast time
		if (world.getRandom().nextFloat() < spell.getRecoilChance(user, world)) {
			spell.onRecoil(caster, world, stack, actionTime);
		} else {
			var manaToConsume = ManaUtils.evaluateManaToConsume(spell, this.affinities, Spell.ActionType.SingleCast);
			if (caster.consumeMana(manaToConsume))
				spell.action(caster, user.getWorld(), stack, actionTime);
		}
	}

	public void usageTick(World world, LivingEntity living, ItemStack stack, int remainingUseTicks) {
		var spell = getActiveEntitySpell(living);
		if (spell == null) return;

		if (living instanceof PlayerEntity player) {
			var actionTime = getMaxUseTime(stack) - remainingUseTicks;
			var caster = DataUtil.asCaster(player);

			if ((spell.type & Spell.ActionType.Tick) == 0)
				return;

			var manaToConsume = ManaUtils.evaluateManaToConsume(spell, this.affinities, Spell.ActionType.SingleCast);
			SpellActionResult result = null;

			if (caster.consumeMana(manaToConsume))
				result = spell.spellTick(caster,
				  world,
				  stack,
				  actionTime);
			if (result == null) {
				player.stopUsingItem();
				return;
			}
			if (result.type == SpellActionResultType.Stop || result.type == SpellActionResultType.Recoil) {
				player.clearActiveItem();
			}
			if (result.type == SpellActionResultType.Recoil) {
				spell.onRecoil(caster, world, stack, actionTime);
			}
		}
	}

	@Nullable
	public static Spell getActiveEntitySpell(LivingEntity user) {
		if (user instanceof PlayerEntity player) {
			var mainHand = getSelectedSpell(player, Hand.MAIN_HAND);
			var offHand = getSelectedSpell(player, Hand.OFF_HAND);
			if (mainHand == null && offHand == null)
				return null;
			if (mainHand != null)
				return mainHand;
			return offHand;
		}
		return null;
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
		var spell = getSelectedSpell(player, context.getHand());
		if (spell == null)
			return ActionResult.FAIL;

		if ((spell.type & Spell.ActionType.BlockCast) == 0) {
			return ActionResult.PASS;
		} // TODO recoil

		var caster = DataUtil.asCaster(player);
		var manaToConsume = ManaUtils.evaluateManaToConsume(spell, this.affinities, Spell.ActionType.SingleCast);
		if (caster.consumeMana(manaToConsume)) {
			spell.blockCast(caster, player.getWorld(), stack, blockPos, side, 0);
			return ActionResult.SUCCESS;
		}
		return ActionResult.FAIL;
	}
}
