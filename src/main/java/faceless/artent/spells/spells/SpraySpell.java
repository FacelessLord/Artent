package faceless.artent.spells.spells;

import faceless.artent.objects.ModEntities;
import faceless.artent.objects.ModParticles;
import faceless.artent.spells.api.ICaster;
import faceless.artent.spells.api.Spell;
import faceless.artent.spells.api.SpellActionResult;
import faceless.artent.spells.entity.SprayElementEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class SpraySpell extends Spell {
	public SprayElementEntity.SprayElement element;
	public boolean isHurricane = false;

	public SpraySpell(String id, SprayElementEntity.SprayElement element, int baseCost) {
		super(id, ActionType.Tick, baseCost, 20);
		this.element = element;
	}

	@Override
	public SpellActionResult spellTick(ICaster caster, World world, ItemStack stack, int actionTime) {
		if (!(caster instanceof LivingEntity living))
			return SpellActionResult.Stop(0);
		var e1 = living.getRotationVector().normalize();
		var e2 = new Vec3d(-e1.y, e1.x, 0).normalize();
		var e3 = new Vec3d(-e1.x * e1.z, -e1.y * e1.z, e1.x * e1.x + e1.y * e1.y).normalize();

		if (!world.isClient) {
			for (int i = 0; i < 25; i++) {
				var randomAngle = Math.random() * Math.PI * 2;
				var randomRange = Math.random();
				var sin = Math.sin(randomAngle) * randomRange;
				var cos = Math.cos(randomAngle) * randomRange;
				var randomDist = Math.random() * 0.25f;

				var offsetVector = e2.multiply(sin).add(e3.multiply(cos)).add(e1.multiply(randomDist));
				var startPos = living.getPos().add(offsetVector).add(0, 1.75f, 0);

				var particle = new SprayElementEntity(ModEntities.SPRAY_ELEMENT_ENTITY, world);
				particle.setCaster(caster);
				particle.setSprayElement(this.element);
				particle.setMovementType(isHurricane ? SprayElementEntity.RotatingMovent : SprayElementEntity.DirectMovent);
				particle.setVelocity(e1.add(offsetVector.multiply(0.4f)));
				particle.setPosition(startPos.add(e1.multiply(1.5f)));
				particle.setStartingPos(startPos);

				world.spawnEntity(particle);
			}
		} else if (isHurricane) {
			for (int i = 0; i < 500; i++) {
				var random = world.random;
				var randomAngle = random.nextFloat() * Math.PI * 2;

				var offset = new Vec3d(Math.sin(randomAngle) * (1 + random.nextFloat()), random.nextFloat() * 4, Math.cos(randomAngle) * (1 + random.nextFloat())).multiply(3, 1, 3);

				var startingPos = living.getPos().add(offset);
				var velocity = offset.crossProduct(new Vec3d(0, 1, 0));

				world.addParticle(ParticleTypes.FLAME,
				  startingPos.x,
				  startingPos.y,
				  startingPos.z,
				  velocity.x * 0.01f,
				  velocity.y * 0.01f,
				  velocity.z * 0.01f);
			}
		} else {
			for (int i = 0; i < 500; i++) {
				var randomAngle = Math.random() * Math.PI * 2;
				var randomRange = Math.random();
				var sin = Math.sin(randomAngle) * randomRange;
				var cos = Math.cos(randomAngle) * randomRange;
				var randomDist = Math.random() * 3;

				var offsetVector = e2.multiply(sin).add(e3.multiply(cos)).add(e1.multiply(randomDist));
				var startPos = living.getPos().add(offsetVector).add(0, 1.75f, 0);
				var velocity = e1.add(offsetVector.multiply(0.4f)).multiply(0.4f);
				var position = startPos.add(e1.multiply(1.5f));

				var particleType = element == SprayElementEntity.SprayElement.Water ? ModParticles.WaterDroplet : ParticleTypes.FLAME;
				world.addParticle(particleType,
				  position.x,
				  position.y,
				  position.z,
				  velocity.x,
				  velocity.y,
				  velocity.z);

			}
		}

		return SpellActionResult.Continue(baseCost);
	}
}
