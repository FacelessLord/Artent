package faceless.artent.spells.spells;

import faceless.artent.spells.api.ICaster;
import faceless.artent.spells.api.Spell;
import faceless.artent.spells.entity.LightSwordProjectileEntity;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class LightSword extends Spell {
    public LightSword() {
        super("light_sword", ActionType.SingleCast, 10);
    }

    public void action(ICaster caster, World world, ItemStack stack, int castTime) {
        if (!(caster instanceof Entity entity))
            return;
        if (!world.isClient) {
            var projectile = new LightSwordProjectileEntity(world);
            projectile.setCaster(caster);
            projectile.setSpell(this);
            projectile.setWandStack(stack);
            projectile.setPosition(entity.getPos().add(entity.getRotationVector().multiply(-0.25f)).add(0, 1.5f, 0));
            var useTimeCoefficient = 1 + castTime / 20f; // TODO мб поднимать не скорость, а количетсво снарядов
            projectile.setVelocity(entity.getRotationVector().multiply(useTimeCoefficient));
            world.spawnEntity(projectile);
        }
    }
}
