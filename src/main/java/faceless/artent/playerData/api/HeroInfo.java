package faceless.artent.playerData.api;

import faceless.artent.network.ArtentServerHook;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;

public class HeroInfo {
    public HeroClass heroClass = HeroClass.NotSelected;
    public SkillElement element = SkillElement.NotSelected;

    private int level = 1;
    public int experience = 0;

    public void writeNbt(NbtCompound nbt) {
        var heroNbt = new NbtCompound();
        heroNbt.putString("class", this.heroClass.name());
        heroNbt.putString("element", this.element.name());
        heroNbt.putInt("level", level);
        heroNbt.putInt("experience", experience);

        nbt.put("hero", heroNbt);
    }

    public void readNbt(NbtCompound nbt) {
        if (!nbt.contains("hero"))
            return;

        var heroNbt = nbt.getCompound("hero");
        var heroClassString = heroNbt.getString("class");
        var elementString = heroNbt.getString("element");
        level = heroNbt.getInt("level");
        experience = heroNbt.getInt("experience");

        this.heroClass = heroClassString.isEmpty() ? HeroClass.NotSelected : HeroClass.valueOf(heroClassString);
        this.element = elementString.isEmpty() ? SkillElement.NotSelected : SkillElement.valueOf(elementString);
    }

    public int getExperienceToLevel(PlayerEntity player) {
        return level > 0 ? 40 * level + 10 * level * level : 1;
    }

    public void addExperience(PlayerEntity player, int experience) {
        this.experience += experience;
        var experienceToLevel = getExperienceToLevel(player);
        if (this.experience > experienceToLevel) {
            this.experience -= experienceToLevel;
            this.level += 1;
            if (!player.getWorld().isClient)
                ArtentServerHook.packetSyncPlayerData(player);
        }
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }
}
