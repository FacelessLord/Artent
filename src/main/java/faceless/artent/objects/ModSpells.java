package faceless.artent.objects;

import faceless.artent.spells.api.AffinityType;
import faceless.artent.spells.api.Spell;
import faceless.artent.spells.spells.LightSword;
import faceless.artent.spells.spells.MakeLight;
import faceless.artent.spells.spells.Nox;

public class ModSpells {
    public static final Spell MakeLight = new MakeLight().setColor(1, 1, 0.5f).setAffinity(AffinityType.Light);
    public static final Spell LightSword = new LightSword().setColor(1, 1, 0.5f).setAffinity(AffinityType.Light);
    public static final Spell Nox = new Nox().setColor(0.17f, 0.03f, 0.38f).setAffinity(AffinityType.Darkness);

}
