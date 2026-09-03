package net.lax1dude.eaglercraft.get.modules.performance;

import net.lax1dude.eaglercraft.get.Category;
import net.lax1dude.eaglercraft.get.module.Module;
import net.minecraft.client.Minecraft;

/**
 * Reduces render work while the window is unfocused (the same goal as "Dynamic FPS" mods),
 * implemented against this client's existing focus tracking instead of a new mod.
 */
public class DynamicFpsModule extends Module {

    private int savedLimitFramerate = 0;

    public DynamicFpsModule() {
        super("Dynamic FPS", "Caps the framerate heavily while the window is unfocused", Category.PERFORMANCE, false, -1);
    }

    @Override
    public void onTick() {
        Minecraft mc = Minecraft.getInstance();
        if (!mc.isGameFocused()) {
            if (mc.mainWindow.getLimitFramerate() != 10) {
                savedLimitFramerate = mc.mainWindow.getLimitFramerate();
                mc.mainWindow.setFramerateLimit(10);
            }
        } else if (savedLimitFramerate != 0 && mc.mainWindow.getLimitFramerate() == 10) {
            mc.mainWindow.setFramerateLimit(savedLimitFramerate);
            savedLimitFramerate = 0;
        }
    }

    @Override
    protected void onDisable() {
        Minecraft mc = Minecraft.getInstance();
        if (savedLimitFramerate != 0) {
            mc.mainWindow.setFramerateLimit(savedLimitFramerate);
            savedLimitFramerate = 0;
        }
    }
}
