package net.lax1dude.eaglercraft.get.modules.combat;

import net.lax1dude.eaglercraft.get.Category;
import net.lax1dude.eaglercraft.get.module.Module;
import net.lax1dude.eaglercraft.get.settings.ColorSetting;

/** Configurable tint applied to entities/the player while flashing from damage (patched into LivingRenderer). */
public class HitColorModule extends Module {

    private final ColorSetting color = new ColorSetting("Color", "Damage flash tint color", 0xFFFF0000);

    public HitColorModule() {
        super("Hit Color", "Configurable entity damage flash tint", Category.COMBAT, true, -1);
        addSetting(color);
    }

    public int getColor() {
        return color.get();
    }
}
