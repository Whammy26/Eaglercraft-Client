package net.lax1dude.eaglercraft.get.modules.misc;

import net.lax1dude.eaglercraft.get.Category;
import net.lax1dude.eaglercraft.get.module.Module;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;

/** Type while any container is open to highlight matching item slots (patched into ContainerScreen). */
public class ItemSearchModule extends Module {

    private String query = "";

    public ItemSearchModule() {
        super("Item Search", "Type to highlight matching items while a container is open", Category.MISC, false, -1);
    }

    public String getQuery() {
        return query;
    }

    public boolean handleChar(char c) {
        if (!isEnabled()) {
            return false;
        }
        query += c;
        return true;
    }

    public boolean handleBackspace() {
        if (!isEnabled() || query.isEmpty()) {
            return false;
        }
        query = query.substring(0, query.length() - 1);
        return true;
    }

    public void clear() {
        query = "";
    }

    public void renderOverlay(int guiLeft, int guiTop, FontRenderer font, Container container) {
        if (!isEnabled()) {
            return;
        }
        String text = "Search: " + query;
        AbstractGui.fill(guiLeft, guiTop - 14, guiLeft + font.getStringWidth(text) + 8, guiTop - 2, 0xC0101216);
        font.drawStringWithShadow(text, guiLeft + 4, guiTop - 12, 0xFFE8E8E8);
        if (query.isEmpty()) {
            return;
        }
        String q = query.toLowerCase();
        for (Slot slot : container.inventorySlots) {
            if (!slot.getStack().isEmpty() && slot.getStack().getDisplayName().getFormattedText().toLowerCase().contains(q)) {
                int sx = guiLeft + slot.xPos;
                int sy = guiTop + slot.yPos;
                AbstractGui.fill(sx - 1, sy - 1, sx + 17, sy + 17, 0x6000FF66);
            }
        }
    }
}
