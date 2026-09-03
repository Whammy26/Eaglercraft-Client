package net.lax1dude.eaglercraft.get.hud.elements;

import net.lax1dude.eaglercraft.get.hud.HudElement;
import net.lax1dude.eaglercraft.get.util.RenderUtil;
import net.minecraft.client.Minecraft;

public class DirectionHud extends HudElement {

    public DirectionHud() {
        super("Direction", 0.01, 0.17, true);
    }

    @Override
    public void render(int screenWidth, int screenHeight, float partialTicks) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) {
            return;
        }
        float yaw = mc.player.rotationYaw % 360.0F;
        if (yaw < 0) {
            yaw += 360.0F;
        }
        String text = "Facing: " + RenderUtil.direction(yaw) + " (" + Math.round(yaw) + ")";
        int x = getPixelX(screenWidth);
        int y = getPixelY(screenHeight);
        int w = mc.fontRenderer.getStringWidth(text) + 8;
        RenderUtil.panel(x, y, w, 12);
        mc.fontRenderer.drawStringWithShadow(text, x + 4, y + 2, RenderUtil.TEXT);
        setLastSize(w, 12);
    }
}
