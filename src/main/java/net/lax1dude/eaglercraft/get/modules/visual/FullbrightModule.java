package net.lax1dude.eaglercraft.get.modules.visual;

import net.lax1dude.eaglercraft.get.Category;
import net.lax1dude.eaglercraft.get.module.Module;
import net.minecraft.client.Minecraft;

public class FullbrightModule extends Module {

    private double savedGamma;

    public FullbrightModule() {
        super("Fullbright", "Maximizes brightness so dark areas are fully visible", Category.VISUAL, false, -1);
    }

    @Override
    protected void onEnable() {
        Minecraft mc = Minecraft.getInstance();
        savedGamma = mc.gameSettings.gamma;
        mc.gameSettings.gamma = 16.0D;
    }

    @Override
    protected void onDisable() {
        Minecraft mc = Minecraft.getInstance();
        mc.gameSettings.gamma = savedGamma;
    }
}
