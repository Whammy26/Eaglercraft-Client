package net.lax1dude.eaglercraft.get.hud.elements;

import java.util.Collection;

import net.lax1dude.eaglercraft.get.hud.HudElement;
import net.lax1dude.eaglercraft.get.util.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.potion.EffectInstance;

public class PotionHud extends HudElement {

    public PotionHud() {
        super("Potion Effects", 0.82, 0.35, false);
    }

    public static boolean isSuppressingVanilla() {
        HudElement e = net.lax1dude.eaglercraft.get.hud.HudManager.getInstance().getByName("Potion Effects");
        return e != null && e.isEnabled();
    }

    @Override
    public void render(int screenWidth, int screenHeight, float partialTicks) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) {
            return;
        }
        Collection<EffectInstance> effects = mc.player.getActivePotionEffects();
        if (effects.isEmpty()) {
            return;
        }
        int x = getPixelX(screenWidth);
        int y = getPixelY(screenHeight);
        int rowHeight = 14;
        int width = 0;
        int row = 0;
        for (EffectInstance instance : effects) {
            String name = net.minecraft.client.resources.I18n.format(instance.getPotion().getName());
            int level = instance.getAmplifier() + 1;
            String time = formatTime(instance.getDuration());
            String text = name + (level > 1 ? " " + toRoman(level) : "") + "  " + time;
            int color = instance.getPotion().isBeneficial() ? 0xFF3FD16B : 0xFFD65F5F;
            int rowY = y + row * rowHeight;
            RenderUtil.panel(x, rowY, mc.fontRenderer.getStringWidth(text) + 12, 12);
            net.minecraft.client.gui.AbstractGui.fill(x, rowY, x + 3, rowY + 12, color);
            mc.fontRenderer.drawStringWithShadow(text, x + 6, rowY + 2, RenderUtil.TEXT);
            width = Math.max(width, mc.fontRenderer.getStringWidth(text) + 12);
            row++;
        }
        setLastSize(width, row * rowHeight);
    }

    private static String formatTime(int ticks) {
        int seconds = ticks / 20;
        return String.format("%d:%02d", seconds / 60, seconds % 60);
    }

    private static String toRoman(int n) {
        String[] romans = {"I", "II", "III", "IV", "V", "VI", "VII", "VIII", "IX", "X"};
        return n >= 1 && n <= romans.length ? romans[n - 1] : String.valueOf(n);
    }
}
