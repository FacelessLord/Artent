package faceless.artent.spells.spells;

import faceless.artent.objects.ModPotionEffects;
import faceless.artent.spells.api.ICaster;
import faceless.artent.spells.entity.SprayElementEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public class Coldthrower extends SpraySpell {
    public Coldthrower() {
        super("coldthrower", SprayElementEntity.SprayElement.Cold, 5);
    }

    @Override
    public void onCollideWithBlock(
      World world, ICaster caster, BlockState blockState, BlockPos blockPos, Direction dir
    ) {
        var block = blockState.getBlock();
        if (block == Blocks.ICE && Math.random() < 1 / 256f)
            world.setBlockState(blockPos, Blocks.PACKED_ICE.getDefaultState());
    }

    @Override
    public void onCollideWithEntity(World world, ICaster caster, Entity entity) {
        if (entity instanceof LivingEntity living) {
            living.addStatusEffect(new StatusEffectInstance(ModPotionEffects.FREEZING, 20 * caster.getPotency()));
            living.damage(createDamageSource(world, caster), caster.getPotency() / 2f);
        }
    }

    @Override
    public boolean projectileTick(World world, ICaster caster, BlockState blockState, BlockPos blockPos) {
        var block = blockState.getBlock();
        if (block == Blocks.WATER) world.setBlockState(blockPos, Blocks.ICE.getDefaultState());
        if (block == Blocks.LAVA) world.setBlockState(blockPos, Blocks.OBSIDIAN.getDefaultState());
        return true;
    }
}
