package net.lax1dude.eaglercraft.get.config;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;

import net.lax1dude.eaglercraft.get.hud.HudElement;
import net.lax1dude.eaglercraft.get.hud.HudManager;
import net.lax1dude.eaglercraft.get.module.Module;
import net.lax1dude.eaglercraft.get.module.ModuleManager;
import net.lax1dude.eaglercraft.get.notification.NotificationManager;
import net.lax1dude.eaglercraft.get.notification.NotificationType;
import net.lax1dude.eaglercraft.get.settings.Setting;
import net.lax1dude.eaglercraft.internal.vfs2.VFile2;
import net.minecraft.client.Minecraft;

/**
 * Persists every module/HUD element's enabled state, keybind and settings as JSON
 * inside the Eaglercraft virtual filesystem, with per-profile and per-server files.
 */
public final class ConfigManager {

    private static final ConfigManager INSTANCE = new ConfigManager();

    public static ConfigManager getInstance() {
        return INSTANCE;
    }

    private final Gson gson = new GsonBuilder().create();
    private ConfigProfile currentProfile = ConfigProfile.PVP;
    private String activeServerKey = null;
    private boolean serverOverrideEnabled = false;

    private ConfigManager() {
    }

    public ConfigProfile getCurrentProfile() {
        return currentProfile;
    }

    private VFile2 baseDir() {
        return new VFile2(Minecraft.getInstance().gameDir, "getclient");
    }

    private VFile2 profileFile(ConfigProfile profile) {
        return new VFile2(baseDir(), "profile_" + profile.name().toLowerCase() + ".json");
    }

    private VFile2 serverFile(String serverKey) {
        return new VFile2(new VFile2(baseDir(), "servers"), sanitize(serverKey) + ".json");
    }

    private static String sanitize(String s) {
        return s.replaceAll("[^a-zA-Z0-9_.-]", "_");
    }

    /** Called once at startup after all modules/HUD elements are registered. */
    public void init() {
        load();
    }

    public void markDirty() {
        save();
    }

    public void save() {
        try {
            JsonObject root = new JsonObject();
            root.addProperty("profile", currentProfile.name());

            JsonArray modulesArr = new JsonArray();
            for (Module m : ModuleManager.getInstance().getModules()) {
                JsonObject mo = new JsonObject();
                mo.addProperty("name", m.getName());
                mo.addProperty("enabled", m.isEnabled());
                mo.addProperty("keyCode", m.getKeyCode());
                mo.add("settings", serializeSettings(m.getSettings()));
                modulesArr.add(mo);
            }
            root.add("modules", modulesArr);

            JsonArray hudArr = new JsonArray();
            for (HudElement h : HudManager.getInstance().getElements()) {
                JsonObject ho = new JsonObject();
                ho.addProperty("name", h.getName());
                ho.addProperty("enabled", h.isEnabled());
                ho.addProperty("x", h.getX());
                ho.addProperty("y", h.getY());
                ho.addProperty("scale", h.getScale());
                ho.add("settings", serializeSettings(h.getSettings()));
                hudArr.add(ho);
            }
            root.add("hud", hudArr);

            VFile2 target = serverOverrideEnabled && activeServerKey != null ? serverFile(activeServerKey) : profileFile(currentProfile);
            writeJson(target, root);
            NotificationManager.getInstance().notify("Config saved", NotificationType.SUCCESS);
        } catch (Exception e) {
            NotificationManager.getInstance().notify("Failed to save config: " + e.getMessage(), NotificationType.ERROR);
        }
    }

    private JsonObject serializeSettings(java.util.List<Setting<?>> settings) {
        JsonObject obj = new JsonObject();
        for (Setting<?> s : settings) {
            Object v = s.toJson();
            if (v instanceof Boolean) {
                obj.addProperty(s.getName(), (Boolean) v);
            } else if (v instanceof Number) {
                obj.addProperty(s.getName(), (Number) v);
            } else {
                obj.addProperty(s.getName(), String.valueOf(v));
            }
        }
        return obj;
    }

    public void load() {
        VFile2 target = serverOverrideEnabled && activeServerKey != null ? serverFile(activeServerKey) : profileFile(currentProfile);
        if (!target.exists()) {
            save();
            return;
        }
        try (InputStreamReader reader = new InputStreamReader(target.getInputStream(), StandardCharsets.UTF_8)) {
            JsonObject root = gson.fromJson(reader, JsonObject.class);
            if (root == null) {
                throw new JsonParseException("empty config");
            }
            applyModules(root.getAsJsonArray("modules"));
            applyHud(root.getAsJsonArray("hud"));
        } catch (Exception e) {
            NotificationManager.getInstance().notify("Config corrupted, restoring defaults", NotificationType.ERROR);
            resetToDefaults();
        }
    }

