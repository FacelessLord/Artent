package faceless.artent.api;

public class Color {
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

    public Color multiply(float mult) {
        return new Color((int) (red * mult), (int) (green * mult), (int) (blue * mult));
    }

    public Color multiplyAlpha(float mult) {
        return new Color((int) (red * mult), (int) (green * mult), (int) (blue * mult), (int) (alpha * mult));
    }

    public Color add(Color b) {
        return new Color(red + b.red, green + b.green, blue + b.blue);
    }

    public Color addAlpha(Color b) {
        return new Color(red + b.red, green + b.green, blue + b.blue, alpha + b.alpha);
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
}
