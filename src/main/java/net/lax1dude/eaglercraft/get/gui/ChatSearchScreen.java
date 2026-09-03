package net.lax1dude.eaglercraft.get.gui;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.ChatLine;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.text.StringTextComponent;

/** Lists recent chat lines and filters them live as you type. */
public class ChatSearchScreen extends Screen {

    private String query = "";

    public ChatSearchScreen() {
        super(new StringTextComponent("Chat Search"));
    }

    @Override
    protected void init() {
    }

    private List<ChatLine> matches() {
        List<ChatLine> result = new ArrayList<>();
        for (ChatLine line : Minecraft.getInstance().ingameGUI.getChatGUI().getRecentLines()) {
            if (query.isEmpty() || line.getChatComponent().getFormattedText().toLowerCase().contains(query.toLowerCase())) {
                result.add(line);
            }
        }
        return result;
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
        renderBackground();
        int pw = Math.min(500, this.width - 40);
        int px = (this.width - pw) / 2;
        int py = 30;
        int ph = this.height - 60;
        AbstractGui.fill(px, py, px + pw, py + ph, 0xE0101216);
        this.font.drawStringWithShadow("Chat Search: " + query + "_", px + 8, py + 8, 0xFFFFFFFF);
        this.font.drawStringWithShadow("Type to filter, Enter/Escape to close", px + 8, py + 20, 0xFF8A8A94);

        List<ChatLine> matches = matches();
        int ly = py + 36;
        for (int i = matches.size() - 1; i >= 0 && ly < py + ph - 10; i--) {
            this.font.drawStringWithShadow(matches.get(i).getChatComponent().getFormattedText(), px + 8, ly, 0xFFD0D0D8);
            ly += 10;
        }
    }

    @Override
    public boolean charTyped(char typedChar, int keyCode) {
        query += typedChar;
        return true;
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == 259 && !query.isEmpty()) {
            query = query.substring(0, query.length() - 1);
            return true;
        }
        if (keyCode == 256 || keyCode == 257) {
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
