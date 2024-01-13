package faceless.artent.item;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.Item;

public class ArtentItem extends Item {
	public final String Id;

	public ArtentItem(String itemId, Settings settings) {
		super(settings);
		Id = itemId;
	}

	public ArtentItem(String itemId) {
		super(new FabricItemSettings());
		Id = itemId;
	}

}
