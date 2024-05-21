package faceless.artent.spells.api;

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

import java.util.Random;

public class Spell {
    public final String id;
    public final SpellSettings settings;

    public Spell(String id, SpellSettings settings) {
        this.id = id;
        this.settings = settings;
    }

    public float getRecoilChance(LivingEntity player, World world) {
        return 0;
    }

    public void action(ICaster caster, World world, ItemStack stack, int castTime) {

    }

    public SpellActionResult spellTick(ICaster caster, World world, ItemStack stack, int actionTime) {
        return SpellActionResult.Continue();
    }

    public void prepareTick(ICaster caster, World world, ItemStack stack, int actionTime) {
        for (int i = 0; i < 5; i++) {
            var random = new Random();
            var randomAngle = random.nextFloat() * Math.PI * 2;

            var offset = new Vec3d(Math.sin(randomAngle) * 0.5f, -0.5, Math.cos(randomAngle) * 0.5f);

            var startingPos = caster.getCasterPosition().add(offset).add(0, 0.5, 0);
            var velocity = offset.multiply(1).rotateY((float) Math.PI / 2f);

            world.addParticle(ParticleTypes.INSTANT_EFFECT,
                              startingPos.x,
                              startingPos.y,
                              startingPos.z,
                              velocity.x * 0.1f,
                              velocity.y * 0.1f,
                              velocity.z * 0.1f);
        }
    }

    public void onRecoil(ICaster caster, World world, ItemStack stack, int actionTime) {

    }

    public boolean isTickAction() {
        return settings.isTickAction();
    }

    public boolean isSingleCastAction() {
        return settings.isSingleCastAction();
    }


    public void onProjectileBlockHit(ICaster caster, World world, ItemStack stack, BlockState blockState, BlockPos blockPos, Direction hitSide) {

    }
    public void onProjectileEntityHit(
      ICaster caster,
      World world,
      ItemStack stack,
      Entity entity
    ) {

    }

    public static DamageSource createDamageSource(World world, ICaster caster) {
        return caster instanceof LivingEntity livingCaster
          ? world.getDamageSources().mobAttack(livingCaster)
          : world.getDamageSources().magic();
    }
}
