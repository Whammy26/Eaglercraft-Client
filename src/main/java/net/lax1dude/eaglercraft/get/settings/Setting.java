package net.lax1dude.eaglercraft.get.settings;

/**
 * Base class for a configurable module setting shown in the ClickGUI.
 */
public abstract class Setting<T> {

    private final String name;
    private final String description;
    protected final T defaultValue;
    protected T value;

    protected Setting(String name, String description, T defaultValue) {
        this.name = name;
        this.description = description;
        this.defaultValue = defaultValue;
        this.value = defaultValue;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    public T getDefaultValue() {
        return defaultValue;
    }

    public void resetToDefault() {
        this.value = defaultValue;
    }

    /**
     * Serializes this setting's value to a JSON-safe primitive/string for config persistence.
     */
    public abstract Object toJson();

    /**
     * Restores this setting's value from a previously serialized JSON primitive.
     */
    public abstract void fromJson(Object json);
}
