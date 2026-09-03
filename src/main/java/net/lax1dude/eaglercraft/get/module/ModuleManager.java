package net.lax1dude.eaglercraft.get.module;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.lax1dude.eaglercraft.get.Category;
import net.lax1dude.eaglercraft.get.config.ConfigManager;
import net.lax1dude.eaglercraft.get.notification.NotificationManager;
import net.lax1dude.eaglercraft.get.notification.NotificationType;

/**
 * Central registry that owns every {@link Module} instance and dispatches
 * tick/render/keybind events to whichever ones are currently enabled.
 */
public final class ModuleManager {

    private static final ModuleManager INSTANCE = new ModuleManager();

    public static ModuleManager getInstance() {
        return INSTANCE;
    }

    private final List<Module> modules = new ArrayList<>();

    private ModuleManager() {
    }

    public void register(Module module) {
        modules.add(module);
    }

    public List<Module> getModules() {
        return Collections.unmodifiableList(modules);
    }

    public List<Module> getModules(Category category) {
        List<Module> result = new ArrayList<>();
        for (Module m : modules) {
            if (m.getCategory() == category) {
                result.add(m);
            }
        }
        return result;
    }

    @SuppressWarnings("unchecked")
    public <T extends Module> T get(Class<T> type) {
        for (Module m : modules) {
            if (type.isInstance(m)) {
                return (T) m;
            }
        }
        return null;
    }

    public Module getByName(String name) {
        for (Module m : modules) {
            if (m.getName().equalsIgnoreCase(name)) {
                return m;
            }
        }
        return null;
    }

    public void onTick() {
        for (Module m : modules) {
            if (m.isEnabled()) {
                try {
                    m.onTick();
                } catch (Exception e) {
                    NotificationManager.getInstance().notify(m.getName() + " error: " + e.getMessage(), NotificationType.ERROR);
                }
            }
        }
    }

    public void onRender(float partialTicks) {
        for (Module m : modules) {
            if (m.isEnabled()) {
                try {
                    m.onRender(partialTicks);
                } catch (Exception e) {
                    NotificationManager.getInstance().notify(m.getName() + " render error: " + e.getMessage(), NotificationType.ERROR);
                }
            }
        }
    }

    /** Handles a raw key press for module keybinds (only called while no screen is open). */
    public boolean onKeyPress(int keyCode) {
        boolean handled = false;
        for (Module m : modules) {
            if (m.getKeyCode() == keyCode && keyCode != -1) {
                m.toggle();
                handled = true;
            }
        }
        return handled;
    }

    /** Called by {@link Module#setEnabled(boolean)} whenever a module is toggled. */
    public void onModuleStateChanged(Module module) {
        NotificationManager.getInstance().notify(
                module.getName() + (module.isEnabled() ? " enabled" : " disabled"),
                module.isEnabled() ? NotificationType.SUCCESS : NotificationType.INFO);
        ConfigManager.getInstance().markDirty();
    }

    /**
     * Returns true if the given keyCode is already used by another module (for conflict
     * detection in the ClickGUI keybind editor).
     */
    public Module findKeyConflict(int keyCode, Module excluding) {
        if (keyCode == -1) {
            return null;
        }
        for (Module m : modules) {
            if (m != excluding && m.getKeyCode() == keyCode) {
                return m;
            }
        }
        return null;
    }
}
