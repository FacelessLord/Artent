package faceless.artent.trading.inventory;

import faceless.artent.api.inventory.ArtentInventory;
import net.minecraft.entity.player.PlayerEntity;

public class TraderSellInventory extends ArtentInventory {
	private final PlayerEntity player;

	public TraderSellInventory(PlayerEntity player) {
		super();
		this.player = player;
	}

	@Override
	public int size() {
		return 9;
	}

	@Override
	public boolean canTakeStackFromSlot(@SuppressWarnings("unused") int slot) {
		return true;
	}
}
