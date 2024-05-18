package faceless.artent.spells.api;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.joml.Vector3f;

import java.util.Random;

public class Spell {
    public final int type;
    public String id;
    public int baseCost;
    public int prepareCost;
    public Vector3f color = new Vector3f(1, 1, 1);
    // used for calculating mage position for successful cast
    public float maxActionDistance = 32;

    public int cooldown;
    public int prepareTime = 3 * 20;

    public AffinityType affinityType = AffinityType.None;

    // TODO SpellSettings
    public Spell(String id, int type, int baseCost, int cooldown) {
        this.type = type;
        this.id = id;
        this.baseCost = baseCost;
        this.cooldown = cooldown;
    }

    public Spell setColor(float r, float g, float b) {
        this.color = new Vector3f(r, g, b);
        return this;
    }

    public Spell setAffinity(AffinityType affinityType) {
        this.affinityType = affinityType;
        return this;
    }

    public float getRecoilChance(LivingEntity player, World world) {
        return 0;
    }

    public void action(ICaster caster, World world, ItemStack stack, int castTime) {

    }

    public SpellActionResult spellTick(ICaster caster, World world, ItemStack stack, int actionTime) {
        return SpellActionResult.Continue(0);
    }

    public void prepareTick(ICaster caster, World world, ItemStack stack, int actionTime) {
        for (int i = 0; i < 5; i++) {
            var random = new Random();
            var randomAngle = random.nextFloat() * Math.PI * 2;

            var offset = new Vec3d(Math.sin(randomAngle) * 0.5f,
                                   -0.5,
                                   Math.cos(randomAngle) * 0.5f);

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

//    public void blockCast(
//      ICaster caster,
//      World world,
//      ItemStack stack,
//      BlockPos blockPos,
//      Direction hitSide,
//      int actionTime
//    ) {
//
//    }

    public void onRecoil(ICaster caster, World world, ItemStack stack, int actionTime) {

    }

    public boolean isTickAction() {
        return (type & ActionType.Tick) > 0;
    }

    public boolean isSingleCastAction() {
        return (type & ActionType.SingleCast) > 0;
    }

    public boolean isBlockCastAction() {
        return (type & ActionType.BlockCast) > 0;
    }

    public static class ActionType { // TODO spell methods for setTickAction, setSingleCastAction, setBlockCastAction
        public static final int Tick = 1;
        public static final int SingleCast = 2;
        public static final int BlockCast = 4;
    }
}
