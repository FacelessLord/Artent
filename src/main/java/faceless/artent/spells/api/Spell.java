package faceless.artent.spells.api;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public class Spell {
	public final int type;
	public String id;
	public int baseCost;

	public Spell(String id, int type, int baseCost) {
		this.type = type;
		this.id = id;
		this.baseCost = baseCost;
	}

	public float getRecoilChance(LivingEntity player, World world) {
		return 0;
	}

	public void action(ICaster caster, World world, ItemStack stack, int castTime) {

	}

	public SpellActionResult spellTick(ICaster caster, World world, ItemStack stack, int actionTime) {
		return SpellActionResult.Continue(0);
	}

	public void blockCast(ICaster caster,
		World world,
		ItemStack stack,
		BlockPos blockPos,
		Direction hitSide,
		int actionTime) {

	}

	public void onRecoil(ICaster caster, World world, ItemStack stack, int actionTime) {

	}

	public static class ActionType {
		public static final int Tick = 1;
		public static final int SingleCast = 2;
		public static final int BlockCast = 4;
	}
}
