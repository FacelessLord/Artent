package faceless.artent.trading.screenHandlers;

import faceless.artent.Artent;
import faceless.artent.objects.ModScreenHandlers;
import faceless.artent.playerData.api.DataUtil;
import faceless.artent.playerData.api.MoneyPouch;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;

public class TraderScreenHandler extends ScreenHandler {
    public final Inventory traderOffers;
    public final Inventory traderSell;
    public final PlayerEntity player;
    private final ScreenHandlerContext context;

    //This constructor gets called on the client when the server wants it to open the screenHandler,
    //The client will call the other constructor with an empty Inventory and the screenHandler will automatically
    //sync this empty inventory with the inventory on the server.
    public TraderScreenHandler(int syncId, PlayerInventory playerInventory) {
        this(syncId, playerInventory, new SimpleInventory(18), new SimpleInventory(9), ScreenHandlerContext.EMPTY);
    }

    //	slotId - id of the slot in order: [...this.inventorySlots, ...player.slots]
    @Override
    public ItemStack quickMove(PlayerEntity player, int slotId) {
        Slot slot = this.slots.get(slotId);
        if (!slot.hasStack() || slotId < traderOffers.size())
            return ItemStack.EMPTY;
        var slotStack = slot.getStack();
        var itemStack = slotStack.copy();

        if (slotId >= traderOffers.size() + traderSell.size()) {
            if (!this.insertItem(slotStack,
                                 traderOffers.size(),
                                 traderOffers.size() + traderSell.size(),
                                 false)) // try put into sell inventory
                return ItemStack.EMPTY;
        }
        if (slotId >= traderOffers.size() && slotId < traderOffers.size() + traderSell.size()) {
            if (!this.insertItem(slotStack,
                                 traderOffers.size() + traderSell.size(),
                                 slots.size(),
                                 false)) // try put into player inventory
                return ItemStack.EMPTY;
        }

        if (slotStack.isEmpty()) {
            slot.setStack(ItemStack.EMPTY);
        } else {
            slot.markDirty();
        }
        if (slotStack.getCount() == itemStack.getCount()) {
            return ItemStack.EMPTY;
        }
        slot.onTakeItem(player, slotStack);
        return ItemStack.EMPTY;
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return this.context.get((world, pos) -> player.squaredDistanceTo((double) pos.getX() + 0.5,
                                                                         (double) pos.getY() + 0.5,
                                                                         (double) pos.getZ() + 0.5) <= 16, true);
    }

