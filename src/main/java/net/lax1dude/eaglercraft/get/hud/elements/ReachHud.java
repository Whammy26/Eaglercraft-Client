package net.lax1dude.eaglercraft.get.hud.elements;

import net.lax1dude.eaglercraft.get.hud.HudElement;
import net.lax1dude.eaglercraft.get.util.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;

public class ReachHud extends HudElement {

    public ReachHud() {
        super("Reach Display", 0.35, 0.94, false);
    }

    @Override
    public void render(int screenWidth, int screenHeight, float partialTicks) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) {
            return;
        }
        String text;
        if (mc.objectMouseOver != null && mc.objectMouseOver.getType() == RayTraceResult.Type.ENTITY) {
            EntityRayTraceResult result = (EntityRayTraceResult) mc.objectMouseOver;
            double dist = mc.player.getPositionVector().distanceTo(result.getEntity().getPositionVector());
            text = String.format("Reach: %.2f", dist);
        } else {
            text = String.format("Reach: %.1f (max)", mc.playerController.getBlockReachDistance());
        }
        int x = getPixelX(screenWidth);
        int y = getPixelY(screenHeight);
        int w = mc.fontRenderer.getStringWidth(text) + 8;
        RenderUtil.panel(x, y, w, 12);
        mc.fontRenderer.drawStringWithShadow(text, x + 4, y + 2, RenderUtil.TEXT);
        setLastSize(w, 12);
    }
}
