package net.lax1dude.eaglercraft.get.hud.elements;

import net.lax1dude.eaglercraft.get.hud.HudElement;
import net.lax1dude.eaglercraft.get.util.RenderUtil;
import net.lax1dude.eaglercraft.get.util.SessionTracker;
import net.minecraft.client.Minecraft;

public class SessionStatsHud extends HudElement {

    public SessionStatsHud() {
        super("Session Stats", 0.01, 0.8, false);
    }

    @Override
    public void render(int screenWidth, int screenHeight, float partialTicks) {
        Minecraft mc = Minecraft.getInstance();
        long secs = SessionTracker.getSessionSeconds();
        String time = String.format("%02d:%02d:%02d", secs / 3600, (secs % 3600) / 60, secs % 60);
        String line1 = "Session: " + time;
        String line2 = String.format("Distance: %.0fm  Kills: %d", SessionTracker.getDistanceTraveled(), SessionTracker.getKills());
        int x = getPixelX(screenWidth);
        int y = getPixelY(screenHeight);
        int w = Math.max(mc.fontRenderer.getStringWidth(line1), mc.fontRenderer.getStringWidth(line2)) + 8;
        RenderUtil.panel(x, y, w, 24);
        mc.fontRenderer.drawStringWithShadow(line1, x + 4, y + 2, RenderUtil.TEXT);
        mc.fontRenderer.drawStringWithShadow(line2, x + 4, y + 13, RenderUtil.TEXT_DIM);
        setLastSize(w, 24);
    }
}
