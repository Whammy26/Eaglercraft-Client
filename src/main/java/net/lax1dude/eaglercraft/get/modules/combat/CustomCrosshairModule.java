package net.lax1dude.eaglercraft.get.modules.combat;

import net.lax1dude.eaglercraft.get.Category;
import net.lax1dude.eaglercraft.get.module.Module;
import net.lax1dude.eaglercraft.get.settings.ColorSetting;
import net.lax1dude.eaglercraft.get.settings.SliderSetting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;

/** Draws its own crosshair; suppresses the vanilla one via a guard in IngameGui. */
public class CustomCrosshairModule extends Module {

    private final SliderSetting size = new SliderSetting("Size", "Arm length in pixels", 6, 2, 16, 1, true);
    private final SliderSetting gap = new SliderSetting("Gap", "Gap from the center in pixels", 2, 0, 10, 1, true);
    private final SliderSetting thickness = new SliderSetting("Thickness", "Line thickness in pixels", 2, 1, 6, 1, true);
    private final ColorSetting color = new ColorSetting("Color", "Crosshair color", 0xFFFFFFFF);

    public CustomCrosshairModule() {
        super("Custom Crosshair", "A fully configurable replacement crosshair", Category.COMBAT, false, -1);
        addSetting(size);
        addSetting(gap);
        addSetting(thickness);
        addSetting(color);
    }

    @Override
    public void onRender(float partialTicks) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || mc.currentScreen != null || mc.gameSettings.hideGUI) {
            return;
        }
        int cx = mc.mainWindow.getScaledWidth() / 2;
        int cy = mc.mainWindow.getScaledHeight() / 2;
        int s = size.getInt();
        int g = gap.getInt();
        int t = thickness.getInt();
        int c = color.get();
        AbstractGui.fill(cx - t / 2, cy - g - s, cx + t - t / 2, cy - g, c);
        AbstractGui.fill(cx - t / 2, cy + g, cx + t - t / 2, cy + g + s, c);
        AbstractGui.fill(cx - g - s, cy - t / 2, cx - g, cy + t - t / 2, c);
        AbstractGui.fill(cx + g, cy - t / 2, cx + g + s, cy + t - t / 2, c);
    }
}
