package faceless.artent.brewing.api;

import faceless.artent.Artent;
import faceless.artent.api.math.Color;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.potion.Potion;

import java.util.Arrays;
import java.util.Objects;

public class AlchemicalPotion extends Potion {
	public String id;
	public Color color;
	public ArtentStatusEffect[] statusEffects;

	public AlchemicalPotion(String id, Color color, StatusEffectInstance... effects) {
		super(id, effects);
		this.id = Artent.MODID + "." + id;
		this.color = color;
		statusEffects = getArtentStatusEffects(effects);
	}

	private ArtentStatusEffect[] getArtentStatusEffects(StatusEffectInstance[] effects) {
		return Arrays.stream(effects)
			.map(StatusEffectInstance::getEffectType)
			.filter(e -> e instanceof ArtentStatusEffect)
			.map(e -> (ArtentStatusEffect) e)
			.toList()
			.toArray(ArtentStatusEffect[]::new);
	}

	@Override
	public String toString() {
		return "AlchemicalPotion(" + id + ')';
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		AlchemicalPotion that = (AlchemicalPotion) o;
		return id.equals(that.id) && color.equals(that.color);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, color);
	}
}