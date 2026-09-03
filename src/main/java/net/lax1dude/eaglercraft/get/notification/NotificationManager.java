package net.lax1dude.eaglercraft.get.notification;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.FontRenderer;

/**
 * Renders small toast notifications stacked in the top-right corner of the screen.
 */
public final class NotificationManager {

    private static final NotificationManager INSTANCE = new NotificationManager();

    public static NotificationManager getInstance() {
        return INSTANCE;
    }

    private final List<Notification> active = new ArrayList<>();

    private NotificationManager() {
    }

    public void notify(String message, NotificationType type) {
        active.add(new Notification(message, type));
        if (active.size() > 6) {
            active.remove(0);
        }
    }

    public void render(float partialTicks) {
        Minecraft mc = Minecraft.getInstance();
        if (active.isEmpty() || mc.player == null) {
            return;
        }
        FontRenderer font = mc.fontRenderer;
        int screenWidth = mc.mainWindow.getScaledWidth();
        int y = 6;
        Iterator<Notification> it = active.iterator();
        while (it.hasNext()) {
            Notification n = it.next();
            if (n.isExpired()) {
                it.remove();
                continue;
            }
            float opacity = n.getOpacity();
            int alpha = (int) (opacity * 235);
            int textWidth = font.getStringWidth(n.getMessage());
            int width = textWidth + 20;
            int x = screenWidth - width - 6;
            int bg = (alpha << 24) | 0x14171C;
            int accent = (n.getType().getColor() & 0xFFFFFF) | (alpha << 24);
            AbstractGui.fill(x, y, x + width, y + 20, bg);
            AbstractGui.fill(x, y, x + 3, y + 20, accent);
            int textAlpha = Math.min(255, alpha + 20) << 24;
            font.drawStringWithShadow(n.getMessage(), x + 9, y + 6, 0xFFFFFF | textAlpha);
            y += 24;
        }
    }
}
