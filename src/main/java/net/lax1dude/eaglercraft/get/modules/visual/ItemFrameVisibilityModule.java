package net.lax1dude.eaglercraft.get.modules.visual;

import net.lax1dude.eaglercraft.get.Category;
import net.lax1dude.eaglercraft.get.module.Module;

/** Hides item frames entirely (patched into ItemFrameRenderer.doRender). */
public class ItemFrameVisibilityModule extends Module {

    public ItemFrameVisibilityModule() {
        super("Hide Item Frames", "Hides item frame entities for a cleaner view", Category.VISUAL, false, -1);
    }
}
