package faceless.artent.spells.entity;

import faceless.artent.registries.SpellRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.world.World;

public class SmallFireballEntity extends BaseSpellProjectile {
    public SmallFireballEntity(
      EntityType<? extends SmallFireballEntity> entityType,
      World world
    ) {
        super(entityType, world);
        this.setNoGravity(false);
    }

    @Override
    public void tick() {
        super.tick();
        var world = getWorld();
        if (world.isClient && world.random.nextBoolean()) {
            var spell = SpellRegistry.getSpell(this.getSpellId());
            if (spell == null) {
                return;
            }
            var pos = getPos();
            var velocity = getVelocity();

            for (int i = 0; i < 15; i++) {
                world.addParticle(
                  ParticleTypes.FLAME,
                  pos.x,
                  pos.y,
                  pos.z,
                  velocity.x * 0.01f,
                  velocity.y * 0.01f,
                  velocity.z * 0.01f
                );
            }
        }

        if (getVelocity().length() < 0.05)
            discard();
    }
}
