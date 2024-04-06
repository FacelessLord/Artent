package faceless.artent.registries;

import faceless.artent.objects.ModSpells;
import faceless.artent.spells.api.Spell;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

public class SpellRegistry implements IRegistry {
    private static final Hashtable<String, Spell> spells = new Hashtable<>();

    public static List<Spell> getAllSpells(){
        return new ArrayList<>(spells.values());
    }

    @Override
    public void register() {
        register(ModSpells.MakeLight);
    }

    private void register(Spell spell) {
        spells.put(spell.id, spell);
    }

    public static Spell getSpell(String id) {
        if (spells.containsKey(id))
            return spells.get(id);
        System.err.println("Spell \""+id+"\" is not registered");
        return null;
    }
}
