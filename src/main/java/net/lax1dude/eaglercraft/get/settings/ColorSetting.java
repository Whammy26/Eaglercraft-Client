package net.lax1dude.eaglercraft.get.settings;

/**
 * ARGB color setting, stored as 0xAARRGGBB.
 */
public class ColorSetting extends Setting<Integer> {

    public ColorSetting(String name, String description, int defaultArgb) {
        super(name, description, defaultArgb);
    }

    public int get() {
        return value;
    }

    public void set(int argb) {
        value = argb;
    }

    public int getAlpha() {
        return (value >> 24) & 0xFF;
    }

    public int getRed() {
        return (value >> 16) & 0xFF;
    }

    public int getGreen() {
        return (value >> 8) & 0xFF;
    }

    public int getBlue() {
        return value & 0xFF;
    }

    public void setChannel(int alpha, int red, int green, int blue) {
        value = ((alpha & 0xFF) << 24) | ((red & 0xFF) << 16) | ((green & 0xFF) << 8) | (blue & 0xFF);
    }

    @Override
    public Object toJson() {
        return value;
    }

    @Override
    public void fromJson(Object json) {
        if (json instanceof Number) {
            value = ((Number) json).intValue();
        }
    }
}
