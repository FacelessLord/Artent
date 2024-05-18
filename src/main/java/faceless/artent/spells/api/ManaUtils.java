package faceless.artent.spells.api;

import java.util.List;

public class ManaUtils {

    public static int evaluateManaToConsume(Spell spell, List<Affinity> wandAffinities, int actionType) {
        if (actionType != Spell.ActionType.Tick)
            return 0;

        var spellAffinityType = spell.affinityType;
        var correspondingWandAffinity = getCorrespondingWandAffinity(wandAffinities, spellAffinityType);
        if (correspondingWandAffinity != null) {
            return (int) (spell.baseCost * (1 - correspondingWandAffinity.value));
        }
        return spell.baseCost;
    }

    public static int evaluatePrepareManaToConsume(Spell spell, List<Affinity> wandAffinities, int actionType) {
        var spellAffinityType = spell.affinityType;
        var correspondingWandAffinity = getCorrespondingWandAffinity(wandAffinities, spellAffinityType);
        if (correspondingWandAffinity != null) {
            var cost = actionType == Spell.ActionType.SingleCast ? spell.baseCost : spell.prepareCost;
            var divider = actionType == Spell.ActionType.SingleCast ? spell.prepareTime : 2;
            return (int) (cost * (1 - correspondingWandAffinity.value)) / divider;
        }
        return spell.baseCost;
    }

    public static Affinity getCorrespondingWandAffinity(List<Affinity> wandAffinities, AffinityType spellAffinity) {
        for (int i = 0; i < wandAffinities.size(); i++) {
            if (wandAffinities.get(i).type == spellAffinity)
                return wandAffinities.get(i);
        }
        return null;
    }
}
