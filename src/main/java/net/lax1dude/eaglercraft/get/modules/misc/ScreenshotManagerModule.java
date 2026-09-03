package net.lax1dude.eaglercraft.get.modules.misc;

import net.lax1dude.eaglercraft.get.Category;
import net.lax1dude.eaglercraft.get.gui.ScreenshotManagerScreen;
import net.lax1dude.eaglercraft.get.module.Module;
import net.minecraft.client.Minecraft;

public class ScreenshotManagerModule extends Module {

    public ScreenshotManagerModule() {
        super("Screenshot Manager", "Browse and delete saved screenshots", Category.MISC, false, -1);
    }

    @Override
    protected void onEnable() {
        Minecraft.getInstance().displayGuiScreen(new ScreenshotManagerScreen());
        setEnabled(false);
    }
}
