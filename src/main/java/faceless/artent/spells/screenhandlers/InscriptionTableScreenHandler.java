package faceless.artent.spells.screenhandlers;

import faceless.artent.objects.ModScreenHandlers;
import faceless.artent.spells.api.ISpellInventoryItem;
import faceless.artent.spells.api.ISpellScroll;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;

public class InscriptionTableScreenHandler extends ScreenHandler {

    private final Inventory table;
    private final ScreenHandlerContext context;

    public InscriptionTableScreenHandler(int syncId, PlayerInventory playerInventory) {
        this(syncId, playerInventory, new SimpleInventory(13), ScreenHandlerContext.EMPTY);
    }

    //	slotId - id of the slot in order: [...this.inventorySlots, ...player.slots]
    @Override
    public ItemStack quickMove(PlayerEntity player, int slotId) { // TODO
        Slot slot = this.slots.get(slotId);
        if (slot.hasStack()) {
            ItemStack slotStack = slot.getStack();
            var itemStack = slotStack.copy();
            var item = itemStack.getItem();
            if (slotId < 14) { // output
                if(slotId == 0)
                    table.setStack(0, slotStack);
                if (!this.insertItem(slotStack, 14, 40, true)) {
                    return ItemStack.EMPTY;
                }
                if(slotId == 0) {
                    table.setStack(0, ItemStack.EMPTY);
                }
                slot.onQuickTransfer(slotStack, itemStack);
                table.removeStack(slotId);
                return ItemStack.EMPTY;
            } else if (item instanceof ISpellInventoryItem) {
                if (table.getStack(0).isEmpty()) {
                    slotStack.setCount(0);
                    table.setStack(0, itemStack);
                } else {
                    return ItemStack.EMPTY;
                }
            } else if (item == Items.PAPER) {
                if (!this.insertItem(slotStack, 1, 2, false))
                    return ItemStack.EMPTY;
            } else if (item instanceof ISpellScroll) {
                if (!table.getStack(0).isEmpty() && table.getStack(0).getItem() instanceof ISpellInventoryItem) {
                    if (!this.insertItem(slotStack, 4, 14, false)) {
                        if (!this.insertItem(slotStack, 2, 3, false)) {
                            return ItemStack.EMPTY;
                        }
                    }
                } else if (!this.insertItem(slotStack, 2, 3, false)) {
                    return ItemStack.EMPTY;
                }
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
        this.addSlot(new Slot(this.table, 0, 15 + 9 * 18, 84 + 18 * 2) {
            @Override
            public boolean canInsert(ItemStack stack) {
                return stack.isEmpty() || stack.getItem() instanceof ISpellInventoryItem;
            }
        });
        this.addSlot(new Slot(this.table, 1, 15 + 9 * 18, 80 + 18) {
            @Override
            public boolean canInsert(ItemStack stack) {
                return stack.isEmpty() || stack.getItem() == Items.PAPER;
            }
        });
        this.addSlot(new Slot(this.table, 2, 8, 80 + 18) {
            @Override
            public boolean canInsert(ItemStack stack) {
                return stack.isEmpty() || stack.getItem() instanceof ISpellScroll;
            }
        });
        this.addSlot(new Slot(this.table, 3, 8 + 18 * 2, 80 + 18) {
            @Override
            public boolean canInsert(ItemStack stack) {
                return false;
            }
        });

        // book slots
        for (int j = 0; j < 9; ++j) {
            var k = j;
            this.addSlot(new Slot(this.table, j + 4, 8 + j * 18, 84 + 18 * 2) {
                @Override
                public boolean canInsert(ItemStack stack) {
                    return stack.isEmpty() || stack.getItem() instanceof ISpellScroll;
                }

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