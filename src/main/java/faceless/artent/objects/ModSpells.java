package faceless.artent.objects;

import faceless.artent.spells.api.AffinityType;
import faceless.artent.spells.api.Spell;
import faceless.artent.spells.spells.*;

public class ModSpells {
	public static final Spell MakeLight = new MakeLight().setColor(1, 1, 0.5f).setAffinity(AffinityType.Light);
	public static final Spell LightSword = new LightSword().setColor(1, 1, 0.5f).setAffinity(AffinityType.Light);
	public static final Spell Nox = new Nox().setColor(0.17f, 0.03f, 0.38f).setAffinity(AffinityType.Void);
	public static final Spell GilgameshLightStorm = new GilgameshLightStorm().setColor(1, 1, 0.5f).setAffinity(AffinityType.Light);
	public static final Spell Wormhole = new WormHole().setColor(0.17f, 0.03f, 0.38f).setAffinity(AffinityType.Void);
	public static final Spell Flamethrower = new Flamethrower().setColor(0.70f, 0.05f, 0.05f).setAffinity(AffinityType.Void);
	public static final Spell WaterJet = new WaterJet().setColor(0.05f, 0.45f, 0.70f).setAffinity(AffinityType.Void);
	public static final Spell Dash = new Dash().setAffinity(AffinityType.Air);
	public static final Spell Gust = new Gust().setAffinity(AffinityType.Air);

}
