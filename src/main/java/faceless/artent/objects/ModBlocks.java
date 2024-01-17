package faceless.artent.objects;

import faceless.artent.sharpening.block.SharpeningAnvil;
import faceless.artent.transmutations.block.AlchemicalCircleBlock;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Blocks;
import net.minecraft.block.MapColor;
import net.minecraft.sound.BlockSoundGroup;

public final class ModBlocks {

	public static final AlchemicalCircleBlock AlchemicalCircle = new AlchemicalCircleBlock(
			FabricBlockSettings.create().notSolid().nonOpaque().breakInstantly().dropsNothing());
	public static SharpeningAnvil SharpeningAnvil = new SharpeningAnvil(FabricBlockSettings.copyOf(Blocks.IRON_BLOCK)
		.mapColor(MapColor.GRAY)
		.nonOpaque()
		.strength(2.0f, 3.0f)
		.sounds(BlockSoundGroup.METAL));


}
