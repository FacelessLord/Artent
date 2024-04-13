package faceless.artent.leveling.api;

import net.minecraft.item.ItemStack;

import java.util.List;

public interface ISpecialMob {

    SpecialMobType getSpecialMobType();
    void setSpecialMobType(SpecialMobType type);

    boolean hasSpecialMobModifier();

    float getSpecialAttackModifier();

    float getSpecialSpeedModifier();

    void makeSpecialDrops(List<ItemStack> drops);

    float getSpecialMoneyDropModifier();
}
