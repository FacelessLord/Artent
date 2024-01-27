package faceless.artent.trading;

import faceless.artent.api.inventory.ArtentInventory;
import faceless.artent.trading.blockEntities.TraderBlockEntity;

public class TraderInventory extends ArtentInventory {
	private final TraderBlockEntity trader;

	public TraderInventory(TraderBlockEntity trader) {
		this.trader = trader;
	}

	@Override
	public int size() {
		return 18 + 9;
	}

	@Override
	public void markDirty() {
		trader.markDirty();
	}

	@Override
	public void clear() {

	}
}
