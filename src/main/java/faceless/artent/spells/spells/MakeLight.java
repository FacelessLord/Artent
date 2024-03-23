package faceless.artent.spells.spells;

import faceless.artent.objects.ModBlocks;
import faceless.artent.objects.ModEntities;
import faceless.artent.spells.api.ICaster;
import faceless.artent.spells.api.Spell;
import faceless.artent.spells.entity.LightbulbEntity;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public class MakeLight extends Spell {
	public MakeLight() {
		super("lightbulb", ActionType.SingleCast | ActionType.BlockCast, 3);
	}

	public void action(ICaster caster, World world, ItemStack stack, int castTime) {
		if (!(caster instanceof Entity entity))
			return;
		var lightbulb = new LightbulbEntity(ModEntities.LIGHTBULB, world);
		lightbulb.setCaster(caster);
		lightbulb.setSpellId(this.id);
		lightbulb.setWandStack(stack);
		lightbulb.setPosition(entity.getPos().add(entity.getRotationVector()).add(0, 1, 0));
		lightbulb.setVelocity(entity.getRotationVector().multiply(2));
		world.spawnEntity(lightbulb);
	}

	@Override
	public void blockCast(ICaster caster,
		World world,
		ItemStack stack,
		BlockPos hitPos,
		Direction hitSide,
		int actionTime) {
		var blockPos = hitPos.offset(hitSide);
		var lightBlockState = ModBlocks.LightBlock.getDefaultState();
		world.setBlockState(blockPos, lightBlockState);
	}
}
