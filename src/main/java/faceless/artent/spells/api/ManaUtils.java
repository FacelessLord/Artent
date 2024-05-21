package faceless.artent.spells.api;

import java.util.List;

public class ManaUtils {

    public static int evaluateManaToConsume(Spell spell, List<Affinity> wandAffinities, int actionType) {
        var spellAffinityType = spell.settings.affinityType;
        var correspondingWandAffinity = getCorrespondingWandAffinity(wandAffinities, spellAffinityType);
        if (correspondingWandAffinity != null) {
            return (int) (spell.settings.baseCost * (1 - correspondingWandAffinity.value));
        }
        return spell.settings.baseCost;
    }

    public static int evaluatePrepareManaToConsume(Spell spell, List<Affinity> wandAffinities, int actionType) {
        if (actionType == SpellSettings.ActionType.SingleCast)
            return 0; // it's hard to divide baseCost on prepareTime, so I will use mana only on Action
        var spellAffinityType = spell.settings.affinityType;
        var correspondingWandAffinity = getCorrespondingWandAffinity(wandAffinities, spellAffinityType);
        if (correspondingWandAffinity != null) {
            var cost = spell.settings.prepareCost;
            var divider = 2;
            return (int) (cost * (1 - correspondingWandAffinity.value)) / divider;
        }
        return spell.settings.baseCost;
    }

    public static Affinity getCorrespondingWandAffinity(List<Affinity> wandAffinities, AffinityType spellAffinity) {
        for (int i = 0; i < wandAffinities.size(); i++) {
            if (wandAffinities.get(i).type == spellAffinity)
                return wandAffinities.get(i);
        }
        return null;
    }
}
