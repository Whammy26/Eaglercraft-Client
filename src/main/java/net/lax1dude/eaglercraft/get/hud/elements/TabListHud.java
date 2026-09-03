package net.lax1dude.eaglercraft.get.hud.elements;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.lax1dude.eaglercraft.get.hud.HudElement;
import net.lax1dude.eaglercraft.get.hud.HudManager;
import net.lax1dude.eaglercraft.get.util.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.network.play.NetworkPlayerInfo;

/** A clean-styled replacement for the vanilla tab player list. When enabled, the vanilla one is suppressed. */
public class TabListHud extends HudElement {

    public TabListHud() {
        super("Custom Tablist", 0.32, 0.03, false);
    }

    public static boolean isSuppressingVanilla() {
        HudElement e = HudManager.getInstance().getByName("Custom Tablist");
        return e != null && e.isEnabled();
    }

    @Override
    public void render(int screenWidth, int screenHeight, float partialTicks) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || mc.player.connection == null || !mc.gameSettings.keyBindPlayerList.isKeyDown()) {
            return;
        }
        Collection<NetworkPlayerInfo> infos = mc.player.connection.getPlayerInfoMap();
        List<NetworkPlayerInfo> sorted = new ArrayList<>(infos);
        sorted.sort((a, b) -> a.getGameProfile().getName().compareToIgnoreCase(b.getGameProfile().getName()));

        int x = getPixelX(screenWidth);
        int y = getPixelY(screenHeight);
        int lineHeight = 10;
        int width = 40;
        for (NetworkPlayerInfo info : sorted) {
            width = Math.max(width, mc.fontRenderer.getStringWidth(info.getGameProfile().getName()) + 40);
        }
        int height = 16 + sorted.size() * lineHeight;
        RenderUtil.panel(x, y, width, height);
        RenderUtil.drawCentered(mc.fontRenderer, "Players (" + sorted.size() + ")", x + width / 2, y + 3, RenderUtil.ACCENT | 0xFF000000);
        int row = 0;
        for (NetworkPlayerInfo info : sorted) {
            int ly = y + 16 + row * lineHeight;
            mc.fontRenderer.drawStringWithShadow(info.getGameProfile().getName(), x + 4, ly, RenderUtil.TEXT);
            String ping = info.getResponseTime() + "ms";
            mc.fontRenderer.drawStringWithShadow(ping, x + width - mc.fontRenderer.getStringWidth(ping) - 4, ly, RenderUtil.TEXT_DIM);
            row++;
        }
        setLastSize(width, height);
    }
}
