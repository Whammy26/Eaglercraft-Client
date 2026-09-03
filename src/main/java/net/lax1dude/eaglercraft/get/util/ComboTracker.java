package net.lax1dude.eaglercraft.get.util;

import net.lax1dude.eaglercraft.EagRuntime;

/** Tracks consecutive successful hits on entities for the combo counter HUD. */
public final class ComboTracker {

    private static final long TIMEOUT_MS = 2500L;

    private static int combo = 0;
    private static long lastHit = 0L;
    private static int bestCombo = 0;

    private ComboTracker() {
    }

    public static void recordHit() {
        combo++;
        bestCombo = Math.max(bestCombo, combo);
        lastHit = EagRuntime.steadyTimeMillis();
    }

    public static void tick() {
        if (combo > 0 && EagRuntime.steadyTimeMillis() - lastHit > TIMEOUT_MS) {
            combo = 0;
        }
    }

    public static int getCombo() {
        return combo;
    }

    public static int getBestCombo() {
        return bestCombo;
    }
}
