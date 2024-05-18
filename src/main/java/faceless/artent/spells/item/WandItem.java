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

        var spell = ((ICaster) player).getCurrentSpell();
        if (spell == null) return TypedActionResult.fail(stack);

        if (Math.random() < spell.getRecoilChance(player, world)) {
            spell.onRecoil(DataUtil.asCaster(player), world, stack, getMaxUseTime(stack));
            return TypedActionResult.fail(stack);
        }

        if (spell.isSingleCastAction() || spell.isTickAction()) {
            player.setCurrentHand(hand);
            return TypedActionResult.consume(stack);
        }
        return TypedActionResult.success(stack);
    }

    public static Spell getSelectedSpell(PlayerEntity player, Hand hand) {
        var book = player.getStackInHand(getOppositeHand(hand));
        if (book.isEmpty() || !(book.getItem() instanceof ISpellInventoryItem)) return null;

        var bookInventory = new ItemSpellInventory(book);

        var casterInfo = DataUtil.getCasterInfo(player);
        var spellIndex = casterInfo.getSpellBookIndex() % bookInventory.getSize();
        var scrollStack = bookInventory.getSpell(spellIndex);
        if (scrollStack == null) return null;

        return scrollStack.spell;
    }


    @Override
    public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        if (!(user instanceof ICaster caster)) return;

        var spell = caster.getCurrentSpell();
        if (spell == null) return;
        if (!spell.isSingleCastAction()) return;

        var actionTime = getMaxUseTime(stack) - remainingUseTicks; // TODO minimal spellcast time
        if (world.getRandom().nextFloat() < spell.getRecoilChance(user, world)) {
            spell.onRecoil(caster, world, stack, actionTime);
        } else {
            var manaToConsume = ManaUtils.evaluateManaToConsume(spell, this.affinities, Spell.ActionType.SingleCast);
            if (caster.consumeMana(manaToConsume)) spell.action(caster, world, stack, actionTime);
        }
    }

    public void usageTick(World world, LivingEntity living, ItemStack stack, int remainingUseTicks) {
        if (!(living instanceof ICaster caster)) return;
        var spell = caster.getCurrentSpell();
        if (spell == null) return;

        var actionTime = getMaxUseTime(stack) - remainingUseTicks;

        if (actionTime < spell.prepareTime) {
            var manaToConsume = ManaUtils.evaluatePrepareManaToConsume(spell, this.affinities, spell.type);
            if (caster.consumeMana(manaToConsume)) {
                spell.prepareTick(caster, world, stack, actionTime);
                return;
            } else {
                spell.onRecoil(caster, world, stack, actionTime);
            }
            living.stopUsingItem();
        }

        actionTime -= spell.prepareTime;

        if (!spell.isTickAction()) return;

        var manaToConsume = ManaUtils.evaluateManaToConsume(spell, this.affinities, Spell.ActionType.Tick);
        SpellActionResult result = null;

        if (caster.consumeMana(manaToConsume)) result = spell.spellTick(caster, world, stack, actionTime);
        if (result == null ||
            result.type == SpellActionResultType.Stop ||
            result.type == SpellActionResultType.Recoil) {
            living.stopUsingItem();
        }
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        return ActionResult.PASS;
//        if (context.shouldCancelInteraction())
//            return ActionResult.FAIL;
//
//        var player = context.getPlayer();
//
//        if (player == null || player.getWorld() == null)
//            return ActionResult.FAIL;
//
//        var stack = context.getStack();
//        var side = context.getSide();
//        var blockPos = context.getBlockPos();
//        var spell = getSelectedSpell(player, context.getHand());
//        if (spell == null)
//            return ActionResult.FAIL;
//
//        if (!spell.isBlockCastAction()) {
//            return ActionResult.PASS;
//        } // TODO recoil
//
//        var caster = DataUtil.asCaster(player);
//        var manaToConsume = ManaUtils.evaluateManaToConsume(spell, this.affinities, Spell.ActionType.SingleCast);
//        if (caster.consumeMana(manaToConsume)) {
//            spell.blockCast(caster, player.getWorld(), stack, blockPos, side, 0);
//            return ActionResult.SUCCESS;
//        }
//        return ActionResult.FAIL;
    }
}
