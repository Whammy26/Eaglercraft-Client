package net.lax1dude.eaglercraft.get.gui;

import java.util.List;

import net.lax1dude.eaglercraft.get.util.Waypoint;
import net.lax1dude.eaglercraft.get.util.WaypointManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.text.StringTextComponent;

/** Create/edit/delete waypoints; distance/direction are shown live in the WaypointHud. */
public class WaypointScreen extends Screen {

    private static final int ROW_H = 16;
    private String nameInput = "";

    public WaypointScreen() {
        super(new StringTextComponent("Waypoints"));
    }

    @Override
    protected void init() {
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
        renderBackground();
        int pw = Math.min(420, this.width - 40);
        int px = (this.width - pw) / 2;
        int py = 30;
        int ph = this.height - 60;
        AbstractGui.fill(px, py, px + pw, py + ph, 0xE0101216);
        this.font.drawStringWithShadow("Waypoints - Escape to close", px + 8, py + 6, 0xFFFFFFFF);

        this.font.drawStringWithShadow("New waypoint name: " + nameInput + "_", px + 8, py + 20, 0xFF8A8A94);
        AbstractGui.fill(px + 8, py + 32, px + pw - 8, py + 32 + 14, 0xFF2A5CDB);
        this.font.drawStringWithShadow("Add at my current position", px + 12, py + 35, 0xFFFFFFFF);

        List<Waypoint> waypoints = WaypointManager.getInstance().getWaypoints();
        int y = py + 54;
        for (Waypoint w : waypoints) {
            AbstractGui.fill(px + 8, y, px + pw - 8, y + ROW_H - 2, 0x20FFFFFF);
            AbstractGui.fill(px + 10, y + 4, px + 14, y + ROW_H - 6, 0xFF000000 | w.color);
            this.font.drawStringWithShadow(String.format("%s  (%.0f, %.0f, %.0f)", w.name, w.x, w.y, w.z), px + 20, y + 4, 0xFFE8E8E8);
            AbstractGui.fill(px + pw - 34, y + 2, px + pw - 10, y + ROW_H - 4, 0xFF7A2A2A);
            this.font.drawStringWithShadow("Del", px + pw - 30, y + 4, 0xFFFFFFFF);
            y += ROW_H;
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        int pw = Math.min(420, this.width - 40);
        int px = (this.width - pw) / 2;
        int py = 30;
        if (mouseY >= py + 32 && mouseY < py + 46 && mouseX >= px + 8 && mouseX < px + pw - 8) {
            Minecraft mc = Minecraft.getInstance();
            if (mc.player != null) {
                String name = nameInput.isEmpty() ? "Waypoint" : nameInput;
                WaypointManager.getInstance().add(new net.lax1dude.eaglercraft.get.util.Waypoint(name, mc.player.posX, mc.player.posY, mc.player.posZ, 0xFFDD55));
                nameInput = "";
            }
            return true;
        }
        List<Waypoint> waypoints = WaypointManager.getInstance().getWaypoints();
        int y = py + 54;
        for (Waypoint w : new java.util.ArrayList<>(waypoints)) {
            if (mouseY >= y && mouseY < y + ROW_H - 2) {
                if (mouseX >= px + pw - 34 && mouseX < px + pw - 10) {
                    WaypointManager.getInstance().remove(w);
                }
                return true;
            }
            y += ROW_H;
        }
        return true;
    }

    @Override
    public boolean charTyped(char typedChar, int keyCode) {
        nameInput += typedChar;
        return true;
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == 259 && !nameInput.isEmpty()) {
            nameInput = nameInput.substring(0, nameInput.length() - 1);
            return true;
        }
        if (keyCode == 256) {
            Minecraft.getInstance().displayGuiScreen(null);
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
