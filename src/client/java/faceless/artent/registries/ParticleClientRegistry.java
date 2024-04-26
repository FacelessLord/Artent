package faceless.artent.registries;

import faceless.artent.objects.ModParticles;
import faceless.artent.particle.WaterParticle;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;

public class ParticleClientRegistry implements IRegistry{
	@Override
	public void register() {
		ParticleFactoryRegistry.getInstance().register(ModParticles.WaterDroplet, WaterParticle.Factory::new);
	}
}
