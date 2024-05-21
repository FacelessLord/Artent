package faceless.artent.spells.spells;

import faceless.artent.spells.api.ICaster;
import faceless.artent.spells.api.Spell;
import faceless.artent.spells.api.SpellSettings;
import faceless.artent.spells.entity.BaseSpellProjectile;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public abstract class ProjectileSpell extends Spell {
    public ProjectileSpell(String id, SpellSettings settings) {
        super(id, settings);
    }

    public abstract BaseSpellProjectile createProjectile(ICaster caster, World world, ItemStack stack);

    public void action(ICaster caster, World world, ItemStack stack, int castTime) {
        if (!world.isClient) {
            var projectile = createProjectile(caster, world, stack);
            projectile.setCaster(caster);
            projectile.setSpell(this);
            projectile.setWandStack(stack);
            projectile.setPosition(caster.getCasterPosition().add(caster.getCasterRotation()).add(0, 1.5f, 0));
            projectile.setVelocity(caster.getCasterRotation().multiply(2));
            world.spawnEntity(projectile);
        }
    }
}
