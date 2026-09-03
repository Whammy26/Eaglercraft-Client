package net.lax1dude.eaglercraft.get.hud.elements;

import net.lax1dude.eaglercraft.get.hud.HudElement;
import net.lax1dude.eaglercraft.get.util.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;

public class HeldItemHud extends HudElement {

    public HeldItemHud() {
        super("Held Item Info", 0.4, 0.85, false);
    }

    @Override
    public void render(int screenWidth, int screenHeight, float partialTicks) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) {
            return;
        }
        ItemStack held = mc.player.getHeldItemMainhand();
        if (held.isEmpty()) {
            return;
        }
        String text = held.getDisplayName().getFormattedText() + (held.getCount() > 1 ? " x" + held.getCount() : "");
        int x = getPixelX(screenWidth);
        int y = getPixelY(screenHeight);
        int w = mc.fontRenderer.getStringWidth(text) + 24;
        RenderUtil.panel(x, y, w, 18);
        mc.getItemRenderer().renderItemAndEffectIntoGUI(held, x + 1, y + 1);
        mc.fontRenderer.drawStringWithShadow(text, x + 20, y + 5, RenderUtil.TEXT);
        setLastSize(w, 18);
    }
}
