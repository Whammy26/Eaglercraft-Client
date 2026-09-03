package net.lax1dude.eaglercraft.get.hud.elements;

import net.lax1dude.eaglercraft.get.hud.HudElement;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;

/** A clean, repositionable alternative to the vanilla attack cooldown indicator. */
public class AttackIndicatorHud extends HudElement {

    public AttackIndicatorHud() {
        super("Attack Indicator", 0.44, 0.94, false);
    }

    @Override
    public void render(int screenWidth, int screenHeight, float partialTicks) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) {
            return;
        }
        float strength = mc.player.getCooledAttackStrength(partialTicks);
        int x = getPixelX(screenWidth);
        int y = getPixelY(screenHeight);
        int width = 40;
        int height = 4;
        AbstractGui.fill(x, y, x + width, y + height, 0x60000000);
        int color = strength >= 1.0F ? 0xFF3FD16B : 0xFFE0C23F;
        AbstractGui.fill(x, y, x + (int) (width * strength), y + height, color);
        setLastSize(width, height);
    }
}
