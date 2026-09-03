package net.lax1dude.eaglercraft.get.hud.elements;

import net.lax1dude.eaglercraft.get.hud.HudElement;
import net.lax1dude.eaglercraft.get.util.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

public class ArmorHud extends HudElement {

    public ArmorHud() {
        super("Armor Status", 0.55, 0.9, true);
    }

    @Override
    public void render(int screenWidth, int screenHeight, float partialTicks) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) {
            return;
        }
        NonNullList<ItemStack> armor = mc.player.inventory.armorInventory;
        int x = getPixelX(screenWidth);
        int y = getPixelY(screenHeight);
        int slot = 18;
        int drawn = 0;
        for (int i = armor.size() - 1; i >= 0; i--) {
            ItemStack stack = armor.get(i);
            if (stack.isEmpty()) {
                continue;
            }
            int sx = x + drawn * slot;
            RenderUtil.panel(sx, y, 16, 16);
            mc.getItemRenderer().renderItemAndEffectIntoGUI(stack, sx, y);
            mc.getItemRenderer().renderItemOverlayIntoGUI(mc.fontRenderer, stack, sx, y, null);
            drawn++;
        }
        setLastSize(Math.max(1, drawn) * slot, 16);
    }
}
