package faceless.artent.spells.api;

import net.minecraft.util.math.Vec3d;

import java.util.UUID;

public interface ICaster {
    float getHealthProportion();

    int getMana();

    Spell getCurrentSpell();

    boolean consumeMana(int mana);

    void restoreMana();

    int getPotency();

    UUID getCasterUuid();

    Vec3d getCasterRotation();

    Vec3d getCasterPosition();

    void setCooldown(int time);

    int getCooldown();
}
