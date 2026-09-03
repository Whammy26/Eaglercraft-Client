package net.lax1dude.eaglercraft.get.modules.misc;

import net.lax1dude.eaglercraft.get.Category;
import net.lax1dude.eaglercraft.get.gui.WaypointScreen;
import net.lax1dude.eaglercraft.get.module.Module;
import net.minecraft.client.Minecraft;

/** Opens the waypoint management screen; actual create/edit/delete/save lives in WaypointManager. */
public class WaypointsModule extends Module {

    public WaypointsModule() {
        super("Waypoints", "Create, edit and delete waypoints", Category.MISC, false, -1);
    }

    @Override
    protected void onEnable() {
        Minecraft.getInstance().displayGuiScreen(new WaypointScreen());
        setEnabled(false);
    }
}
