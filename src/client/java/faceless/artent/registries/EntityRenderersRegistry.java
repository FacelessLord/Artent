package faceless.artent.registries;

import faceless.artent.mobs.CrowEntityRenderer;
import faceless.artent.objects.ModEntities;
import faceless.artent.spells.BaseSpellProjectileRenderer;
import faceless.artent.spells.LightSwordProjectileEntityRenderer;
import faceless.artent.spells.MageEntityRenderer;
import faceless.artent.spells.SprayParticleEntityRenderer;
import faceless.artent.trading.CoinEntityRenderer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.render.entity.FlyingItemEntityRenderer;


public class EntityRenderersRegistry implements IRegistry {
    @Override
    public void register() {
        EntityRendererRegistry.register(ModEntities.POTION_PHIAL, FlyingItemEntityRenderer::new);
        EntityRendererRegistry.register(ModEntities.COIN_ENTITY, CoinEntityRenderer::new);
        EntityRendererRegistry.register(ModEntities.CROW_ENTITY, CrowEntityRenderer::new);
        EntityRendererRegistry.register(ModEntities.SPELL_PARTICLE, BaseSpellProjectileRenderer::new);
        EntityRendererRegistry.register(ModEntities.SMALL_FIREBALL, BaseSpellProjectileRenderer::new);
        EntityRendererRegistry.register(ModEntities.LIGHT_SWORD, LightSwordProjectileEntityRenderer::new);
        EntityRendererRegistry.register(ModEntities.SPRAY_ELEMENT_ENTITY, SprayParticleEntityRenderer::new);
        EntityRendererRegistry.register(ModEntities.MAGE_ENTITY, MageEntityRenderer::new);
    }
}
