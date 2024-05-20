package faceless.artent.spells.spells;

import faceless.artent.objects.ModEntities;
import faceless.artent.objects.ModParticles;
import faceless.artent.spells.api.ICaster;
import faceless.artent.spells.api.Spell;
import faceless.artent.spells.api.SpellActionResult;
import faceless.artent.spells.entity.SprayElementEntity;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public abstract class SpraySpell extends Spell {
    public SprayElementEntity.SprayElement element;
    public boolean isHurricane = false;

    public SpraySpell(String id, SprayElementEntity.SprayElement element, int baseCost) {
        super(id, ActionType.Tick, baseCost, 20);
        this.element = element;
        maxActionDistance = 6;
        prepareTime = 10;
    }

    @Override
    public SpellActionResult spellTick(ICaster caster, World world, ItemStack stack, int actionTime) {
        if (!(caster instanceof LivingEntity living)) return SpellActionResult.Stop(0);
        var e1 = living.getRotationVector().normalize();
        var e2 = new Vec3d(-e1.y, e1.x, 0).normalize();
        var e3 = new Vec3d(-e1.x * e1.z, -e1.y * e1.z, e1.x * e1.x + e1.y * e1.y).normalize();

        // TODO: to spell files
        if (!world.isClient) {
            for (int i = 0; i < 25; i++) {
                var randomAngle = Math.random() * Math.PI * 2;
                var randomRange = Math.random();
                var sin = Math.sin(randomAngle) * randomRange;
                var cos = Math.cos(randomAngle) * randomRange;
                var randomDist = Math.random() * 0.25f;

                var offsetVector = e2.multiply(sin).add(e3.multiply(cos)).add(e1.multiply(randomDist));
                var startPos = living.getPos().add(offsetVector).add(0, 1.75f, 0);

                var particle = new SprayElementEntity(ModEntities.SPRAY_ELEMENT_ENTITY, world);
                particle.setCaster(caster);
                particle.setSprayElement(this.element);
                particle.setSpell(this);
                particle.setMovementType(isHurricane
                                           ? SprayElementEntity.RotatingMovement
                                           : SprayElementEntity.DirectMovement);
                particle.setVelocity(e1.add(offsetVector.multiply(0.4f)));
                particle.setPosition(startPos.add(e1.multiply(1.5f)));
                particle.setStartingPos(startPos);

                world.spawnEntity(particle);
            }
        } else if (isHurricane) {
            for (int i = 0; i < 500; i++) {
                var randomAngle = Math.random() * Math.PI * 2;

                var offset = new Vec3d(Math.sin(randomAngle) * (1 + Math.random()),
                                       Math.random() * 4,
                                       Math.cos(randomAngle) * (1 + Math.random())).multiply(3, 1, 3);

                var startingPos = living.getPos().add(offset);
                var velocity = offset.crossProduct(new Vec3d(0, 1, 0));

                world.addParticle(ParticleTypes.FLAME,
                                  startingPos.x,
                                  startingPos.y,
                                  startingPos.z,
                                  velocity.x * 0.01f,
                                  velocity.y * 0.01f,
                                  velocity.z * 0.01f);
            }
        } else {
            for (int i = 0; i < 50; i++) { // TODO 500 as before
                var randomAngle = Math.random() * Math.PI * 2;
                var randomRange = Math.random();
                var sin = Math.sin(randomAngle) * randomRange;
                var cos = Math.cos(randomAngle) * randomRange;
                var randomDist = Math.random() * 3;

                var offsetVector = e2.multiply(sin).add(e3.multiply(cos)).add(e1.multiply(randomDist));
                var startPos = living.getPos().add(offsetVector).add(0, 1.75f, 0);
                var velocity = e1.add(offsetVector.multiply(0.4f)).multiply(0.4f);
                var position = startPos.add(e1.multiply(1.5f));

                var particleType = element == SprayElementEntity.SprayElement.Water
                  ? ModParticles.WaterDroplet
                  : element == SprayElementEntity.SprayElement.Fire
                    ? ParticleTypes.FLAME
                    : ParticleTypes.FIREWORK; // TODO: other texture and less lifetime (like other two)
                world.addParticle(particleType, position.x, position.y, position.z, velocity.x, velocity.y, velocity.z);

            }
        }

        return SpellActionResult.Continue(baseCost);
    }

    public abstract void onCollideWithBlock(World world, ICaster caster, BlockState blockState, BlockPos blockPos, Direction dir);

    public abstract void onCollideWithEntity(World world, ICaster caster, Entity entity);

    public abstract boolean projectileTick(World world, ICaster caster, BlockState blockState, BlockPos blockPos);

    public static DamageSource createDamageSource(World world, ICaster caster) {
        return caster instanceof LivingEntity livingCaster
          ? world.getDamageSources().mobAttack(livingCaster)
          : world.getDamageSources().magic();
    }
}
