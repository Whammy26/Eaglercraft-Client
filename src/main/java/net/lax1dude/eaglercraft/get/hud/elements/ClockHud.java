package net.lax1dude.eaglercraft.get.hud.elements;

import net.lax1dude.eaglercraft.get.hud.HudElement;
import net.lax1dude.eaglercraft.get.util.RenderUtil;
import net.minecraft.client.Minecraft;

/** Real-world (UTC) wall clock, since the environment cannot rely on java.time/SimpleDateFormat. */
public class ClockHud extends HudElement {

    public ClockHud() {
        super("Clock", 0.9, 0.01, true);
    }

    @Override
    public void render(int screenWidth, int screenHeight, float partialTicks) {
        Minecraft mc = Minecraft.getInstance();
        long totalSeconds = (System.currentTimeMillis() / 1000L) % 86400L;
        long h = totalSeconds / 3600L;
        long m = (totalSeconds % 3600L) / 60L;
        long s = totalSeconds % 60L;
        String text = String.format("%02d:%02d:%02d UTC", h, m, s);
        int x = getPixelX(screenWidth);
        int y = getPixelY(screenHeight);
        int w = mc.fontRenderer.getStringWidth(text) + 8;
        RenderUtil.panel(x, y, w, 12);
        mc.fontRenderer.drawStringWithShadow(text, x + 4, y + 2, RenderUtil.TEXT);
        setLastSize(w, 12);
    }
}
