package net.lax1dude.eaglercraft.get.config;

public enum ConfigProfile {
    PVP("PvP"),
    SURVIVAL("Survival"),
    FPS("FPS"),
    CUSTOM("Custom");

    private final String displayName;

    ConfigProfile(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
