package faceless.artent.leveling.api;

import net.minecraft.item.ItemStack;

import java.util.List;

public interface ILeveledMob {
    int getBaseLevel();

    int getLevel();

    void setLevel(int level);

    int getLevelVariation();

    float getLevelAttackModifier();

    float getLevelSpeedModifier();

    float getLevelHealthModifier();

    float getLevelArmorModifier();

    void makeLevelDrops(List<ItemStack> drops);

    float getLevelMoneyDropModifier();

    float getXpModifier(int level); // TODO possibily remove
}
