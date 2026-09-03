package net.lax1dude.eaglercraft.get.hud.elements;

import net.lax1dude.eaglercraft.get.hud.HudElement;
import net.lax1dude.eaglercraft.get.util.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.play.NetworkPlayerInfo;

public class PingHud extends HudElement {

    public PingHud() {
        super("Ping", 0.01, 0.05, true);
    }

    @Override
    public void render(int screenWidth, int screenHeight, float partialTicks) {
        Minecraft mc = Minecraft.getInstance();
        int ping = 0;
        if (mc.player != null && mc.player.connection != null) {
            NetworkPlayerInfo info = mc.player.connection.getPlayerInfo(mc.player.getGameProfile().getName());
            if (info != null) {
                ping = info.getResponseTime();
            }
        }
        String text = "Ping: " + ping + "ms";
        int x = getPixelX(screenWidth);
        int y = getPixelY(screenHeight);
        int w = mc.fontRenderer.getStringWidth(text) + 8;
        RenderUtil.panel(x, y, w, 12);
        mc.fontRenderer.drawStringWithShadow(text, x + 4, y + 2, RenderUtil.TEXT);
        setLastSize(w, 12);
    }
}
