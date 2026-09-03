package net.lax1dude.eaglercraft.get.modules.combat;

import net.lax1dude.eaglercraft.get.Category;
import net.lax1dude.eaglercraft.get.module.Module;
import net.minecraft.client.Minecraft;

public class ToggleSprintModule extends Module {

    public ToggleSprintModule() {
        super("Toggle Sprint", "Sprints continuously without holding the sprint key", Category.COMBAT, false, -1);
    }

    @Override
    public void onTick() {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || mc.player.movementInput == null) {
            return;
        }
        boolean moving = mc.player.movementInput.moveForward > 0.0F;
        if (moving && !mc.player.isSprinting()) {
            mc.player.setSprinting(true);
        } else if (!moving && mc.player.isSprinting()) {
            mc.player.setSprinting(false);
        }
    }
}
