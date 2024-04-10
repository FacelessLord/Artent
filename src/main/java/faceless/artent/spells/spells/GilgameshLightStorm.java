package faceless.artent.spells.spells;

import faceless.artent.spells.api.ICaster;
import faceless.artent.spells.api.Spell;
import faceless.artent.spells.api.SpellActionResult;
import faceless.artent.spells.entity.LightSwordProjectileEntity;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;

public class GilgameshLightStorm extends Spell {
    public GilgameshLightStorm() {
        super("gilgamesh_light_storm", ActionType.Tick, 1);
    }

    @Override
    public SpellActionResult spellTick(ICaster caster, World world, ItemStack stack, int actionTime) {
        if (!(caster instanceof Entity entity))
            return SpellActionResult.Continue(1);

        if (!world.isClient) {
            var casterPos = entity.getPos();
            var raycastContext = new RaycastContext(
                    casterPos.add(0, 1, 0),
                    casterPos.add(entity.getRotationVector().multiply(100)).add(0, 1, 0),
                    RaycastContext.ShapeType.COLLIDER,
                    RaycastContext.FluidHandling.NONE,
                    ShapeContext.of(entity));

            var blockHitResult = world.raycast(raycastContext);
            if (blockHitResult != null) {
                for (int i = 0; i < 5; i++) {
                    var targetRange = 3;
                    var targetPos = blockHitResult.getPos().add(
                            (Math.random() - 0.5) * targetRange,
                            (Math.random() - 0.5) * targetRange,
                            (Math.random() - 0.5) * targetRange
                    );
                    var attackHeight = 30;
                    var attackRange = Math.random() * 15;
                    var spawnAngle = Math.random() * Math.PI * 2;

                    var spawnPos = targetPos.add(Math.cos(spawnAngle) * attackRange, attackHeight, Math.sin(spawnAngle) * attackRange);

                    var projectile = new LightSwordProjectileEntity(world);
                    projectile.setCaster(caster);
                    projectile.setSpell(this);
                    projectile.setWandStack(stack);
                    projectile.setPosition(spawnPos);
                    projectile.setYaw(entity.getYaw());
                    projectile.setPitch(entity.getPitch());

                    var velocityVec = targetPos.subtract(spawnPos).normalize();
                    projectile.setVelocity(velocityVec.multiply(2));
                    world.spawnEntity(projectile);
                }


                return SpellActionResult.Continue(1);
            }
        }

        return SpellActionResult.Continue(0);
    }
}
