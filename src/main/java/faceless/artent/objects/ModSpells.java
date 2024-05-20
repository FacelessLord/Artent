package faceless.artent.objects;

import faceless.artent.spells.api.AffinityType;
import faceless.artent.spells.api.Spell;
import faceless.artent.spells.api.SpellSettings;
import faceless.artent.spells.spells.*;

public class ModSpells {
    public static final Spell MakeLight = new MakeLight(SpellSettings
                                                          .action(3)
                                                          .prepareTime(10)
                                                          .cooldown(10)
                                                          .color(1, 1, 0.5f)
                                                          .affinity(AffinityType.Light)
                                                          .build());
    public static final Spell LightSword = new LightSword(SpellSettings
                                                            .action(10)
                                                            .prepareTime(30)
                                                            .cooldown(10)
                                                            .color(1, 1, 0.5f)
                                                            .affinity(AffinityType.Light)
                                                            .build());
    public static final Spell Nox = new Nox(SpellSettings
                                              .action(1)
                                              .prepareTime(10)
                                              .cooldown(10)
                                              .color(0.17f, 0.03f, 0.38f)
                                              .affinity(AffinityType.Void)
                                              .build());
    public static final Spell GilgameshLightStorm = new GilgameshLightStorm(SpellSettings
                                                                              .tick(10, 10)
                                                                              .prepareTime(80)
                                                                              .cooldown(80)
                                                                              .color(1, 1, 0.5f)
                                                                              .affinity(AffinityType.Light)
                                                                              .build());
//    public static final Spell Wormhole = new WormHole().setColor(0.17f, 0.03f, 0.38f).setAffinity(AffinityType.Void);

    public static final SpellSettings.SpellSettingsBuilder sprayBuilder = SpellSettings
      .tick(3, 5)
      .prepareTime(10)
      .cooldown(20)
      .maxActionDistance(6);

    public static final Spell Flamethrower = new Flamethrower(sprayBuilder
                                                                .copy()
                                                                .color(0.70f, 0.05f, 0.05f)
                                                                .affinity(AffinityType.Fire)
                                                                .build());
    public static final Spell FireHurricane = new FireHurricane(sprayBuilder
                                                                  .copy()
                                                                  .color(0.70f, 0.05f, 0.05f)
                                                                  .affinity(AffinityType.Fire)
                                                                  .build());
    public static final Spell Coldthrower = new Coldthrower(sprayBuilder
                                                              .copy()
                                                              .color(0.52f, 0.78f, 0.84f)
                                                              .affinity(AffinityType.Cold)
                                                              .build());
    public static final Spell WaterJet = new WaterJet(sprayBuilder
                                                        .copy()
                                                        .color(0.05f, 0.45f, 0.70f)
                                                        .affinity(AffinityType.Water)
                                                        .build());
    public static final Spell Dash = new Dash(SpellSettings
                                                .action(5)
                                                .cooldown(10)
                                                .affinity(AffinityType.Air)
                                                .maxActionDistance(6)
                                                .prepareTime(20)
                                                .build());
    public static final Spell Gust = new Gust(SpellSettings
                                                .action(5)
                                                .cooldown(10)
                                                .affinity(AffinityType.Air)
                                                .maxActionDistance(6)
                                                .prepareTime(20)
                                                .build());

}
