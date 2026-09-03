package net.lax1dude.eaglercraft.get.hud.elements;

import java.util.List;

import net.lax1dude.eaglercraft.get.hud.HudElement;
import net.lax1dude.eaglercraft.get.hud.HudManager;
import net.lax1dude.eaglercraft.get.util.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.ChatLine;

/** A clean-styled, draggable replacement for the vanilla chat box. Suppresses the vanilla one while enabled. */
public class ChatHud extends HudElement {

    public ChatHud() {
        super("Custom Chat", 0.01, 0.55, false);
    }

    public static boolean isSuppressingVanilla() {
        HudElement e = HudManager.getInstance().getByName("Custom Chat");
        return e != null && e.isEnabled();
    }

    @Override
    public void render(int screenWidth, int screenHeight, float partialTicks) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || mc.gameSettings.chatVisibility == net.minecraft.entity.player.ChatVisibility.HIDDEN) {
            return;
        }
        List<ChatLine> lines = mc.ingameGUI.getChatGUI().getRecentLines();
        int shown = Math.min(lines.size(), 10);
        if (shown == 0) {
            return;
        }
        int x = getPixelX(screenWidth);
        int y = getPixelY(screenHeight);
        int lineHeight = 9;
        int width = 320;
        int height = shown * lineHeight + 6;
        AbstractGui.fill(x, y, x + width, y + height, RenderUtil.PANEL_BG);
        for (int i = 0; i < shown; i++) {
            ChatLine line = lines.get(i);
            int ly = y + height - 6 - (i + 1) * lineHeight;
            mc.fontRenderer.drawStringWithShadow(line.getChatComponent().getFormattedText(), x + 4, ly, RenderUtil.TEXT);
        }
        setLastSize(width, height);
    }
}
