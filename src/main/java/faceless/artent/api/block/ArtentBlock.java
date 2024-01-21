package faceless.artent.api.block;

import faceless.artent.api.item.INamed;
import net.minecraft.block.Block;

public class ArtentBlock extends Block implements INamed {
	public final String Id;

	public ArtentBlock(String blockId, Settings settings) {
		super(settings);
		Id = blockId;
	}

	@Override
	public String getId() {
		return Id;
	}
}
