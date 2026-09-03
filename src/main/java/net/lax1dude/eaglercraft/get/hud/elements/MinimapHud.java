package net.lax1dude.eaglercraft.get.hud.elements;

import java.util.List;

import net.lax1dude.eaglercraft.get.hud.HudElement;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.entity.player.PlayerEntity;

/** Simplified radar-style minimap: shows nearby players as dots relative to your facing. */
public class MinimapHud extends HudElement {

    private static final int RADIUS = 40;
    private static final double RANGE = 80.0;

    public MinimapHud() {
        super("Minimap", 0.88, 0.02, false);
    }

    @Override
    public void render(int screenWidth, int screenHeight, float partialTicks) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || mc.world == null) {
            return;
        }
        int cx = getPixelX(screenWidth) + RADIUS;
        int cy = getPixelY(screenHeight) + RADIUS;

        AbstractGui.fill(cx - RADIUS, cy - RADIUS, cx + RADIUS, cy + RADIUS, 0xA0101216);
        drawCircleOutline(cx, cy, RADIUS, 0x60FFFFFF);
        AbstractGui.fill(cx - 1, cy - 1, cx + 1, cy + 1, 0xFF4C8DFF);

        float yaw = mc.player.rotationYaw;
        List<net.minecraft.client.entity.player.AbstractClientPlayerEntity> players = mc.world.getPlayers();
        for (net.minecraft.client.entity.player.AbstractClientPlayerEntity p : players) {
            if (p == mc.player) {
                continue;
            }
            double dx = p.posX - mc.player.posX;
            double dz = p.posZ - mc.player.posZ;
            double dist = Math.sqrt(dx * dx + dz * dz);
            if (dist > RANGE) {
                continue;
            }
            double angle = Math.atan2(dz, dx) - Math.toRadians(yaw + 90);
            double px = Math.cos(angle) * (dist / RANGE) * RADIUS;
            double pz = Math.sin(angle) * (dist / RANGE) * RADIUS;
            int dotX = (int) (cx + px);
            int dotY = (int) (cy + pz);
            AbstractGui.fill(dotX - 1, dotY - 1, dotX + 2, dotY + 2, 0xFFFF5555);
        }
        setLastSize(RADIUS * 2, RADIUS * 2);
    }

    private void drawCircleOutline(int cx, int cy, int r, int color) {
        int segments = 32;
        for (int i = 0; i < segments; i++) {
            double a = (Math.PI * 2 * i) / segments;
            int x = (int) (cx + Math.cos(a) * r);
            int y = (int) (cy + Math.sin(a) * r);
            AbstractGui.fill(x, y, x + 1, y + 1, color);
        }
    }
}
