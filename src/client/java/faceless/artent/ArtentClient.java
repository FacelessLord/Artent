package faceless.artent;

import faceless.artent.keybindings.ModKeyBindings;
import faceless.artent.network.ArtentClientHook;
import faceless.artent.objects.ModItems;
import faceless.artent.registries.*;
import faceless.artent.spells.SprayParticleEntityModel;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.util.Identifier;

public class ArtentClient implements ClientModInitializer {
	public ScreenRegistry Screens = new ScreenRegistry();
	public EntityRenderersRegistry EntityRenderers = new EntityRenderersRegistry();
	public BlockEntityRenderersRegistry BlockEntityRenderers = new BlockEntityRenderersRegistry();
	public BlockRenderLayerMapRegistry BlockRenderLayerMaps = new BlockRenderLayerMapRegistry();
	public ColorProvidersRegistry ColorProviders = new ColorProvidersRegistry();
	public ParticleClientRegistry Particles = new ParticleClientRegistry();
	public ModKeyBindings keyBindings = new ModKeyBindings();
	public ArtentClientHook ClientHook = new ArtentClientHook();

	public static final EntityModelLayer SPRAY_PARTICLE_LAYER = new EntityModelLayer(new Identifier(Artent.MODID, "spray_particle"), "main");

	@Override
	public void onInitializeClient() {
		Screens.register();
		keyBindings.register();
		EntityRenderers.register();
		BlockEntityRenderers.register();
		BlockRenderLayerMaps.register();
		ColorProviders.register();
		Particles.register();
		ClientHook.loadClient();

		EntityModelLayerRegistry.registerModelLayer(SPRAY_PARTICLE_LAYER, SprayParticleEntityModel::getTexturedModelData);

		ModelPredicateProviderRegistry.register(ModItems.MediumConcentrate, new Identifier("amount"),
		  (stack, world, entity, seed) -> stack.getOrCreateNbt().getInt("amount") / 4.0f);
		ModelPredicateProviderRegistry.register(ModItems.BigConcentrate, new Identifier("amount"),
		  (stack, world, entity, seed) -> stack.getOrCreateNbt().getInt("amount") / 10.0f);

	}
}