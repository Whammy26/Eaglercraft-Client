package net.lax1dude.eaglercraft.get.util;

import net.lax1dude.eaglercraft.EagRuntime;

/** Tracks play-time and distance travelled for the session statistics HUD. */
public final class SessionTracker {

    private static long sessionStart = EagRuntime.steadyTimeMillis();
    private static double distanceTraveled = 0.0;
    private static double lastX, lastY, lastZ;
    private static boolean hasLast = false;
    private static int kills = 0;

    private SessionTracker() {
    }

    public static void reset() {
        sessionStart = EagRuntime.steadyTimeMillis();
        distanceTraveled = 0.0;
        hasLast = false;
        kills = 0;
    }

    public static void updatePosition(double x, double y, double z) {
        if (hasLast) {
            double dx = x - lastX;
            double dy = y - lastY;
            double dz = z - lastZ;
            distanceTraveled += Math.sqrt(dx * dx + dy * dy + dz * dz);
        }
        lastX = x;
        lastY = y;
        lastZ = z;
        hasLast = true;
    }

    public static void recordKill() {
        kills++;
    }

    public static long getSessionSeconds() {
        return (EagRuntime.steadyTimeMillis() - sessionStart) / 1000L;
    }

    public static double getDistanceTraveled() {
        return distanceTraveled;
    }

    public static int getKills() {
        return kills;
    }
}
