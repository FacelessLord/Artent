package faceless.artent.spells.spells;

import faceless.artent.spells.api.ICaster;
import faceless.artent.spells.api.SpellSettings;
import faceless.artent.spells.entity.SprayElementEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FarmlandBlock;
import net.minecraft.block.Fertilizable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.EndermanEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public class WaterJet extends SpraySpell {
    public WaterJet(SpellSettings settings) {
        super("water_jet", SprayElementEntity.SprayElement.Water, settings);
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

        if (block == Blocks.DIRT) {
            world.setBlockState(blockPos, Blocks.GRASS_BLOCK.getDefaultState());
        }
        if (block instanceof FarmlandBlock) {
            world.setBlockState(blockPos, blockState.with(FarmlandBlock.MOISTURE, FarmlandBlock.MAX_MOISTURE));
        }
        if (block instanceof Fertilizable fertilizable && world.random.nextFloat() < 0.0125) {
            fertilizable.grow((ServerWorld) world, world.random, blockPos, blockState);
        }
        int r = 2;
        for (int i = -r; i <= r; i++) {
            for (int j = -r; j <= r; j++) {
                for (int k = -r; k <= r; k++) {
                    if (Math.random() < 0.25f) {
                        var offsetPos = blockPos.add(i, j, k);
                        var offsetBlock = world.getBlockState(offsetPos).getBlock();
                        if (offsetBlock == Blocks.FIRE) {
                            world.setBlockState(offsetPos, Blocks.AIR.getDefaultState());
                        }
                    }
                }
            }
        }
    }

    @Override
    public void onProjectileEntityHit(ICaster caster, World world, ItemStack stack, Entity entity) {
        super.onProjectileEntityHit(caster, world, stack, entity);
        if (entity instanceof EndermanEntity enderman) {
            enderman.damage(createDamageSource(world, caster), caster.getPotency());
        }
        if (entity.isOnFire()) {
            entity.extinguish();
        }
    }

    @Override
    public boolean projectileTick(World world, ICaster caster, BlockState blockState, BlockPos blockPos) {
        var block = blockState.getBlock();
        if (block == Blocks.FIRE) {
            world.setBlockState(blockPos, Blocks.AIR.getDefaultState());
            return true;
        }
        if (block instanceof Fertilizable fertilizable && !world.isClient && Math.random() < 0.0125) {
            fertilizable.grow((ServerWorld) world, world.random, blockPos, blockState);
            return true;
        }

        return false;
    }
}
