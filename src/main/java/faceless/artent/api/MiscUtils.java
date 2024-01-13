package faceless.artent.api;

public class MiscUtils {
    public static boolean isInRange(int a, int min, int max) {
        return a >= min && a <= max;
    }

    public static int clamp(int a, int min, int max) {
        return Math.max(min, Math.min(a, max));
    }
}