    private void applyModules(JsonArray arr) {
        if (arr == null) {
            return;
        }
        for (JsonElement el : arr) {
            JsonObject mo = el.getAsJsonObject();
            Module m = ModuleManager.getInstance().getByName(mo.get("name").getAsString());
            if (m == null) {
                continue;
            }
            if (mo.has("keyCode")) {
                m.setKeyCode(mo.get("keyCode").getAsInt());
            }
            applySettings(m.getSettings(), mo.getAsJsonObject("settings"));
            boolean enabled = mo.has("enabled") && mo.get("enabled").getAsBoolean();
            if (enabled != m.isEnabled()) {
                m.setEnabled(enabled);
            }
        }
    }

    private void applyHud(JsonArray arr) {
        if (arr == null) {
            return;
        }
        for (JsonElement el : arr) {
            JsonObject ho = el.getAsJsonObject();
            HudElement h = HudManager.getInstance().getByName(ho.get("name").getAsString());
            if (h == null) {
                continue;
            }
            h.setEnabled(!ho.has("enabled") || ho.get("enabled").getAsBoolean());
            if (ho.has("x") && ho.has("y")) {
                h.setPosition(ho.get("x").getAsDouble(), ho.get("y").getAsDouble());
            }
            if (ho.has("scale")) {
                h.setScale(ho.get("scale").getAsFloat());
            }
            applySettings(h.getSettings(), ho.getAsJsonObject("settings"));
        }
    }

    private void applySettings(java.util.List<Setting<?>> settings, JsonObject obj) {
        if (obj == null) {
            return;
        }
        for (Setting<?> s : settings) {
            if (!obj.has(s.getName())) {
                continue;
            }
            JsonPrimitive p = obj.getAsJsonPrimitive(s.getName());
            if (p.isBoolean()) {
                s.fromJson(p.getAsBoolean());
            } else if (p.isNumber()) {
                s.fromJson(p.getAsNumber());
            } else {
                s.fromJson(p.getAsString());
            }
        }
    }

    private void writeJson(VFile2 file, JsonObject root) throws IOException {
        try (OutputStreamWriter writer = new OutputStreamWriter(file.getOutputStream(), StandardCharsets.UTF_8)) {
            gson.toJson(root, writer);
        }
    }

    public void switchProfile(ConfigProfile profile) {
        save();
        currentProfile = profile;
        load();
        NotificationManager.getInstance().notify("Switched to " + profile.getDisplayName() + " profile", NotificationType.INFO);
    }

    public void resetToDefaults() {
        for (Module m : ModuleManager.getInstance().getModules()) {
            m.resetToDefault();
        }
        for (HudElement h : HudManager.getInstance().getElements()) {
            h.resetPosition();
        }
        save();
        NotificationManager.getInstance().notify("Reset to defaults", NotificationType.INFO);
    }

    public String exportToString() {
        JsonObject root = new JsonObject();
        JsonArray modulesArr = new JsonArray();
        for (Module m : ModuleManager.getInstance().getModules()) {
            JsonObject mo = new JsonObject();
            mo.addProperty("name", m.getName());
            mo.addProperty("enabled", m.isEnabled());
            mo.addProperty("keyCode", m.getKeyCode());
            mo.add("settings", serializeSettings(m.getSettings()));
            modulesArr.add(mo);
        }
        root.add("modules", modulesArr);
        JsonArray hudArr = new JsonArray();
        for (HudElement h : HudManager.getInstance().getElements()) {
            JsonObject ho = new JsonObject();
            ho.addProperty("name", h.getName());
            ho.addProperty("enabled", h.isEnabled());
            ho.addProperty("x", h.getX());
            ho.addProperty("y", h.getY());
            ho.addProperty("scale", h.getScale());
            ho.add("settings", serializeSettings(h.getSettings()));
            hudArr.add(ho);
        }
        root.add("hud", hudArr);
        return gson.toJson(root);
    }

    public boolean importFromString(String json) {
        try {
            JsonObject root = gson.fromJson(json, JsonObject.class);
            if (root == null) {
                return false;
            }
            applyModules(root.getAsJsonArray("modules"));
            applyHud(root.getAsJsonArray("hud"));
            save();
            NotificationManager.getInstance().notify("Config imported", NotificationType.SUCCESS);
            return true;
        } catch (Exception e) {
            NotificationManager.getInstance().notify("Import failed: invalid config", NotificationType.ERROR);
            return false;
        }
    }

    /** Called when a multiplayer connection is established, to pick up per-server overrides. */
    public void onServerJoin(String serverKey) {
        save();
        activeServerKey = serverKey;
        serverOverrideEnabled = serverKey != null && serverFile(serverKey).exists();
        load();
    }

    public void onServerLeave() {
        save();
        activeServerKey = null;
        serverOverrideEnabled = false;
        load();
    }

    public boolean isServerOverrideActive() {
        return serverOverrideEnabled;
    }

    /** Saves the current state as a per-server override for the given server. */
    public void saveAsServerConfig(String serverKey) {
        activeServerKey = serverKey;
        serverOverrideEnabled = true;
        save();
        NotificationManager.getInstance().notify("Saved server-specific config", NotificationType.SUCCESS);
    }
}
