package faceless.artent.objects;

import faceless.artent.transmutations.world.AlchemicalCircleBlock;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;

public final class ModBlocks {

	public static final AlchemicalCircleBlock alchemicalCircle = new AlchemicalCircleBlock(
			FabricBlockSettings.create().notSolid().nonOpaque().breakInstantly().dropsNothing());
}
