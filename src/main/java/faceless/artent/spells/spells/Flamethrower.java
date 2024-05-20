package faceless.artent.spells.spells;

import faceless.artent.spells.api.ICaster;
import faceless.artent.spells.entity.SprayElementEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FluidBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public class Flamethrower extends SpraySpell {
    public Flamethrower() {
        super("flamethrower", SprayElementEntity.SprayElement.Fire, 5);
    }


    @Override
    public void onCollideWithBlock(World world, ICaster caster, BlockState state, BlockPos pos, Direction dir) {
        fireParticleBlockCollide(world, state, pos);
    }


    @Override
    public void onCollideWithEntity(World world, ICaster caster, Entity entity) {
        fireParticleEntityCollide(world, caster, entity);
    }

    @Override
    public boolean projectileTick(World world, ICaster caster, BlockState state, BlockPos pos) {
        return fireParticleTick(world, state, pos);
    }

    public static void fireParticleBlockCollide(World world, BlockState state, BlockPos pos) {
        var block = state.getBlock();
        if (block == Blocks.ICE ||
            block == Blocks.BLUE_ICE ||
            block == Blocks.FROSTED_ICE ||
            block == Blocks.PACKED_ICE ||
            block == Blocks.SNOW_BLOCK ||
            block == Blocks.POWDER_SNOW) world.setBlockState(pos, Blocks.WATER.getDefaultState());
    }

    public static void fireParticleEntityCollide(World world, ICaster caster, Entity entity) {
        entity.setOnFireFor(20);

        if (entity instanceof LivingEntity living) {
            living.damage(createDamageSource(world, caster), caster.getPotency());
        }
    }

    public static boolean fireParticleTick(World world, BlockState state, BlockPos pos) {
        var block = state.getBlock();
        if (block == Blocks.SNOW) {
            world.setBlockState(pos, Blocks.WATER.getDefaultState().with(FluidBlock.LEVEL, 5));
            return true;
        }
        return false;
    }
}
