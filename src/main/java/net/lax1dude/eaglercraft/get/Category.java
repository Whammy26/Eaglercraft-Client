package net.lax1dude.eaglercraft.get;

/**
 * Module categories shown as tabs in the ClickGUI.
 */
public enum Category {
    COMBAT("Combat"),
    HUD("HUD"),
    VISUAL("Visual"),
    PLAYER("Player"),
    PERFORMANCE("Performance"),
    MISC("Misc");

    private final String displayName;

    Category(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
