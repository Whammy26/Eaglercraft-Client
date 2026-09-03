package net.lax1dude.eaglercraft.get.hud.elements;

import net.lax1dude.eaglercraft.get.hud.HudElement;
import net.lax1dude.eaglercraft.get.util.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;

public class DurabilityHud extends HudElement {

    public DurabilityHud() {
        super("Item Durability", 0.4, 0.9, false);
    }

    @Override
    public void render(int screenWidth, int screenHeight, float partialTicks) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) {
            return;
        }
        ItemStack held = mc.player.getHeldItemMainhand();
        if (held.isEmpty() || !held.isDamageable()) {
            return;
        }
        int max = held.getMaxDamage();
        int used = held.getDamage();
        int remaining = max - used;
        float pct = remaining / (float) max;
        int x = getPixelX(screenWidth);
        int y = getPixelY(screenHeight);
        String text = remaining + " / " + max;
        int width = 60;
        RenderUtil.panel(x, y, width, 14);
        int barColor = pct > 0.5F ? 0xFF3FD16B : pct > 0.2F ? 0xFFE0C23F : 0xFFD65F5F;
        net.minecraft.client.gui.AbstractGui.fill(x + 3, y + 9, x + 3 + (int) ((width - 6) * pct), y + 11, barColor);
        mc.fontRenderer.drawStringWithShadow(text, x + 4, y + 2, RenderUtil.TEXT);
        setLastSize(width, 14);
    }
}
