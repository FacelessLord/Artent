package faceless.artent.spells.spells;

import faceless.artent.spells.api.ICaster;
import faceless.artent.spells.api.Spell;
import faceless.artent.spells.api.SpellActionResult;
import faceless.artent.spells.entity.LightSwordProjectileEntity;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;

public class GilgameshLightStorm extends Spell {
	public GilgameshLightStorm() {
		super("gilgamesh_light_storm", ActionType.Tick, 1, 80);
	}

	@Override
	public SpellActionResult spellTick(ICaster caster, World world, ItemStack stack, int actionTime) {
		if (!world.isClient) {
			var casterPos = caster.getCasterPosition();
			var start = casterPos.add(0, 1, 0);
			var end = casterPos.add(caster.getCasterRotation().multiply(100)).add(0, 1, 0);

			Vec3d targetPos = null;
			if (caster instanceof Entity entity) {
				var result = ProjectileUtil.raycast(entity, start, end, new Box(start, end), e -> true, end.subtract(start).length());
				if (result != null) {
					targetPos = result.getPos();
				}
			}
			if (targetPos == null) {
				var raycastContext = new RaycastContext(
				  start,
				  end,
				  RaycastContext.ShapeType.COLLIDER,
				  RaycastContext.FluidHandling.NONE,
				  ShapeContext.absent());
				var blockHitResult = world.raycast(raycastContext);

				if (blockHitResult != null) {
					targetPos = blockHitResult.getPos();
				}
			}
			if (targetPos == null)
				return SpellActionResult.Continue(0);

			for (int i = 0; i < 5; i++) {
				var targetRange = 3;
				var projectileTargetPos = targetPos.add(
				  (Math.random() - 0.5) * targetRange,
				  (Math.random() - 0.5) * targetRange,
				  (Math.random() - 0.5) * targetRange
				);
				var attackHeight = 30;
				var attackRange = Math.random() * 15;
				var spawnAngle = Math.random() * Math.PI * 2;

				var spawnPos = projectileTargetPos.add(Math.cos(spawnAngle) * attackRange, attackHeight, Math.sin(spawnAngle) * attackRange);

				var projectile = new LightSwordProjectileEntity(world);
				projectile.setCaster(caster);
				projectile.setSpell(this);
				projectile.setWandStack(stack);
				projectile.setPosition(spawnPos);

				var velocityVec = projectileTargetPos.subtract(spawnPos).normalize();
				projectile.setVelocity(velocityVec.multiply(2));
				world.spawnEntity(projectile);
			}
			return SpellActionResult.Continue(1);
		}

		return SpellActionResult.Continue(0);
	}
}