    public TraderScreenHandler(
      int syncId,
      PlayerInventory inv,
      Inventory traderOffers,
      Inventory traderSell,
      ScreenHandlerContext context
    ) {
        super(ModScreenHandlers.TRADER_HANDLER, syncId);
        this.context = context;
        this.player = inv.player;
        var handler = DataUtil.getHandler(player);
        var canEdit = handler.canEditTrades();

        checkSize(traderOffers, 18);
        checkSize(traderSell, 9);
        this.traderOffers = traderOffers;
        this.traderOffers.onOpen(inv.player);

        this.traderSell = traderSell;
        this.traderSell.onOpen(inv.player);

        var tradeInfo = DataUtil.getHandler(player).getTradeInfo();
        var determinator = Artent.ItemPriceDeterminators.determinators.get(tradeInfo.priceDeterminatorType);
        var determinatorContext = tradeInfo.priceDeterminatorContext;

        for (var i = 0; i < 6; ++i) {
            for (int j = 0; j < 3; ++j) {
                var slotIndex = j + i * 3;
                this.addSlot(new TradeOfferSlot(traderOffers,
                                                slotIndex,
                                                8 + j * 18,
                                                -10 + i * 18 + 18,
                                                canEdit,
                                                determinator,
                                                determinatorContext));
            }
        }
        for (var i = 0; i < 3; ++i) {
            for (int j = 0; j < 3; ++j) {
                this.addSlot(new Slot(traderSell, j + i * 3, 116 + j * 18, -10 + i * 18 + 18));
            }
        }

        for (var i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(inv, j + i * 9 + 9, 8 + j * 18, 102 + i * 18 + 18));
            }
        }
        for (var i = 0; i < 9; ++i) {
            this.addSlot(new Slot(inv, i, 8 + i * 18, 142 + 18 + 18));
        }
    }

    public long getSellInventoryPrice() {
        var tradeInfo = DataUtil.getHandler(player).getTradeInfo();
        var determinator = Artent.ItemPriceDeterminators.determinators.get(tradeInfo.priceDeterminatorType);
        var determinatorContext = tradeInfo.priceDeterminatorContext;

        var sum = 0L;
        for (int i = 0; i < traderSell.size(); i++) {
            var stack = traderSell.getStack(i);
            if (!stack.isEmpty())
                sum += determinator.getSellPrice(stack, determinatorContext).asLong();
        }
        return sum;
    }

    @Override
    public void onSlotClick(int slotIndex, int button, SlotActionType actionType, PlayerEntity player) {
        var cursorStack = this.getCursorStack();
        if (actionType == SlotActionType.PICKUP && slotIndex >= 0 && slotIndex < traderOffers.size()) {
            TradeOfferSlot slot = (TradeOfferSlot) this.slots.get(slotIndex);
            var slotStack = slot.getStack();
            if (!cursorStack.isEmpty() && slot.hasStack() && ItemStack.canCombine(cursorStack, slotStack)) {
                ItemStack itemStack7 = slot.takeStackRange(slotStack.getCount(),
                                                           cursorStack.getMaxCount() - cursorStack.getCount(),
                                                           player);
                cursorStack.increment(itemStack7.getCount());
                return;
            }
        }
        if (actionType == SlotActionType.QUICK_MOVE && slotIndex >= 0 && slotIndex < traderOffers.size()) {
            TradeOfferSlot slot = (TradeOfferSlot) this.slots.get(slotIndex);
            var slotStack = slot.getStack();
            if (cursorStack.isEmpty() && slot.hasStack()) {
                ItemStack itemStack7 = slot.shiftTakeStackRange(slotStack.getMaxCount(),
                                                                cursorStack.getMaxCount() - cursorStack.getCount(),
                                                                player);
                setCursorStack(itemStack7);
                return;
            }
            if (!cursorStack.isEmpty() && slot.hasStack() && ItemStack.canCombine(cursorStack, slotStack)) {
                ItemStack itemStack7 = slot.shiftTakeStackRange(slotStack.getMaxCount(),
                                                                cursorStack.getMaxCount() - cursorStack.getCount(),
                                                                player);
                cursorStack.increment(itemStack7.getCount());
                return;
            }
        }

        if (actionType != SlotActionType.PICKUP_ALL) {
            super.onSlotClick(slotIndex, button, actionType, player);
            return;
        }
        if (slotIndex < 0)
            return;

        Slot slot = this.slots.get(slotIndex);

        if (cursorStack.isEmpty() || slot.hasStack() && slot.canTakeItems(player))
            return;

        // button defines order on which items will be collected
        int k = button == 0 ? traderOffers.size() : this.slots.size() - 1;
        int p = button == 0 ? 1 : -1;

        for (int o = 0; o < 2; ++o) {
            // traderOffers.size() - because it's slots are first and we need to skip them when collecting stacks
            for (int q = k; q >= traderOffers.size() &&
                            q < this.slots.size() &&
                            cursorStack.getCount() < cursorStack.getMaxCount(); q += p) {
                Slot slot4 = this.slots.get(q);
                if (!slot4.hasStack() ||
                    !ScreenHandler.canInsertItemIntoSlot(slot4, cursorStack, true) ||
                    !slot4.canTakeItems(player) ||
                    !this.canInsertIntoSlot(cursorStack, slot4))
                    continue;
                ItemStack itemStack6 = slot4.getStack();
                if (o == 0 && itemStack6.getCount() == itemStack6.getMaxCount()) continue;
                ItemStack itemStack7 = slot4.takeStackRange(itemStack6.getCount(),
                                                            cursorStack.getMaxCount() - cursorStack.getCount(),
                                                            player);
                cursorStack.increment(itemStack7.getCount());
            }
        }
    }

    public MoneyPouch getStackSellPrice(ItemStack stack) {
        var tradeInfo = DataUtil.getHandler(player).getTradeInfo();
        var determinator = Artent.ItemPriceDeterminators.determinators.get(tradeInfo.priceDeterminatorType);
        var determinatorContext = tradeInfo.priceDeterminatorContext;

        return determinator.getSellPrice(stack, determinatorContext);
    }

    public MoneyPouch getStackBuyPrice(ItemStack stack) {
        var tradeInfo = DataUtil.getHandler(player).getTradeInfo();
        var determinator = Artent.ItemPriceDeterminators.determinators.get(tradeInfo.priceDeterminatorType);
        var determinatorContext = tradeInfo.priceDeterminatorContext;

        return determinator.getBuyPrice(stack, player, determinatorContext);
    }

    @Override
    public void onClosed(PlayerEntity player) {
        super.onClosed(player);
        var handler = DataUtil.getHandler(player);
        handler.setTradeInfo(null);
    }
}