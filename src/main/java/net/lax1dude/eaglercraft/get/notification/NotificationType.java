package net.lax1dude.eaglercraft.get.notification;

public enum NotificationType {
    INFO(0xFF3B82F6),
    SUCCESS(0xFF22C55E),
    ERROR(0xFFEF4444);

    private final int color;

    NotificationType(int color) {
        this.color = color;
    }

    public int getColor() {
        return color;
    }
}
