package net.lax1dude.eaglercraft.get.module;

import java.util.ArrayList;
import java.util.List;

import net.lax1dude.eaglercraft.get.Category;
import net.lax1dude.eaglercraft.get.settings.Setting;

/**
 * Base class for a single toggleable client feature ("mod").
 * Subclasses hook into the game via onEnable/onDisable/onTick/onRender.
 */
public abstract class Module {

    private final String name;
    private final String description;
    private final Category category;
    private final List<Setting<?>> settings = new ArrayList<>();
    private boolean enabled;
    private int keyCode = -1;
    /** default enabled state, used by "reset to defaults" */
    private final boolean defaultEnabled;
    private final int defaultKeyCode;

    protected Module(String name, String description, Category category) {
        this(name, description, category, false, -1);
    }

    protected Module(String name, String description, Category category, boolean defaultEnabled) {
        this(name, description, category, defaultEnabled, -1);
    }

    protected Module(String name, String description, Category category, boolean defaultEnabled, int defaultKeyCode) {
        this.name = name;
        this.description = description;
        this.category = category;
        this.defaultEnabled = defaultEnabled;
        this.defaultKeyCode = defaultKeyCode;
        this.enabled = defaultEnabled;
        this.keyCode = defaultKeyCode;
    }

    protected void addSetting(Setting<?> setting) {
        settings.add(setting);
    }

    public List<Setting<?>> getSettings() {
        return settings;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Category getCategory() {
        return category;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public int getKeyCode() {
        return keyCode;
    }

    public void setKeyCode(int keyCode) {
        this.keyCode = keyCode;
    }

    public int getDefaultKeyCode() {
        return defaultKeyCode;
    }

    public boolean isDefaultEnabled() {
        return defaultEnabled;
    }

    public final void setEnabled(boolean value) {
        if (this.enabled == value) {
            return;
        }
        this.enabled = value;
        if (value) {
            onEnable();
        } else {
            onDisable();
        }
        ModuleManager.getInstance().onModuleStateChanged(this);
    }

    public final void toggle() {
        setEnabled(!enabled);
    }

    /** Called once when the module becomes active. */
    protected void onEnable() {
    }

    /** Called once when the module becomes inactive. */
    protected void onDisable() {
    }

    /** Called every client tick (20/s) while enabled. */
    public void onTick() {
    }

    /** Called every frame after the vanilla HUD has rendered, while enabled. */
    public void onRender(float partialTicks) {
    }

    public void resetToDefault() {
        keyCode = defaultKeyCode;
        setEnabled(defaultEnabled);
        for (Setting<?> s : settings) {
            s.resetToDefault();
        }
    }
}
