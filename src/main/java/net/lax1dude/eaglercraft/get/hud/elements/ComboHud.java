package net.lax1dude.eaglercraft.get.hud.elements;

import net.lax1dude.eaglercraft.get.hud.HudElement;
import net.lax1dude.eaglercraft.get.util.ComboTracker;
import net.lax1dude.eaglercraft.get.util.RenderUtil;
import net.minecraft.client.Minecraft;

public class ComboHud extends HudElement {

    public ComboHud() {
        super("Combo Counter", 0.35, 0.9, false);
    }

    @Override
    public void render(int screenWidth, int screenHeight, float partialTicks) {
        int combo = ComboTracker.getCombo();
        if (combo <= 0) {
            return;
        }
        Minecraft mc = Minecraft.getInstance();
        String text = combo + " Hit Combo";
        int x = getPixelX(screenWidth);
        int y = getPixelY(screenHeight);
        int w = mc.fontRenderer.getStringWidth(text) + 8;
        RenderUtil.panel(x, y, w, 12);
        mc.fontRenderer.drawStringWithShadow(text, x + 4, y + 2, RenderUtil.ACCENT | 0xFF000000);
        setLastSize(w, 12);
    }
}
