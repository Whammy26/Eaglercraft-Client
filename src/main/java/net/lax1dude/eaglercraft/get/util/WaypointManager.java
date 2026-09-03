package net.lax1dude.eaglercraft.get.util;

import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import net.lax1dude.eaglercraft.internal.vfs2.VFile2;
import net.minecraft.client.Minecraft;

/** Persists waypoints per-world (keyed by world/server identifier). */
public final class WaypointManager {

    private static final WaypointManager INSTANCE = new WaypointManager();

    public static WaypointManager getInstance() {
        return INSTANCE;
    }

    private final Gson gson = new Gson();
    private final List<Waypoint> waypoints = new ArrayList<>();
    private String worldKey = null;

    private WaypointManager() {
    }

    public List<Waypoint> getWaypoints() {
        return waypoints;
    }

    public void add(Waypoint w) {
        waypoints.add(w);
        save();
    }

    public void remove(Waypoint w) {
        waypoints.remove(w);
        save();
    }

    private VFile2 file() {
        String key = worldKey != null ? worldKey : "default";
        return new VFile2(new VFile2(Minecraft.getInstance().gameDir, "getclient"), "waypoints_" + key.replaceAll("[^a-zA-Z0-9_.-]", "_") + ".json");
    }

    public void onWorldChanged(String newKey) {
        this.worldKey = newKey;
        waypoints.clear();
        VFile2 f = file();
        if (!f.exists()) {
            return;
        }
        try (InputStreamReader reader = new InputStreamReader(f.getInputStream(), StandardCharsets.UTF_8)) {
            JsonArray arr = gson.fromJson(reader, JsonArray.class);
            if (arr == null) {
                return;
            }
            for (com.google.gson.JsonElement el : arr) {
                JsonObject o = el.getAsJsonObject();
                waypoints.add(new Waypoint(o.get("name").getAsString(), o.get("x").getAsDouble(), o.get("y").getAsDouble(), o.get("z").getAsDouble(), o.get("color").getAsInt()));
            }
        } catch (Exception e) {
            waypoints.clear();
        }
    }

    public void save() {
        JsonArray arr = new JsonArray();
        for (Waypoint w : waypoints) {
            JsonObject o = new JsonObject();
            o.addProperty("name", w.name);
            o.addProperty("x", w.x);
            o.addProperty("y", w.y);
            o.addProperty("z", w.z);
            o.addProperty("color", w.color);
            arr.add(o);
        }
        try (OutputStreamWriter writer = new OutputStreamWriter(file().getOutputStream(), StandardCharsets.UTF_8)) {
            gson.toJson(arr, writer);
        } catch (Exception ignored) {
        }
    }
}
