package faceless.artent.spells.spells;

import faceless.artent.objects.ModBlocks;
import faceless.artent.objects.ModEntities;
import faceless.artent.spells.api.ICaster;
import faceless.artent.spells.api.SpellSettings;
import faceless.artent.spells.entity.BaseSpellProjectile;
import faceless.artent.spells.entity.SpellParticleEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public class Nox extends ProjectileSpell {
    public Nox(SpellSettings settings) {
        super("nox", settings);
    }

    @Override
    public BaseSpellProjectile createProjectile(ICaster caster, World world, ItemStack stack) {
        return new SpellParticleEntity(ModEntities.SPELL_PARTICLE, world);
    }

    public void onProjectileBlockHit(ICaster caster, World world, ItemStack stack, BlockState blockState, BlockPos blockPos, Direction hitSide) {
        super.onProjectileBlockHit(caster, world, stack, blockState, blockPos, hitSide);

        var range = 8;

        for (int i = -range; i <= range; i++) {
            for (int j = -range; j <= range; j++) {
                for (int k = -range; k <= range; k++) {
                    var pos = blockPos.add(i, j, k);
                    var block = world.getBlockState(pos).getBlock();
                    if (block == ModBlocks.LightBlock) {
                        if (caster.consumeMana(-2))
                            world.setBlockState(pos, Blocks.AIR.getDefaultState());
                    }
                    if (block == Blocks.TORCH || block == Blocks.WALL_TORCH || block == Blocks.GLOWSTONE) {
                        if (caster.consumeMana(2))
                            world.breakBlock(pos, true);
                    }
                }
            }
        }
    }
}
