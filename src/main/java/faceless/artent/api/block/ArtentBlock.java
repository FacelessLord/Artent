package faceless.artent.api.block;

import net.minecraft.block.Block;

public class ArtentBlock extends Block {
	public final String Id;

	public ArtentBlock(String blockId, Settings settings) {
		super(settings);
		Id = blockId;
	}
}
