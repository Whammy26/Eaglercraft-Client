package net.lax1dude.eaglercraft.get.hud.elements;

import net.lax1dude.eaglercraft.get.hud.HudElement;
import net.lax1dude.eaglercraft.get.util.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.Vec3d;

public class SpeedHud extends HudElement {

    public SpeedHud() {
        super("Movement Speed", 0.01, 0.29, false);
    }

    @Override
    public void render(int screenWidth, int screenHeight, float partialTicks) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) {
            return;
        }
        Vec3d motion = mc.player.getMotion();
        double speed = Math.sqrt(motion.x * motion.x + motion.z * motion.z) * 20.0;
        String text = String.format("Speed: %.2f b/s", speed);
        int x = getPixelX(screenWidth);
        int y = getPixelY(screenHeight);
        int w = mc.fontRenderer.getStringWidth(text) + 8;
        RenderUtil.panel(x, y, w, 12);
        mc.fontRenderer.drawStringWithShadow(text, x + 4, y + 2, RenderUtil.TEXT);
        setLastSize(w, 12);
    }
}
