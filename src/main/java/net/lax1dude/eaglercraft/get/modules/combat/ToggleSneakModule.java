package net.lax1dude.eaglercraft.get.modules.combat;

import net.lax1dude.eaglercraft.get.Category;
import net.lax1dude.eaglercraft.get.module.Module;
import net.minecraft.client.Minecraft;

public class ToggleSneakModule extends Module {

    public ToggleSneakModule() {
        super("Toggle Sneak", "Sneaks continuously without holding the sneak key", Category.COMBAT, false, -1);
    }

    @Override
    public void onTick() {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player != null && mc.player.movementInput != null) {
            mc.player.movementInput.sneak = true;
        }
    }

    @Override
    protected void onDisable() {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player != null && mc.player.movementInput != null) {
            mc.player.movementInput.sneak = false;
        }
    }
}
