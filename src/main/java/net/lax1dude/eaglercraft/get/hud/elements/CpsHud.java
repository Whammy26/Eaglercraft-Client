package net.lax1dude.eaglercraft.get.hud.elements;

import net.lax1dude.eaglercraft.get.hud.HudElement;
import net.lax1dude.eaglercraft.get.util.ClickTracker;
import net.lax1dude.eaglercraft.get.util.RenderUtil;
import net.minecraft.client.Minecraft;

public class CpsHud extends HudElement {

    public CpsHud() {
        super("CPS", 0.02, 0.94, true);
    }

    @Override
    public void render(int screenWidth, int screenHeight, float partialTicks) {
        Minecraft mc = Minecraft.getInstance();
        String text = "CPS: " + ClickTracker.getLeftCps() + " / " + ClickTracker.getRightCps();
        int x = getPixelX(screenWidth);
        int y = getPixelY(screenHeight);
        int w = mc.fontRenderer.getStringWidth(text) + 8;
        RenderUtil.panel(x, y, w, 12);
        mc.fontRenderer.drawStringWithShadow(text, x + 4, y + 2, RenderUtil.TEXT);
        setLastSize(w, 12);
    }
}
