package net.lax1dude.eaglercraft.get.settings;

public class SliderSetting extends Setting<Double> {

    private final double min;
    private final double max;
    private final double step;
    private final boolean integer;

    public SliderSetting(String name, String description, double defaultValue, double min, double max, double step, boolean integer) {
        super(name, description, clamp(defaultValue, min, max));
        this.min = min;
        this.max = max;
        this.step = step;
        this.integer = integer;
    }

    private static double clamp(double v, double min, double max) {
        return Math.max(min, Math.min(max, v));
    }

    public double get() {
        return value;
    }

    public int getInt() {
        return (int) Math.round(value);
    }

    public float getFloat() {
        return (float) (double) value;
    }

    public double getMin() {
        return min;
    }

    public double getMax() {
        return max;
    }

    public double getStep() {
        return step;
    }

    public boolean isInteger() {
        return integer;
    }

    public void set(double v) {
        v = clamp(v, min, max);
        if (step > 0) {
            v = Math.round(v / step) * step;
        }
        value = clamp(v, min, max);
    }

    public String display() {
        return integer ? String.valueOf(getInt()) : String.format("%.2f", value);
    }

    @Override
    public Object toJson() {
        return value;
    }

    @Override
    public void fromJson(Object json) {
        if (json instanceof Number) {
            set(((Number) json).doubleValue());
        }
    }
}
