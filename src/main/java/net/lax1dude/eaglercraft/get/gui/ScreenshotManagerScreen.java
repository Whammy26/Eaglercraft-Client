package net.lax1dude.eaglercraft.get.gui;

import java.util.ArrayList;
import java.util.List;

import net.lax1dude.eaglercraft.internal.vfs2.VFile2;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.text.StringTextComponent;

/** Lists saved screenshots (PNG files in the game data folder) with a delete action per row. */
public class ScreenshotManagerScreen extends Screen {

    private static final int ROW_H = 14;
    private double scroll = 0;

    public ScreenshotManagerScreen() {
        super(new StringTextComponent("Screenshots"));
    }

    @Override
    protected void init() {
    }

    private List<VFile2> screenshots() {
        List<VFile2> result = new ArrayList<>();
        for (VFile2 f : Minecraft.getInstance().gameDir.listFiles(false)) {
            if (f.getName().toLowerCase().endsWith(".png")) {
                result.add(f);
            }
        }
        return result;
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
        renderBackground();
        int pw = Math.min(400, this.width - 40);
        int px = (this.width - pw) / 2;
        int py = 30;
        int ph = this.height - 60;
        AbstractGui.fill(px, py, px + pw, py + ph, 0xE0101216);
        this.font.drawStringWithShadow("Screenshots - Escape to close", px + 8, py + 6, 0xFFFFFFFF);

        List<VFile2> files = screenshots();
        int listY = py + 20;
        int y = listY - (int) scroll;
        for (VFile2 f : files) {
            if (y + ROW_H > listY && y < py + ph) {
                boolean hover = mouseX >= px && mouseX < px + pw - 40 && mouseY >= y && mouseY < y + ROW_H;
                AbstractGui.fill(px + 4, y, px + pw - 4, y + ROW_H - 1, hover ? 0x30FFFFFF : 0x00000000);
                this.font.drawStringWithShadow(f.getName(), px + 8, y + 3, 0xFFD0D0D8);
                AbstractGui.fill(px + pw - 36, y + 1, px + pw - 6, y + ROW_H - 2, 0xFF7A2A2A);
                this.font.drawStringWithShadow("Del", px + pw - 30, y + 3, 0xFFFFFFFF);
            }
            y += ROW_H;
        }
        if (files.isEmpty()) {
            this.font.drawStringWithShadow("No screenshots found", px + 8, listY + 4, 0xFF8A8A94);
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        int pw = Math.min(400, this.width - 40);
        int px = (this.width - pw) / 2;
        int py = 30;
        int listY = py + 20;
        List<VFile2> files = screenshots();
        int y = listY - (int) scroll;
        for (VFile2 f : files) {
            if (mouseY >= y && mouseY < y + ROW_H) {
                if (mouseX >= px + pw - 36 && mouseX < px + pw - 6) {
                    f.delete();
                }
                return true;
            }
            y += ROW_H;
        }
        return true;
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
        scroll = Math.max(0, scroll - delta * ROW_H);
        return true;
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
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
