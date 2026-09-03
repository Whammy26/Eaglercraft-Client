package net.lax1dude.eaglercraft.get.hud.elements;

import java.util.List;

import net.lax1dude.eaglercraft.get.hud.HudElement;
import net.lax1dude.eaglercraft.get.util.RenderUtil;
import net.lax1dude.eaglercraft.get.util.Waypoint;
import net.lax1dude.eaglercraft.get.util.WaypointManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.util.math.MathHelper;

public class WaypointHud extends HudElement {

    public WaypointHud() {
        super("Waypoints HUD", 0.82, 0.55, false);
    }

    @Override
    public void render(int screenWidth, int screenHeight, float partialTicks) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) {
            return;
        }
        List<Waypoint> waypoints = WaypointManager.getInstance().getWaypoints();
        if (waypoints.isEmpty()) {
            return;
        }
        int x = getPixelX(screenWidth);
        int y = getPixelY(screenHeight);
        int lineHeight = 11;
        int width = 130;
        int height = 16 + waypoints.size() * lineHeight;
        RenderUtil.panel(x, y, width, height);
        RenderUtil.drawCentered(mc.fontRenderer, "Waypoints", x + width / 2, y + 3, RenderUtil.ACCENT | 0xFF000000);
        int row = 0;
        for (Waypoint w : waypoints) {
            double dx = w.x - mc.player.posX;
            double dz = w.z - mc.player.posZ;
            double dist = Math.sqrt(dx * dx + dz * dz);
            float angleToTarget = (float) (MathHelper.atan2(dz, dx) * (180.0 / Math.PI)) - 90.0F;
            String dir = RenderUtil.direction((angleToTarget + 360.0F) % 360.0F);
            String text = String.format("%s %s %.0fm", w.name, dir, dist);
            int ly = y + 16 + row * lineHeight;
            AbstractGui.fill(x + 3, ly + 1, x + 6, ly + 8, 0xFF000000 | w.color);
            mc.fontRenderer.drawStringWithShadow(text, x + 9, ly, RenderUtil.TEXT);
            row++;
        }
        setLastSize(width, height);
    }
}
