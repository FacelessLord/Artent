package faceless.artent.spells.spells;

import faceless.artent.objects.ModEntities;
import faceless.artent.spells.api.ICaster;
import faceless.artent.spells.api.Spell;
import faceless.artent.spells.entity.SpellParticleEntity;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ProjectileSpell extends Spell {
    public ProjectileSpell(String id, int baseCost) {
        super(id, ActionType.SingleCast | ActionType.BlockCast, baseCost);
    }

    public void action(ICaster caster, World world, ItemStack stack, int castTime) {
        if (!(caster instanceof Entity entity))
            return;
        if (!world.isClient) {
            var projectile = new SpellParticleEntity(ModEntities.SPELL_PARTICLE, world);
            projectile.setCaster(caster);
            projectile.setSpell(this);
            projectile.setWandStack(stack);
            projectile.setPosition(entity.getPos().add(entity.getRotationVector()).add(0, 1, 0));
            var useTimeCoefficient = 1 + castTime / 60f; // TODO мб поднимать не скорость, а количетсво снарядов
            projectile.setVelocity(entity.getRotationVector().multiply(useTimeCoefficient));
            world.spawnEntity(projectile);
        }
    }
}
