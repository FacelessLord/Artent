package faceless.artent.spells.spells;

import faceless.artent.objects.ModEntities;
import faceless.artent.spells.api.ICaster;
import faceless.artent.spells.api.Spell;
import faceless.artent.spells.entity.SpellParticleEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ProjectileSpell extends Spell {
    public ProjectileSpell(String id, int baseCost) {
        super(id, ActionType.SingleCast | ActionType.BlockCast, baseCost);
    }

    public void action(ICaster caster, World world, ItemStack stack, int castTime) {
        if (!world.isClient) {
            var projectile = new SpellParticleEntity(ModEntities.SPELL_PARTICLE, world);
            projectile.setCaster(caster);
            projectile.setSpell(this);
            projectile.setWandStack(stack);
            projectile.setPosition(caster.getCasterPosition().add(caster.getCasterPosition()).add(0, 1.5f, 0));
            var useTimeCoefficient = 1 + castTime / 10f; // TODO мб поднимать не скорость, а количетсво снарядов
            projectile.setVelocity(caster.getCasterPosition().multiply(useTimeCoefficient));
            world.spawnEntity(projectile);
        }
    }
}
