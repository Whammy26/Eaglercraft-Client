package net.lax1dude.eaglercraft.get.gui;

import java.util.HashMap;
import java.util.Map;

import net.lax1dude.eaglercraft.get.config.ConfigManager;
import net.lax1dude.eaglercraft.get.hud.HudElement;
import net.lax1dude.eaglercraft.get.hud.HudManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.text.StringTextComponent;

/** RIGHT SHIFT + Insert: drag any HUD element to reposition it, scroll to scale, right-click to toggle. */
public class HudEditorScreen extends Screen {

    private HudElement dragging = null;
    private double dragOffsetX, dragOffsetY;
    private HudElement hovered = null;

    public HudEditorScreen() {
        super(new StringTextComponent("HUD Editor"));
    }

    @Override
    protected void init() {
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
        Minecraft mc = Minecraft.getInstance();
        int w = mc.mainWindow.getScaledWidth();
        int h = mc.mainWindow.getScaledHeight();

        hovered = null;
        Map<HudElement, int[]> bounds = new HashMap<>();
        for (HudElement e : HudManager.getInstance().getElements()) {
            int x = e.getPixelX(w);
            int y = e.getPixelY(h);
            e.render(w, h, partialTicks);
            bounds.put(e, new int[]{x, y, e.getLastWidth(), e.getLastHeight()});
            if (mouseX >= x && mouseX <= x + e.getLastWidth() && mouseY >= y && mouseY <= y + e.getLastHeight()) {
                hovered = e;
            }
        }

        for (Map.Entry<HudElement, int[]> entry : bounds.entrySet()) {
            HudElement e = entry.getKey();
            int[] b = entry.getValue();
            int color = e == hovered ? 0xFF4C8DFF : e.isEnabled() ? 0x60FFFFFF : 0x60FF4040;
            AbstractGui.fill(b[0] - 1, b[1] - 1, b[0] + b[2] + 1, b[1], color);
            AbstractGui.fill(b[0] - 1, b[1] + b[3], b[0] + b[2] + 1, b[1] + b[3] + 1, color);
            AbstractGui.fill(b[0] - 1, b[1] - 1, b[0], b[1] + b[3] + 1, color);
            AbstractGui.fill(b[0] + b[2], b[1] - 1, b[0] + b[2] + 1, b[1] + b[3] + 1, color);
        }

        String hint = "Drag to move - Scroll to scale - Right click to enable/disable - Insert to close";
        this.font.drawStringWithShadow(hint, 6, h - 12, 0xFFFFFFFF);
        if (hovered != null) {
            this.font.drawStringWithShadow(hovered.getName() + " (" + String.format("%.2f", hovered.getScale()) + "x)", 6, h - 22, 0xFF4C8DFF);
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (hovered == null) {
            return true;
        }
        if (button == 1) {
            hovered.setEnabled(!hovered.isEnabled());
            ConfigManager.getInstance().markDirty();
            return true;
        }
        Minecraft mc = Minecraft.getInstance();
        int x = hovered.getPixelX(mc.mainWindow.getScaledWidth());
        int y = hovered.getPixelY(mc.mainWindow.getScaledHeight());
        dragging = hovered;
        dragOffsetX = mouseX - x;
        dragOffsetY = mouseY - y;
        return true;
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        if (dragging != null) {
            Minecraft mc = Minecraft.getInstance();
            int w = mc.mainWindow.getScaledWidth();
            int h = mc.mainWindow.getScaledHeight();
            double nx = (mouseX - dragOffsetX) / w;
            double ny = (mouseY - dragOffsetY) / h;
            dragging.setPosition(nx, ny);
            return true;
        }
        return super.mouseDragged(mouseX, mouseY, button, dragX, dragY);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (dragging != null) {
            dragging = null;
            ConfigManager.getInstance().markDirty();
        }
        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
        if (hovered != null) {
            hovered.setScale(hovered.getScale() + (float) delta * 0.05F);
            ConfigManager.getInstance().markDirty();
        }
        return true;
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == net.lax1dude.eaglercraft.get.GetClient.KEY_HUD_EDITOR || keyCode == 256) {
            Minecraft.getInstance().displayGuiScreen(null);
            return true;
        }
        if (keyCode == 261) {
            HudManager.getInstance().resetAllPositions();
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
