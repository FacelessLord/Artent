package faceless.artent.spells.spells;

import faceless.artent.spells.api.ICaster;
import faceless.artent.spells.api.Spell;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class Dash extends Spell {
	public Dash() {
		super("dash", ActionType.SingleCast, 5, 10);
	}

	@Override
	public void action(ICaster caster, World world, ItemStack stack, int castTime) {
		super.action(caster, world, stack, castTime);
		if (!(caster instanceof Entity entity))
			return;
		var rotation = caster.getCasterRotation();
		entity.setVelocity(rotation.multiply(1.25f + Math.min(castTime / 20f, 3f)));
		entity.fallDistance = 0;
	}
}
