package net.lax1dude.eaglercraft.get.settings;

public class EnumSetting<T extends Enum<T>> extends Setting<T> {

    private final T[] values;

    public EnumSetting(String name, String description, T defaultValue, T[] values) {
        super(name, description, defaultValue);
        this.values = values;
    }

    public T get() {
        return value;
    }

    public T[] getValues() {
        return values;
    }

    public void cycle() {
        int idx = value.ordinal();
        int next = (idx + 1) % values.length;
        for (T t : values) {
            if (t.ordinal() == next) {
                value = t;
                return;
            }
        }
        value = values[0];
    }

    @Override
    public Object toJson() {
        return value.name();
    }

    @Override
    public void fromJson(Object json) {
        if (json instanceof String) {
            for (T t : values) {
                if (t.name().equals(json)) {
                    value = t;
                    return;
                }
            }
        }
    }
}
