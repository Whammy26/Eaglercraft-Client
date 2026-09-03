package net.lax1dude.eaglercraft.get.modules.combat;

import net.lax1dude.eaglercraft.get.Category;
import net.lax1dude.eaglercraft.get.module.Module;
import net.lax1dude.eaglercraft.get.settings.SliderSetting;

/**
 * Exposes tunable speed/target values for the vanilla hold-C zoom (which already exists
 * and works); GameRenderer reads these instead of its hardcoded constants while enabled.
 */
public class ZoomModule extends Module {

    private final SliderSetting speed = new SliderSetting("Smoothing", "Zoom interpolation speed", 0.1, 0.02, 0.4, 0.01, false);
    private final SliderSetting defaultZoom = new SliderSetting("Zoom Level", "Default zoom amount (lower = closer)", 0.25, 0.05, 1.0, 0.05, false);

    public ZoomModule() {
        super("Zoom", "Smooth configurable zoom (hold C, scroll to adjust)", Category.COMBAT, true, -1);
        addSetting(speed);
        addSetting(defaultZoom);
    }

    public float getSpeed() {
        return speed.getFloat();
    }

    public float getDefaultZoom() {
        return defaultZoom.getFloat();
    }
}
