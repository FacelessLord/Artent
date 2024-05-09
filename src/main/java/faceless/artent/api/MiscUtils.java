package faceless.artent.api;

import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class MiscUtils {
    public static boolean isInRange(int a, int min, int max) {
        return a >= min && a <= max;
    }

    public static int clamp(int a, int min, int max) {
        return Math.max(min, Math.min(a, max));
    }

    public static final String FILLED_CIRCLE = "●";
    public static final String EMPTY_CIRCLE = "◯";

    /**
     * Puts number in the limits
     */
    public static int limit(int a, int min, int max) {
        if (a > max) {
            a = max;
        }
        if (a < min) {
            a = min;
        }
        return a;
    }

    /**
     * Puts number in the limits
     */
    public static float limit(float a, float min, float max) {
        if (a > max) {
            a = max;
        }
        if (a < min) {
            a = min;
        }
        return a;
    }

    public static void giveOrDropItem(World world, BlockPos pos, PlayerEntity player, ItemStack droppedStack) {
        if (!player.giveItemStack(droppedStack)) {
            var entityItem = new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), droppedStack);
            world.spawnEntity(entityItem);
        }
    }

    public static void setStatusEffectColor(StatusEffect effect, int color) {
        var clazz = StatusEffect.class;
        try {
            var field = clazz.getDeclaredField("color");
            field.setAccessible(true);
            field.setInt(effect, color);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
