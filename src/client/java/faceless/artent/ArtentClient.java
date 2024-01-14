package faceless.artent;

import faceless.artent.api.Color;
import faceless.artent.objects.ModBlockEntities;
import faceless.artent.objects.ModBlocks;
import faceless.artent.objects.ModItems;
import faceless.artent.registries.ScreenRegistry;
import faceless.artent.trasmutations.AlchemicalCircleRenderer;
import faceless.artent.trasmutations.network.AlchemicalCircleClientHook;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
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
		ColorProviderRegistry.BLOCK.register((state, view, pos, tintIndex) -> {
			if (view == null || view.getBlockEntityRenderData(pos) == null)
				return new Color().asInt();
			//noinspection DataFlowIssue
			return ((Color) view.getBlockEntityRenderData(pos)).asInt();
		}, ModBlocks.alchemicalCircle);
//		BuiltinItemRendererRegistry.INSTANCE.register(ModItems.alchemicalPaper, new AlchemicalPaperRenderer());
		BlockEntityRendererFactories.register(ModBlockEntities.alchemicalCircleEntity, AlchemicalCircleRenderer::new);
	}
}