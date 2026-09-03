package net.lax1dude.eaglercraft.get.modules.combat;

import net.lax1dude.eaglercraft.get.Category;
import net.lax1dude.eaglercraft.get.module.Module;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.MathHelper;

/**
 * Decouples the rendered camera direction from the player's actual facing (movement/attacks
 * still use the real facing). Mouse deltas are redirected here instead of the player while active.
 */
public class FreelookModule extends Module {

    private float freelookYaw;
    private float freelookPitch;

    public FreelookModule() {
        super("Freelook", "Rotate the camera independently of your character's facing", Category.COMBAT, false, -1);
    }

    @Override
    protected void onEnable() {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player != null) {
            freelookYaw = mc.player.rotationYaw;
            freelookPitch = mc.player.rotationPitch;
        }
    }

    /** Called from MouseHelper instead of player.rotateTowards() while this module is active. */
    public void accumulate(double dYaw, double dPitch) {
        freelookYaw = (float) (freelookYaw + dYaw);
        freelookPitch = MathHelper.clamp((float) (freelookPitch + dPitch), -90.0F, 90.0F);
    }

    public float getYaw() {
        return freelookYaw;
    }

    public float getPitch() {
        return freelookPitch;
    }
}
