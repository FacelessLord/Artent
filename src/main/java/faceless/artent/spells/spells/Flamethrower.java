package faceless.artent.spells.spells;

import faceless.artent.spells.entity.SprayElementEntity;

public class Flamethrower extends SpraySpell {
    public Flamethrower() {
        super("flamethrower", SprayElementEntity.SprayElement.Fire, 5);
    }
}
