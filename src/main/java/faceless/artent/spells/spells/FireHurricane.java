package faceless.artent.spells.spells;

import faceless.artent.spells.api.ICaster;
import faceless.artent.spells.api.SpellSettings;
import faceless.artent.spells.entity.SprayElementEntity;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public class FireHurricane extends SpraySpell {
    public FireHurricane(SpellSettings settings) {
        super("fire_hurricane", SprayElementEntity.SprayElement.Fire, settings);
        isHurricane = true;
    }

    public void onProjectileBlockHit(ICaster caster, World world, ItemStack stack, BlockState blockState, BlockPos blockPos, Direction hitSide) {
        super.onProjectileBlockHit(caster, world, stack, blockState, blockPos, hitSide);
        Flamethrower.fireParticleBlockCollide(world, blockState, blockPos);
    }

    @Override
    public void onProjectileEntityHit(ICaster caster, World world, ItemStack stack, Entity entity) {
        super.onProjectileEntityHit(caster, world, stack, entity);
        Flamethrower.fireParticleEntityCollide(world, caster, entity);
    }

    @Override
    public boolean projectileTick(World world, ICaster caster, BlockState state, BlockPos pos) {
        return Flamethrower.fireParticleTick(world, state, pos);
    }
}
