package faceless.artent.api.item;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.Item;

public class ArtentItem extends Item implements INamed {
	public final String Id;

	public ArtentItem(String itemId) {
		super(new FabricItemSettings());
		Id = itemId;
	}

	public ArtentItem(Settings settings, String itemId) {
		super(settings);
		Id = itemId;
	}

	@Override
	public String getId() {
		return Id;
	}
}
