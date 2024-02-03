package faceless.artent.trading.screenHandlers;

import faceless.artent.network.ArtentServerHook;
import faceless.artent.playerData.api.DataUtil;
import faceless.artent.trading.api.IItemStackPriceDeterminator;
import faceless.artent.trading.api.IPriceDeterminatorContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;

import java.util.Optional;

public class TradeOfferSlot extends Slot {
	private final boolean canEdit;
	private final IItemStackPriceDeterminator determinator;
	private final IPriceDeterminatorContext determinatorContext;

	public TradeOfferSlot(
		Inventory inventory,
		int index,
		int x,
		int y,
		boolean canEdit,
		IItemStackPriceDeterminator determinator,
		IPriceDeterminatorContext determinatorContext) {
		super(inventory, index, x, y);
		this.canEdit = canEdit;
		this.determinator = determinator;
		this.determinatorContext = determinatorContext;
	}

	@Override
	public boolean canInsert(ItemStack stack) {
		return canEdit;
	}

	@Override
	public boolean canTakeItems(PlayerEntity player) {
		var pouch = DataUtil.getMoneyPouch(player);
		var item = this.getStack();
		var price = determinator.getBuyPrice(item, player, determinatorContext);

		return pouch.greaterOrEqual(price) || canEdit;
	}

	public boolean canShiftTakeItems(PlayerEntity player) {
		var item = this.getStack();
		if (item.getCount() != 1)
			return canTakeItems(player);

		var pouch = DataUtil.getMoneyPouch(player).asLong();
		var newStack = item.copy();
		var singlePrice = determinator.getBuyPrice(newStack, player, determinatorContext);
		var count = pouch / singlePrice.asLong();

		return count > 0 || canEdit;
	}

	@Override
	public void onTakeItem(PlayerEntity player, ItemStack stack) {
		if (!canEdit && player.getWorld() != null && !player.getWorld().isClient) {
			var handler = DataUtil.getHandler(player);
			var price = determinator.getBuyPrice(stack, player, determinatorContext);
			handler.addMoney(-price.asLong());
			ArtentServerHook.packetSyncPlayerData(player);
		}
		super.onTakeItem(player, stack);
	}

	@Override
	public Optional<ItemStack> tryTakeStackRange(int min, int max, PlayerEntity player) {
		if (!this.canTakeItems(player)) {
			return Optional.empty();
		}
		if (!this.canTakePartial(player) && max < this.getStack().getCount()) {
			return Optional.empty();
		}
		ItemStack itemStack = this.takeStack(min = Math.min(min, max));
		if (itemStack.isEmpty()) {
			return Optional.empty();
		}
		if (this.getStack().isEmpty()) {
			this.setStack(ItemStack.EMPTY, itemStack);
		}
		return Optional.of(itemStack);
	}

	public ItemStack shiftTakeStackRange(int min, int max, PlayerEntity player) {
		var newStack = getStack();
		if (newStack.getCount() != 1)
			return takeStackRange(min, max, player);

		var pouch = DataUtil.getMoneyPouch(player).asLong();
		var singlePrice = determinator.getBuyPrice(newStack, player, determinatorContext);
		var count = pouch / singlePrice.asLong();
		var newMax = count > 256 ? max : Math.min((int) count, max);

		Optional<ItemStack> optional = this.tryShiftTakeStackRange(min, newMax, player);
		optional.ifPresent(stack -> this.onTakeItem(player, stack));
		return optional.orElse(ItemStack.EMPTY);
	}

	public Optional<ItemStack> tryShiftTakeStackRange(int min, int max, PlayerEntity player) {
		if (!this.canShiftTakeItems(player)) {
			return Optional.empty();
		}
		if (!this.canTakePartial(player) && max < this.getStack().getCount()) {
			return Optional.empty();
		}
		ItemStack itemStack = this.takeStack(min = Math.min(min, max));
		if (itemStack.isEmpty()) {
			return Optional.empty();
		}
		if (this.getStack().isEmpty()) {
			this.setStack(ItemStack.EMPTY, itemStack);
		}
		return Optional.of(itemStack);
	}
}
