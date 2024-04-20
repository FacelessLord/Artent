package faceless.artent.spells.spells;

import faceless.artent.spells.entity.SprayElementEntity;

public class FireHurricane extends SpraySpell {
	public FireHurricane() {
		super("fire_hurricane", SprayElementEntity.SprayElement.Fire, 5);
		isHurricane = true;
	}
}
