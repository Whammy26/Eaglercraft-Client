package net.lax1dude.eaglercraft.get.modules.misc;

import java.util.List;

import net.lax1dude.eaglercraft.get.Category;
import net.lax1dude.eaglercraft.get.module.Module;
import net.lax1dude.eaglercraft.get.util.SessionTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.BlockPos;

/** Adds a few extra genuinely useful lines to the vanilla F3 debug screen. */
public class BetterF3Module extends Module {

    public BetterF3Module() {
        super("Better F3", "Adds session time, light level and local difficulty to the debug screen", Category.MISC, false, -1);
    }

    public void appendInfo(List<String> list) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || mc.world == null) {
            return;
        }
        BlockPos pos = mc.player.getPosition();
        long secs = SessionTracker.getSessionSeconds();
        list.add("");
        list.add(String.format("[GET] Session: %02d:%02d:%02d", secs / 3600, (secs % 3600) / 60, secs % 60));
        list.add("[GET] Light level: " + mc.world.getLight(pos));
        list.add("[GET] Local difficulty: " + String.format("%.2f", mc.world.getDifficultyForLocation(pos).getAdditionalDifficulty()));
    }
}
