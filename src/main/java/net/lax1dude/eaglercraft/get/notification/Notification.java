package net.lax1dude.eaglercraft.get.notification;

import net.lax1dude.eaglercraft.EagRuntime;

/**
 * A single toast notification with a fixed lifetime, fading out near the end.
 */
public class Notification {

    private static final long LIFETIME_MS = 3000L;
    private static final long FADE_MS = 400L;

    private final String message;
    private final NotificationType type;
    private final long createdAt;

    public Notification(String message, NotificationType type) {
        this.message = message;
        this.type = type;
        this.createdAt = EagRuntime.steadyTimeMillis();
    }

    public String getMessage() {
        return message;
    }

    public NotificationType getType() {
        return type;
    }

    public long getAgeMs() {
        return EagRuntime.steadyTimeMillis() - createdAt;
    }

    public boolean isExpired() {
        return getAgeMs() > LIFETIME_MS;
    }

    /** Returns 0..1 opacity, fading in and out at the edges of its lifetime. */
    public float getOpacity() {
        long age = getAgeMs();
        if (age < FADE_MS) {
            return age / (float) FADE_MS;
        }
        if (age > LIFETIME_MS - FADE_MS) {
            return Math.max(0.0F, (LIFETIME_MS - age) / (float) FADE_MS);
        }
        return 1.0F;
    }
}
