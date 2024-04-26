package faceless.artent.registries;

import faceless.artent.brewing.BrewingCauldronRenderer;
import faceless.artent.objects.ModBlockEntities;
import faceless.artent.sharpening.SharpeningAnvilRenderer;
import faceless.artent.spells.VoidBlockRenderer;
import faceless.artent.trasmutations.AlchemicalCircleRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;


public class BlockEntityRenderersRegistry implements IRegistry {
	@Override
	public void register() {
		BlockEntityRendererFactories.register(ModBlockEntities.AlchemicalCircle, AlchemicalCircleRenderer::new);
		BlockEntityRendererFactories.register(ModBlockEntities.SharpeningAnvil, SharpeningAnvilRenderer::new);
		BlockEntityRendererFactories.register(ModBlockEntities.VoidBlock, VoidBlockRenderer::new);
		BlockEntityRendererFactories.register(ModBlockEntities.BrewingCauldron, BrewingCauldronRenderer::new);
	}
}
