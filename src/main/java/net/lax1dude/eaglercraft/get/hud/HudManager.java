package net.lax1dude.eaglercraft.get.hud;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.lax1dude.eaglercraft.get.config.ConfigManager;
import net.minecraft.client.Minecraft;

public final class HudManager {

    private static final HudManager INSTANCE = new HudManager();

    public static HudManager getInstance() {
        return INSTANCE;
    }

    private final List<HudElement> elements = new ArrayList<>();

    private HudManager() {
    }

    public void register(HudElement element) {
        elements.add(element);
    }

    public List<HudElement> getElements() {
        return Collections.unmodifiableList(elements);
    }

    public HudElement getByName(String name) {
        for (HudElement e : elements) {
            if (e.getName().equalsIgnoreCase(name)) {
                return e;
            }
        }
        return null;
    }

    public void render(float partialTicks) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || mc.gameSettings.hideGUI) {
            return;
        }
        int w = mc.mainWindow.getScaledWidth();
        int h = mc.mainWindow.getScaledHeight();
        for (HudElement e : elements) {
            if (e.isEnabled()) {
                e.render(w, h, partialTicks);
            }
        }
    }

    public void resetAllPositions() {
        for (HudElement e : elements) {
            e.resetPosition();
        }
        ConfigManager.getInstance().markDirty();
    }
}
