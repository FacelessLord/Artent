package faceless.artent.spells.spells;

import faceless.artent.spells.api.ICaster;
import faceless.artent.spells.api.SpellSettings;
import faceless.artent.spells.entity.SprayElementEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public class Coldthrower extends SpraySpell {
    public Coldthrower(SpellSettings settings) {
        super("coldthrower", SprayElementEntity.SprayElement.Cold, settings);
    }

    public void onProjectileBlockHit(
      ICaster caster,
      World world,
      ItemStack stack,
      BlockState blockState,
      BlockPos blockPos,
      Direction hitSide
    ) {
        super.onProjectileBlockHit(caster, world, stack, blockState, blockPos, hitSide);
        var block = blockState.getBlock();
        if (block == Blocks.ICE && Math.random() < 1 / 256f)
            world.setBlockState(blockPos, Blocks.PACKED_ICE.getDefaultState());
    }

    @Override
    public void onProjectileEntityHit(ICaster caster, World world, ItemStack stack, Entity entity) {
        super.onProjectileEntityHit(caster, world, stack, entity);
        if (entity instanceof LivingEntity living) {
            living.setFrozenTicks(5 * 20 * caster.getPotency());
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
