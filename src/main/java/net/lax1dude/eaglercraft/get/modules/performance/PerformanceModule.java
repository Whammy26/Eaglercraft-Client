package net.lax1dude.eaglercraft.get.modules.performance;

import net.lax1dude.eaglercraft.get.Category;
import net.lax1dude.eaglercraft.get.module.Module;
import net.lax1dude.eaglercraft.get.settings.SliderSetting;
import net.minecraft.client.Minecraft;

/**
 * Exposes the client's existing performance knobs (particle limit, entity/tile-entity render
 * distance, fast rendering) through the ClickGUI instead of duplicating a chunk-rendering
 * engine the way Sodium/Lithium/etc do - those are Fabric mods and are architecturally
 * incompatible with this monolithic Eaglercraft build.
 */
public class PerformanceModule extends Module {

    private final SliderSetting particleLimit = new SliderSetting("Particle Limit", "Maximum particles rendered at once", 8192, 0, 16384, 256, true);
    private final SliderSetting entityRenderDist = new SliderSetting("Entity Render Distance", "Multiplier for entity culling distance", 32, 4, 64, 1, true);

    private double savedParticleLimit;
    private double savedEntityRenderDistMul;

    public PerformanceModule() {
        super("Performance Mode", "Tunes the client's built-in particle/entity culling for extra FPS", Category.PERFORMANCE, false, -1);
        addSetting(particleLimit);
        addSetting(entityRenderDist);
    }

    @Override
    protected void onEnable() {
        Minecraft mc = Minecraft.getInstance();
        savedParticleLimit = mc.gameSettings.particleLimit;
        savedEntityRenderDistMul = mc.gameSettings.entityRenderDistMul;
        apply();
    }

    @Override
    public void onTick() {
        if (isEnabled()) {
            apply();
        }
    }

    private void apply() {
        Minecraft mc = Minecraft.getInstance();
        mc.gameSettings.particleLimit = particleLimit.getInt();
        mc.gameSettings.entityRenderDistMul = entityRenderDist.get();
        mc.gameSettings.fastEntityRender = true;
        mc.gameSettings.fastTileEntityRender = true;
        mc.gameSettings.reduceParticles = true;
    }

    @Override
    protected void onDisable() {
        Minecraft mc = Minecraft.getInstance();
        mc.gameSettings.particleLimit = (int) savedParticleLimit;
        mc.gameSettings.entityRenderDistMul = savedEntityRenderDistMul;
    }
}
