package faceless.artent.registries;

import faceless.artent.objects.ModSpells;
import faceless.artent.spells.api.Spell;

import java.util.Hashtable;
import java.util.List;
import java.util.Map;

public class SpellRegistry implements IRegistry {
    private static final Hashtable<String, Spell> spells = new Hashtable<>();

    public static List<Spell> getAllSpells() {
        return spells.entrySet().stream()
                     .sorted((a, b) -> String.CASE_INSENSITIVE_ORDER.compare(a.getKey(), b.getKey()))
                     .map(Map.Entry::getValue).toList();
    }

    @Override
    public void register() {
        register(ModSpells.MakeLight);
        register(ModSpells.Nox);
        register(ModSpells.Wormhole);
        register(ModSpells.LightSword);
        register(ModSpells.GilgameshLightStorm);
        register(ModSpells.Flamethrower);
        register(ModSpells.WaterJet);
        register(ModSpells.Dash);
        register(ModSpells.Gust);
        register(ModSpells.FireHurricane);
    }

    private void register(Spell spell) {
        spells.put(spell.id, spell);
    }

    public static Spell getSpell(String id) {
        if (spells.containsKey(id))
            return spells.get(id);
        System.err.println("Spell \"" + id + "\" is not registered");
        return null;
    }
}
