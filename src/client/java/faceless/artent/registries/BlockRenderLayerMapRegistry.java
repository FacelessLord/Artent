package faceless.artent.registries;

import faceless.artent.objects.ModBlocks;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.client.render.RenderLayer;


public class BlockRenderLayerMapRegistry implements IRegistry{
	@Override
	public void register() {
		// ALCHEMY
		BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.AlchemicalCircle, RenderLayer.getCutoutMipped());
		BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.InscriptionTable, RenderLayer.getCutoutMipped());
		BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.InscriptionTable2, RenderLayer.getCutoutMipped());
		BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.VoidBlock, RenderLayer.getEndPortal());

		// BREWING
		BlockRenderLayerMap.INSTANCE.putBlocks(RenderLayer.getCutout(), ModBlocks.Shroom, ModBlocks.Shadowveil,
		  ModBlocks.berryBush[0], ModBlocks.berryBush[1], ModBlocks.berryBush[2], ModBlocks.berryBush[3],
		  ModBlocks.CrimsonwoodLeaves, ModBlocks.Trader);
	}
}
