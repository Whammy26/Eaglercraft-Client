package net.lax1dude.eaglercraft.get.modules.combat;

import net.lax1dude.eaglercraft.get.Category;
import net.lax1dude.eaglercraft.get.module.Module;
import net.lax1dude.eaglercraft.get.notification.NotificationManager;
import net.lax1dude.eaglercraft.get.notification.NotificationType;
import net.lax1dude.eaglercraft.get.settings.ColorSetting;
import net.lax1dude.eaglercraft.get.settings.SliderSetting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;

public class LowHealthWarningModule extends Module {

    private final SliderSetting threshold = new SliderSetting("Threshold", "Health value that triggers the warning", 6, 1, 20, 1, true);
    private final ColorSetting color = new ColorSetting("Color", "Warning tint color", 0x50FF0000);

    private boolean wasBelow = false;

    public LowHealthWarningModule() {
        super("Low Health Warning", "Flashes the screen and notifies you below a health threshold", Category.COMBAT, true, -1);
        addSetting(threshold);
        addSetting(color);
    }

    @Override
    public void onTick() {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) {
            return;
        }
        boolean below = mc.player.getHealth() > 0 && mc.player.getHealth() <= threshold.getFloat();
        if (below && !wasBelow) {
            NotificationManager.getInstance().notify("Low health: " + (int) mc.player.getHealth() + " HP", NotificationType.ERROR);
        }
        wasBelow = below;
    }

    @Override
    public void onRender(float partialTicks) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || mc.player.getHealth() > threshold.getFloat()) {
            return;
        }
        int w = mc.mainWindow.getScaledWidth();
        int h = mc.mainWindow.getScaledHeight();
        int border = 6;
        int c = color.get();
        AbstractGui.fill(0, 0, w, border, c);
        AbstractGui.fill(0, h - border, w, h, c);
        AbstractGui.fill(0, 0, border, h, c);
        AbstractGui.fill(w - border, 0, w, h, c);
    }
}
