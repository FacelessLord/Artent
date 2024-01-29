package faceless.artent.trading.inventory;

import faceless.artent.api.inventory.ArtentInventory;
import faceless.artent.trading.blockEntities.TraderBlockEntity;

public class TraderOfferInventory extends ArtentInventory {
	private final TraderBlockEntity trader;

	public TraderOfferInventory(TraderBlockEntity trader) {
		this.trader = trader;
	}

	@Override
	public int size() {
		return 18;
	}

	@Override
	public void markDirty() {
		trader.markDirty();
	}

	@Override
	public boolean canTakeStackFromSlot(@SuppressWarnings("unused") int slot) {
		return true;
	}

	@Override
	public void clear() {

	}
}
