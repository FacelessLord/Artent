package faceless.artent;

import faceless.artent.objects.ModBlockEntities;
import faceless.artent.objects.ModBlocks;
import faceless.artent.registries.ScreenRegistry;
import faceless.artent.trasmutations.AlchemicalCircleRenderer;
import faceless.artent.trasmutations.network.AlchemicalCircleClientHook;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;

public class ArtentClient implements ClientModInitializer {
	public ScreenRegistry Screens = new ScreenRegistry();
	public AlchemicalCircleClientHook ClientHook = new AlchemicalCircleClientHook();
	
	@Override
	public void onInitializeClient() {
		Screens.register();
		ClientHook.loadClient();
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.alchemicalCircle, RenderLayer.getCutoutMipped());
		BlockEntityRendererFactories.register(ModBlockEntities.alchemicalCircleEntity, AlchemicalCircleRenderer::new);
	}
}