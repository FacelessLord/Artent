package faceless.artent.spells.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.particle.DustParticleEffect;
import net.minecraft.world.World;

public class SpellParticleEntity extends BaseSpellProjectile {
    public SpellParticleEntity(
      EntityType<? extends SpellParticleEntity> entityType, World world
    ) {
        super(entityType, world);
    }

    @Override
    public void onRemoved() {
        super.onRemoved();
    }

    @Override
    public void onSpawnPacket(EntitySpawnS2CPacket packet) {
        super.onSpawnPacket(packet);
    }

    @Override
    public void tick() {
        super.tick();
        var world = getWorld();
        var spell = getSpell();
        if (world.isClient && world.random.nextBoolean() && spell != null) {
            world.addParticle(new DustParticleEffect(spell.settings.color, 1),
                              getParticleX(0.05f),
                              this.getRandomBodyY(),
                              getParticleZ(0.05f),
                              0,
                              0,
                              0
            );
        }

        if (getVelocity().length() < 0.05) discard();
    }
}
