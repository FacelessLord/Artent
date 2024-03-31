package faceless.artent.sharpening.screenHandlers;

import faceless.artent.objects.ModBlocks;
import faceless.artent.objects.ModItems;
import faceless.artent.objects.ModScreenHandlers;
import faceless.artent.sharpening.api.IEnhancer;
import faceless.artent.sharpening.api.ISharpenable;
import faceless.artent.sharpening.inventory.SharpeningAnvilInventory;
import faceless.artent.sharpening.item.SmithingHammer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolItem;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.slot.Slot;

import java.util.Arrays;

public class SharpeningAnvilScreenHandler extends ScreenHandler {
	private final Inventory anvil;
	private final ScreenHandlerContext context;

	//This constructor gets called on the client when the server wants it to open the screenHandler,
	//The client will call the other constructor with an empty Inventory and the screenHandler will automatically
	//sync this empty inventory with the inventory on the server.
	public SharpeningAnvilScreenHandler(int syncId, PlayerInventory playerInventory) {
		this(syncId, playerInventory, new SimpleInventory(4), ScreenHandlerContext.EMPTY);
	}

	protected boolean canTakeOutput(PlayerEntity player, boolean present) {
		if (!present)
			return false;
		if (player.getAbilities().creativeMode)
			return true;
		var modifier = anvil.getStack(1);
		var hammer = anvil.getStack(2);
		var stack = anvil.getStack(3);
		return !hammer.isEmpty() && hammer.getItem() == ModBlocks.SharpeningAnvil.Item
				   && !stack.isEmpty() && stack.getItem() instanceof ISharpenable sharpenable
				   && (isCatalyst(modifier.getItem()) && modifier.getCount() <= SharpeningAnvilInventory.getCatalystCount(sharpenable.getLevel(stack)) ||
						   modifier.getItem() instanceof IEnhancer && modifier.getCount() > 0);
	}

	protected void onTakeOutput(PlayerEntity player, ItemStack stack) {
		if (!stack.isEmpty() && stack.getItem() instanceof ISharpenable sharpenable) {
			var level = sharpenable.getLevel(stack);
			this.anvil.getStack(0).decrement(1);
			var modifier = this.anvil.getStack(1);
			var modifierItem = modifier.getItem();
			if (isCatalyst(modifierItem)) {
				this.anvil.getStack(1).decrement(SharpeningAnvilInventory.getCatalystCount(level));
			} else if (modifierItem instanceof IEnhancer) {
				this.anvil.getStack(1).decrement(1);
			}
			var hammer = anvil.getStack(2);
			hammer.damage(4, player, p -> {
			});
		}
	}

	private boolean isCatalyst(Item modifierItem) {
		return modifierItem == ModItems.StoneOfTheSea || modifierItem == ModItems.FortitudeSpiritStone || modifierItem == ModItems.AmberSphere;
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
				anvil.removeStack(slotId);
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
	public boolean canUse(PlayerEntity player) {
		return this.context.get((world, pos) -> player.squaredDistanceTo((double) pos.getX() + 0.5, (double) pos.getY() + 0.5, (double) pos.getZ() + 0.5) <= 16, true);
	}

	public SharpeningAnvilScreenHandler(int syncId, PlayerInventory inv, Inventory anvil, ScreenHandlerContext context) {
		super(ModScreenHandlers.SHARPENING_ANVIL, syncId);
		this.context = context;
		checkSize(anvil, 4);
		this.anvil = anvil;
		anvil.onOpen(inv.player);
		this.addSlot(new Slot(this.anvil, 0, 27, 47) {
			@Override
			public boolean canInsert(ItemStack stack) {
				return stack.getItem() instanceof ToolItem;
			}
		});
		this.addSlot(new Slot(this.anvil, 1, 76, 47) {
			@Override
			public boolean canInsert(ItemStack stack) {
				return stack.getItem() instanceof IEnhancer || Arrays.stream(ModItems.Catalysts).anyMatch(cat -> cat == stack.getItem());
			}
		});
		this.addSlot(new Slot(this.anvil, 2, 52, 16) {
			@Override
			public boolean canInsert(ItemStack stack) {
				return stack.getItem() instanceof SmithingHammer;
			}
		});
		this.addSlot(new Slot(this.anvil, 3, 134, 47) {

			@Override
			public boolean canInsert(ItemStack stack) {
				return false;
			}

			@Override
			public boolean canTakeItems(PlayerEntity playerEntity) {
				return canTakeOutput(playerEntity, this.hasStack());
			}

			@Override
			public void onTakeItem(PlayerEntity player, ItemStack stack) {
				super.onTakeItem(player, stack);
				onTakeOutput(player, stack);
			}
		});
		for (var i = 0; i < 3; ++i) {
			for (int j = 0; j < 9; ++j) {
				this.addSlot(new Slot(inv, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
			}
		}
		for (var i = 0; i < 9; ++i) {
			this.addSlot(new Slot(inv, i, 8 + i * 18, 142));
		}
	}
}