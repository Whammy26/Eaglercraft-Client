package net.lax1dude.eaglercraft.get.modules.misc;

import net.lax1dude.eaglercraft.get.Category;
import net.lax1dude.eaglercraft.get.module.Module;
import net.minecraft.client.Minecraft;

public class AutoGgModule extends Module {

    private boolean sentThisDeath = false;

    public AutoGgModule() {
        super("Auto GG", "Automatically sends \"gg\" in chat when you die", Category.MISC, false, -1);
    }

    @Override
    public void onTick() {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) {
            return;
        }
        if (mc.player.getHealth() <= 0.0F) {
            if (!sentThisDeath) {
                mc.player.sendChatMessage("gg");
                sentThisDeath = true;
            }
        } else {
            sentThisDeath = false;
        }
    }
}
