package faceless.artent.spells.spells;

import faceless.artent.spells.api.ICaster;
import faceless.artent.spells.api.SpellSettings;
import faceless.artent.spells.entity.SprayElementEntity;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public class FireHurricane extends SpraySpell {
    public FireHurricane(SpellSettings settings) {
        super("fire_hurricane", SprayElementEntity.SprayElement.Fire, settings);
        isHurricane = true;
    }

    @Override
    public void onCollideWithBlock(World world, ICaster caster, BlockState state, BlockPos pos, Direction dir) {
        Flamethrower.fireParticleBlockCollide(world, state, pos);
    }

    @Override
    public void onCollideWithEntity(World world, ICaster caster, Entity entity) {
        Flamethrower.fireParticleEntityCollide(world, caster, entity);
    }

    @Override
    public boolean projectileTick(World world, ICaster caster, BlockState state, BlockPos pos) {
        return Flamethrower.fireParticleTick(world, state, pos);
    }
}
