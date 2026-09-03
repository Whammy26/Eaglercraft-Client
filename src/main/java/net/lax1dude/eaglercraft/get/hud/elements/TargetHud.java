package net.lax1dude.eaglercraft.get.hud.elements;

import net.lax1dude.eaglercraft.get.hud.HudElement;
import net.lax1dude.eaglercraft.get.util.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;

public class TargetHud extends HudElement {

    public TargetHud() {
        super("Target HUD", 0.35, 0.03, true);
    }

    @Override
    public void render(int screenWidth, int screenHeight, float partialTicks) {
        Minecraft mc = Minecraft.getInstance();
        Entity target = mc.pointedEntity;
        if (!(target instanceof LivingEntity) || mc.player == null) {
            return;
        }
        LivingEntity living = (LivingEntity) target;
        String name = target.getDisplayName().getFormattedText();
        float health = living.getHealth();
        float maxHealth = living.getMaxHealth();
        int armor = living instanceof PlayerEntity ? -1 : (int) living.getTotalArmorValue();

        int x = getPixelX(screenWidth);
        int y = getPixelY(screenHeight);
        String healthText = String.format("%.1f / %.1f HP", health, maxHealth);
        int width = Math.max(mc.fontRenderer.getStringWidth(name), mc.fontRenderer.getStringWidth(healthText)) + 10;
        int height = 24;
        RenderUtil.panel(x, y, width, height);
        mc.fontRenderer.drawStringWithShadow(name, x + 5, y + 3, RenderUtil.TEXT);
        float pct = maxHealth > 0 ? Math.max(0, health / maxHealth) : 0;
        int barColor = pct > 0.5F ? 0xFF3FD16B : pct > 0.25F ? 0xFFE0C23F : 0xFFD65F5F;
        net.minecraft.client.gui.AbstractGui.fill(x + 5, y + 14, x + width - 5, y + 17, 0x40FFFFFF);
        net.minecraft.client.gui.AbstractGui.fill(x + 5, y + 14, x + 5 + (int) ((width - 10) * pct), y + 17, barColor);
        mc.fontRenderer.drawStringWithShadow(healthText, x + 5, y + 18, RenderUtil.TEXT_DIM);
        setLastSize(width, height);
    }
}
