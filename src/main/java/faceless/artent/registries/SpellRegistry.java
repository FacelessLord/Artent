package faceless.artent.registries;

import faceless.artent.objects.ModSpells;
import faceless.artent.spells.api.Spell;

import java.util.Hashtable;

public class SpellRegistry implements IRegistry {
    private static final Hashtable<String, Spell> spells = new Hashtable<>();

    @Override
    public void register() {
        register(ModSpells.MakeLight);
    }

    private void register(Spell spell) {
        spells.put(spell.id, spell);
    }

    public static Spell getSpell(String id) {
        if (spells.contains(id))
            return spells.get(id);
        return null;
    }
}
