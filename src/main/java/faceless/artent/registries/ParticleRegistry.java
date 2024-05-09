package faceless.artent.registries;

import faceless.artent.Artent;
import faceless.artent.objects.ModParticles;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ParticleRegistry implements IRegistry {
    @Override
    public void register() {
        register("water_droplet", ModParticles.WaterDroplet);
    }

    public <T extends ParticleEffect> void register(String id, ParticleType<T> type) {
        Registry.register(Registries.PARTICLE_TYPE, new Identifier(Artent.MODID, id), type);
    }
}
