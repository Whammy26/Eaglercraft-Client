package net.lax1dude.eaglercraft.get.hud;

import java.util.ArrayList;
import java.util.List;

import net.lax1dude.eaglercraft.get.settings.Setting;

/**
 * Base class for a single draggable HUD widget. Position is stored as a fraction
 * (0..1) of the screen so it stays correct across resolutions/GUI scales.
 */
public abstract class HudElement {

    private final String name;
    private final List<Setting<?>> settings = new ArrayList<>();
    private boolean enabled;
    private double x;
    private double y;
    private float scale = 1.0F;

    private final boolean defaultEnabled;
    private final double defaultX;
    private final double defaultY;
    private final float defaultScale = 1.0F;

    /** last measured size, updated every render(); used for drag hit-testing in the HUD editor */
    private int lastWidth = 60;
    private int lastHeight = 10;

    protected HudElement(String name, double defaultX, double defaultY, boolean defaultEnabled) {
        this.name = name;
        this.x = this.defaultX = defaultX;
        this.y = this.defaultY = defaultY;
        this.enabled = this.defaultEnabled = defaultEnabled;
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

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public void setPosition(double x, double y) {
        this.x = Math.max(0.0, Math.min(1.0, x));
        this.y = Math.max(0.0, Math.min(1.0, y));
    }

    public float getScale() {
        return scale;
    }

    public void setScale(float scale) {
        this.scale = Math.max(0.5F, Math.min(2.0F, scale));
    }

    public int getLastWidth() {
        return lastWidth;
    }

    public int getLastHeight() {
        return lastHeight;
    }

    protected void setLastSize(int w, int h) {
        this.lastWidth = w;
        this.lastHeight = h;
    }

    public int getPixelX(int screenWidth) {
        return (int) Math.round(x * screenWidth);
    }

    public int getPixelY(int screenHeight) {
        return (int) Math.round(y * screenHeight);
    }

    public void resetPosition() {
        x = defaultX;
        y = defaultY;
        scale = defaultScale;
        enabled = defaultEnabled;
        for (Setting<?> s : settings) {
            s.resetToDefault();
        }
    }

    public boolean isDefaultEnabled() {
        return defaultEnabled;
    }

    /** Renders the element's content at (screenWidth*x, screenHeight*y); must call setLastSize(). */
    public abstract void render(int screenWidth, int screenHeight, float partialTicks);
}
