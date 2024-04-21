package faceless.artent.spells.entity.ai;

import faceless.artent.spells.api.Affinity;
import faceless.artent.spells.api.ICaster;
import faceless.artent.spells.api.ManaUtils;
import faceless.artent.spells.api.Spell;
import net.minecraft.entity.ai.goal.Goal;

import java.util.List;

public class MageAttackGoal extends Goal {
	public ICaster caster;
	public List<Affinity> affinities;

	public MageAttackGoal(ICaster caster, List<Affinity> affinities) {
		this.caster = caster;
		this.affinities = affinities;
	}

	@Override
	public boolean canStart() {
		var spell = caster.getCurrentSpell();
		if (spell == null)
			return false;
		var neededMana = 0;
		if ((spell.type | Spell.ActionType.SingleCast) > 0)
			neededMana = ManaUtils.evaluateManaToConsume(spell, this.affinities, Spell.ActionType.SingleCast);
		if ((spell.type | Spell.ActionType.Tick) > 0)
			neededMana += ManaUtils.evaluateManaToConsume(spell, this.affinities, Spell.ActionType.Tick) * 10;
		return caster.getMana() >= neededMana;
	}

	@Override
	public boolean shouldContinue() {
		return this.canStart() || caster.getHealthProportion() > 0.3f;
	}
}
