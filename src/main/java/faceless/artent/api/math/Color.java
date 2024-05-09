package faceless.artent.api.math;

import faceless.artent.api.MiscUtils;

import java.util.Arrays;

public class Color {
    public static final Color White = new Color(255, 255, 255);
    public static final Color Black = new Color(0, 0, 0);
    public static final Color Gray = new Color(127, 127, 127);
    public static final Color Cobalt = new Color(40, 60, 255);
    public static final Color Mithril = new Color(95, 207, 215);
    public static final Color Red = new Color(194, 0, 0);
    public static final Color Yellow = new Color(247, 215, 0);
    public static final Color Green = new Color(0, 200, 0);
    public static final Color Cyan = new Color(0, 186, 255);
    public static final Color Blue = new Color(0, 68, 198);
    public static final Color LightBlue = new Color(88, 171, 255);
    public static final Color Purple = new Color(133, 0, 255);
    public static final Color Void = new Color(68, 57, 193);
    public static final Color Gold = new Color(255, 199, 62);
    public static final Color Orange = new Color(255, 186, 0);
    public static final Color Brown = new Color(147, 107, 0);
    public static final Color Pink = new Color(0, 186, 255).getOpposite();

    public int red;
    public int green;
    public int blue;
    public int alpha = 255;

    public Color() {
        this.red = 255;
        this.green = 255;
        this.blue = 255;
    }

    public Color(int red, int green, int blue) {
        this.red = red;
        this.green = green;
        this.blue = blue;
    }

    public Color(int red, int green, int blue, int alpha) {
        this.red = red;
        this.green = green;
        this.blue = blue;
        this.alpha = alpha;
    }

    public Color(double r, double g, double b) {
        this.red = (int) (r * 255);
        this.green = (int) (g * 255);
        this.blue = (int) (b * 255);
    }

    public Color(double r, double g, double b, double a) {
        this.red = (int) r * 255;
        this.green = (int) g * 255;
        this.blue = (int) b * 255;
        this.alpha = (int) a * 255;
    }

    public int toHex() {
        return (red << 8 | green) << 8 | blue;
    }

    public Color getOpposite() {
        return new Color(255 - this.red, 255 - this.green, 255 - this.blue);
    }

    public Color multiply(float mult) {
        return new Color(
          (int) MiscUtils.limit(red * mult, 0, 255f),
          (int) MiscUtils.limit(green * mult, 0, 255),
          (int) MiscUtils.limit(blue * mult, 0, 255));
    }

    public Color multiplyAlpha(float mult) {
        return new Color(
          (int) MiscUtils.limit(red * mult, 0, 255f),
          (int) MiscUtils.limit(green * mult, 0, 255),
          (int) MiscUtils.limit(blue * mult, 0, 255),
          (int) MiscUtils.limit(alpha * mult, 0, 255));
    }

    public Color add(Color c) {
        int red = MiscUtils.limit((c.red + this.red) / 2, 0, 255);
        int green = MiscUtils.limit((c.green + this.green) / 2, 0, 255);
        int blue = MiscUtils.limit((c.blue + this.blue) / 2, 0, 255);
        int alpha = MiscUtils.limit((c.alpha + this.alpha) / 2, 0, 255);

        return new Color(red, green, blue, alpha);
    }

    public Color addNoMixing(Color c) {
        int red = MiscUtils.limit(c.red + this.red, 0, 255);
        int green = MiscUtils.limit(c.green + this.green, 0, 255);
        int blue = MiscUtils.limit(c.blue + this.blue, 0, 255);
        int alpha = MiscUtils.limit(c.alpha + this.alpha, 0, 255);

        return new Color(red, green, blue, alpha);
    }

    public Color addAlpha(Color b) {
        return new Color(red + b.red, green + b.green, blue + b.blue, alpha + b.alpha);
    }

    @Override
    public String toString() {
        return this.red + "|" + this.green + "|" + this.blue;
    }

    public static Color fromString(String colorString) {
        var components = Arrays.stream(colorString.split("\\|")).map(Integer::parseInt).toList();
        return new Color(components.get(0), components.get(1), components.get(2));
    }

    public float getRedF() {
        return red / 255f;
    }

    public float getGreenF() {
        return green / 255f;
    }

    public float getBlueF() {
        return blue / 255f;
    }

    public int asInt() {
        return new java.awt.Color(red, green, blue, alpha).getRGB();
    }

    public Color withAlpha(int d) {
        return new Color(this.red, this.green, this.blue, d);
    }
}
