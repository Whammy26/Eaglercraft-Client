package net.lax1dude.eaglercraft.get.util;

/** Small draw helpers shared by HUD elements and the ClickGUI to keep a consistent look. */
public final class RenderUtil {

    public static final int PANEL_BG = 0xB0101216;
    public static final int PANEL_BORDER = 0x30FFFFFF;
    public static final int ACCENT = 0xFF4C8DFF;
    public static final int TEXT = 0xFFE8E8E8;
    public static final int TEXT_DIM = 0xFFA0A0A8;

    private RenderUtil() {
    }

    public static void panel(int x, int y, int width, int height) {
        net.minecraft.client.gui.AbstractGui.fill(x, y, x + width, y + height, PANEL_BG);
        net.minecraft.client.gui.AbstractGui.fill(x, y, x + width, y + 1, PANEL_BORDER);
        net.minecraft.client.gui.AbstractGui.fill(x, y + height - 1, x + width, y + height, PANEL_BORDER);
        net.minecraft.client.gui.AbstractGui.fill(x, y, x + 1, y + height, PANEL_BORDER);
        net.minecraft.client.gui.AbstractGui.fill(x + width - 1, y, x + width, y + height, PANEL_BORDER);
    }

    public static String direction(float yaw) {
        String[] dirs = {"S", "SW", "W", "NW", "N", "NE", "E", "SE"};
        int idx = Math.round(yaw / 45.0F) & 7;
        return dirs[idx];
    }

    public static void drawCentered(net.minecraft.client.gui.FontRenderer font, String text, int centerX, int y, int color) {
        font.drawStringWithShadow(text, centerX - font.getStringWidth(text) / 2.0F, y, color);
    }
}
