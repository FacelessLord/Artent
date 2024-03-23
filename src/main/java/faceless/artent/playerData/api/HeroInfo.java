package faceless.artent.playerData.api;

import net.minecraft.nbt.NbtCompound;

public class HeroInfo {
	public HeroClass heroClass = HeroClass.NotSelected;
	public SkillElement element = SkillElement.NotSelected;

	public void writeNbt(NbtCompound nbt) {
		var heroNbt = new NbtCompound();
		heroNbt.putString("class", this.heroClass.name());
		heroNbt.putString("element", this.element.name());

		nbt.put("hero", heroNbt);
	}

	public void readNbt(NbtCompound nbt) {
		if (!nbt.contains("hero"))
			return;

		var heroNbt = nbt.getCompound("hero");
		var heroClassString = heroNbt.getString("class");
		var elementString = heroNbt.getString("element");

		this.heroClass = heroClassString.isEmpty() ? HeroClass.NotSelected : HeroClass.valueOf(heroClassString);
		this.element = elementString.isEmpty() ? SkillElement.NotSelected : SkillElement.valueOf(elementString);
	}
}
