package net.lax1dude.eaglercraft.get.modules.visual;

import net.lax1dude.eaglercraft.get.Category;
import net.lax1dude.eaglercraft.get.module.Module;
import net.lax1dude.eaglercraft.get.settings.BooleanSetting;

/** Appends live health to entity name tags (patched into LivingRenderer.renderName). */
public class CustomNameTagsModule extends Module {

    private final BooleanSetting showHealth = new BooleanSetting("Show Health", "Append current health to name tags", true);

    public CustomNameTagsModule() {
        super("Custom Name Tags", "Shows extra info on player/mob name tags", Category.VISUAL, false, -1);
        addSetting(showHealth);
    }

    public boolean shouldShowHealth() {
        return showHealth.get();
    }
}
