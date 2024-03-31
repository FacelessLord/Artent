package faceless.artent;

import faceless.artent.api.math.Color;
import faceless.artent.brewing.BrewingCauldronRenderer;
import faceless.artent.brewing.api.AlchemicalPotionUtil;
import faceless.artent.brewing.blockEntities.BrewingCauldronBlockEntity;
import faceless.artent.mobs.CrowEntityRenderer;
import faceless.artent.network.ArtentClientHook;
import faceless.artent.objects.ModBlockEntities;
import faceless.artent.objects.ModBlocks;
import faceless.artent.objects.ModEntities;
import faceless.artent.objects.ModItems;
import faceless.artent.registries.ScreenRegistry;
import faceless.artent.sharpening.SharpeningAnvilRenderer;
import faceless.artent.spells.LightbulbRenderer;
import faceless.artent.trading.CoinEntityRenderer;
import faceless.artent.trasmutations.AlchemicalCircleRenderer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;
import net.minecraft.client.render.entity.FlyingItemEntityRenderer;
import net.minecraft.util.Identifier;

public class ArtentClient implements ClientModInitializer {
	public ScreenRegistry Screens = new ScreenRegistry();
	public ArtentClientHook ClientHook = new ArtentClientHook();

	@Override
	public void onInitializeClient() {
		Screens.register();
		ClientHook.loadClient();

		// ALCHEMY
		BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.AlchemicalCircle, RenderLayer.getCutoutMipped());
		BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.InscriptionTable, RenderLayer.getCutoutMipped());
		BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.InscriptionTable2, RenderLayer.getCutoutMipped());
		ColorProviderRegistry.BLOCK.register((state, view, pos, tintIndex) -> {
			if (view == null || view.getBlockEntityRenderData(pos) == null)
				return new Color().asInt();
			//noinspection DataFlowIssue
			return (int) view.getBlockEntityRenderData(pos);
		}, ModBlocks.AlchemicalCircle);
//		BuiltinItemRendererRegistry.INSTANCE.register(ModItems.alchemicalPaper, new AlchemicalPaperRenderer()); TODO
		BlockEntityRendererFactories.register(ModBlockEntities.AlchemicalCircle, AlchemicalCircleRenderer::new);
		BlockEntityRendererFactories.register(ModBlockEntities.SharpeningAnvil, SharpeningAnvilRenderer::new);

		// BREWING
		BlockEntityRendererFactories.register(ModBlockEntities.BrewingCauldron, BrewingCauldronRenderer::new);
		BlockRenderLayerMap.INSTANCE.putBlocks(RenderLayer.getCutout(), ModBlocks.Shroom, ModBlocks.Shadowveil,
			ModBlocks.berryBush[0], ModBlocks.berryBush[1], ModBlocks.berryBush[2], ModBlocks.berryBush[3],
			ModBlocks.CrimsonwoodLeaves, ModBlocks.Trader);
		ColorProviderRegistry.BLOCK.register((state, view, pos, tintIndex) -> {
			if (view == null)
				return 0;
			var be = view.getBlockEntity(pos);
			if (be instanceof BrewingCauldronBlockEntity cauldron) {
				return cauldron.color.toHex();
			}
			return 0;
		}, ModBlocks.CauldronFluid);
		ColorProviderRegistry.ITEM.register((stack, tintIndex) -> {
				if (tintIndex == 0) return -1;
				return AlchemicalPotionUtil.getColor(stack);
			}, ModItems.PotionPhial, ModItems.PotionPhialExplosive, ModItems.GoldenBucketFilled,
			ModItems.SmallConcentrate, ModItems.MediumConcentrate, ModItems.BigConcentrate);
		EntityRendererRegistry.register(ModEntities.POTION_PHIAL, FlyingItemEntityRenderer::new);
		EntityRendererRegistry.register(ModEntities.COIN_ENTITY, CoinEntityRenderer::new);
		EntityRendererRegistry.register(ModEntities.CROW_ENTITY, CrowEntityRenderer::new);
		EntityRendererRegistry.register(ModEntities.LIGHTBULB, LightbulbRenderer::new);

		ModelPredicateProviderRegistry.register(ModItems.MediumConcentrate, new Identifier("amount"),
			(stack, world, entity, seed) -> stack.getOrCreateNbt().getInt("amount") / 4.0f);
		ModelPredicateProviderRegistry.register(ModItems.BigConcentrate, new Identifier("amount"),
			(stack, world, entity, seed) -> stack.getOrCreateNbt().getInt("amount") / 10.0f);

	}
}