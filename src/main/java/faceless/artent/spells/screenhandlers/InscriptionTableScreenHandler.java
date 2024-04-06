package faceless.artent.spells.screenhandlers;

import faceless.artent.objects.ModItems;
import faceless.artent.objects.ModScreenHandlers;
import faceless.artent.sharpening.api.IEnhancer;
import faceless.artent.sharpening.item.SmithingHammer;
import faceless.artent.spells.api.ISpellInventoryItem;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolItem;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class InscriptionTableScreenHandler extends ScreenHandler {

    private final Inventory table;
    private final ScreenHandlerContext context;

    private List<Slot> bookSlots = new ArrayList<>(9);

    //This constructor gets called on the client when the server wants it to open the screenHandler,
    //The client will call the other constructor with an empty Inventory and the screenHandler will automatically
    //sync this empty inventory with the inventory on the server.
    public InscriptionTableScreenHandler(int syncId, PlayerInventory playerInventory) {
        this(syncId, playerInventory, new SimpleInventory(13), ScreenHandlerContext.EMPTY);
    }

    protected boolean canTakeOutput(PlayerEntity player, boolean present) {
        if (!present)
            return false;
        if (player.getAbilities().creativeMode)
            return true;
//		var modifier = anvil.getStack(1);
//		var hammer = anvil.getStack(2);
//		var stack = anvil.getStack(3);
//		return !hammer.isEmpty() && hammer.getItem() == ModBlocks.SharpeningAnvil.Item
//				   && !stack.isEmpty() && stack.getItem() instanceof ISharpenable sharpenable
//				   && (isCatalyst(modifier.getItem()) && modifier.getCount() <= SharpeningAnvilInventory.getCatalystCount(sharpenable.getLevel(stack)) ||
//						   modifier.getItem() instanceof IEnhancer && modifier.getCount() > 0);
        return true;
    }

    protected void onTakeOutput(PlayerEntity player, ItemStack stack) {
//		if (!stack.isEmpty() && stack.getItem() instanceof ISharpenable sharpenable) {
//			var level = sharpenable.getLevel(stack);
//			this.anvil.getStack(0).decrement(1);
//			var modifier = this.anvil.getStack(1);
//			var modifierItem = modifier.getItem();
//			if (isCatalyst(modifierItem)) {
//				this.anvil.getStack(1).decrement(SharpeningAnvilInventory.getCatalystCount(level));
//			} else if (modifierItem instanceof IEnhancer) {
//				this.anvil.getStack(1).decrement(1);
//			}
//			var hammer = anvil.getStack(2);
//			hammer.damage(4, player, p -> {
//			});
//		}
    }

    //	slotId - id of the slot in order: [...this.inventorySlots, ...player.slots]
    @Override
    public ItemStack quickMove(PlayerEntity player, int slotId) {
        Slot slot = this.slots.get(slotId);
        if (slot.hasStack()) {
            ItemStack slotStack = slot.getStack();
            var itemStack = slotStack.copy();
            var item = itemStack.getItem();
            if (slotId == 3) { // output
                if (!this.insertItem(slotStack, 4, 40, true)) {
                    return ItemStack.EMPTY;
                }
                slot.onQuickTransfer(slotStack, itemStack);
                table.removeStack(slotId);
                onTakeOutput(player, itemStack);
            } else if (slotId >= 0 && slotId <= 3 // shift-click on slots for tool, hammer, catalyst and result
                    ? !this.insertItem(slotStack, 4, 40, false) // try put into player inventory
                    : (item instanceof SmithingHammer
                    // Insert hammer into hammer slot. Otherwise, try to insert it as tool
                    ? !this.insertItem(slotStack, 2, 3, false) || !this.insertItem(slotStack, 0, 1, false)
                    : (item instanceof ToolItem // insert tool into slot 0
                    ? !this.insertItem(slotStack, 0, 1, false)
                    : (item instanceof IEnhancer || Arrays.stream(ModItems.Catalysts).anyMatch(cat -> cat == item)
                    // insert catalyst or enchancer into slot 1
                    ? !this.insertItem(slotStack, 1, 2, false)
                    : (slotId >= 4 && slotId < 31
                    ? !this.insertItem(slotStack, 31, 40, false)
                    : slotId >= 31 && slotId < 40 && !this.insertItem(slotStack, 3, 31, false)
            ))))) {
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
        }
        return ItemStack.EMPTY;
    }

    @Override
    public void onSlotClick(int slotIndex, int button, SlotActionType actionType, PlayerEntity player) {
        super.onSlotClick(slotIndex, button, actionType, player);
    }

    @Override
    public boolean canInsertIntoSlot(Slot slot) {
        return super.canInsertIntoSlot(slot);
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return this.context.get((world, pos) -> player.squaredDistanceTo((double) pos.getX() + 0.5, (double) pos.getY() + 0.5, (double) pos.getZ() + 0.5) <= 16, true);
    }

    public InscriptionTableScreenHandler(int syncId, PlayerInventory inv, Inventory table, ScreenHandlerContext context) {
        super(ModScreenHandlers.INSCRIPTION_TABLE, syncId);
        this.context = context;
        checkSize(table, 3);
        this.table = table;
        table.onOpen(inv.player);
        this.addSlot(new Slot(this.table, 0, 15 + 9 * 18, 84 + 18 * 2));
        this.addSlot(new Slot(this.table, 1, 15 + 9 * 18, 80 + 18));
        this.addSlot(new Slot(this.table, 2, 8, 80 + 18));
        this.addSlot(new Slot(this.table, 3, 8 + 18 * 2, 80 + 18));

        // book slots
        for (int j = 0; j < 9; ++j) {
            var k = j;
            this.addSlot(new Slot(this.table, j + 4, 8 + j * 18, 84 + 18 * 2) {
                @Override
                public boolean isEnabled() {
                    var bookStack = table.getStack(0);
                    return !bookStack.isEmpty() && bookStack.getItem() instanceof ISpellInventoryItem book && k < book.getSize(bookStack);
                }
            });
        }

        for (var i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(inv, j + i * 9 + 9, 8 + j * 18, 88 + 18 * 3 + i * 18));
            }
        }
        for (var i = 0; i < 9; ++i) {
            this.addSlot(new Slot(inv, i, i * 18 + 8, 146 + 18 * 3));
        }
    }
}