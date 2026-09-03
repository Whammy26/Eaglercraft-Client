package net.lax1dude.eaglercraft.get.hud.elements;

import net.lax1dude.eaglercraft.get.hud.HudElement;
import net.lax1dude.eaglercraft.get.util.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;

public class KeystrokesHud extends HudElement {

    public KeystrokesHud() {
        super("Keystrokes", 0.02, 0.62, true);
    }

    @Override
    public void render(int screenWidth, int screenHeight, float partialTicks) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.gameSettings == null) {
            return;
        }
        int x = getPixelX(screenWidth);
        int y = getPixelY(screenHeight);
        int key = 18;

        drawKey(mc, x + key, y, key, key, "W", mc.gameSettings.keyBindForward.isKeyDown());
        drawKey(mc, x, y + key, key, key, "A", mc.gameSettings.keyBindLeft.isKeyDown());
        drawKey(mc, x + key, y + key, key, key, "S", mc.gameSettings.keyBindBack.isKeyDown());
        drawKey(mc, x + key * 2, y + key, key, key, "D", mc.gameSettings.keyBindRight.isKeyDown());
        drawKey(mc, x, y + key * 2, key, 12, "MB1", mc.gameSettings.keyBindAttack.isKeyDown());
        drawKey(mc, x + key * 2 - 6, y + key * 2, key + 12, 12, "MB2", mc.gameSettings.keyBindUseItem.isKeyDown());

        setLastSize(key * 3, key * 2 + 12);
    }

    private void drawKey(Minecraft mc, int x, int y, int w, int h, String label, boolean pressed) {
        int bg = pressed ? RenderUtil.ACCENT & 0xC0FFFFFF : RenderUtil.PANEL_BG;
        AbstractGui.fill(x, y, x + w, y + h, bg);
        AbstractGui.fill(x, y, x + w, y + 1, RenderUtil.PANEL_BORDER);
        AbstractGui.fill(x, y + h - 1, x + w, y + h, RenderUtil.PANEL_BORDER);
        AbstractGui.fill(x, y, x + 1, y + h, RenderUtil.PANEL_BORDER);
        AbstractGui.fill(x + w - 1, y, x + w, y + h, RenderUtil.PANEL_BORDER);
        int tw = mc.fontRenderer.getStringWidth(label);
        mc.fontRenderer.drawStringWithShadow(label, x + (w - tw) / 2f, y + (h - 8) / 2f, pressed ? 0xFFFFFFFF : RenderUtil.TEXT_DIM);
    }
}
