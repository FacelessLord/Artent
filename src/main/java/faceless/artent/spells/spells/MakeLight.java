package faceless.artent.spells.spells;

import faceless.artent.objects.ModBlocks;
import faceless.artent.objects.ModEntities;
import faceless.artent.spells.api.ICaster;
import faceless.artent.spells.api.SpellSettings;
import faceless.artent.spells.entity.BaseSpellProjectile;
import faceless.artent.spells.entity.SpellParticleEntity;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public class MakeLight extends ProjectileSpell {
    public MakeLight(SpellSettings settings) {
        super("lightbulb", settings);
    }

    @Override
    public BaseSpellProjectile createProjectile(ICaster caster, World world, ItemStack stack) {
        return new SpellParticleEntity(ModEntities.SPELL_PARTICLE, world);
    }



    public void onProjectileBlockHit(ICaster caster, World world, ItemStack stack, BlockState blockState, BlockPos hitPos, Direction hitSide) {
        super.onProjectileBlockHit(caster, world, stack, blockState, hitPos, hitSide);

        var blockPos = hitPos.offset(hitSide);
        if (world.getBlockState(blockPos).isAir())
            world.setBlockState(blockPos, ModBlocks.LightBlock.getDefaultState());
    }
}
