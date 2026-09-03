package net.lax1dude.eaglercraft.get;

import net.lax1dude.eaglercraft.get.config.ConfigManager;
import net.lax1dude.eaglercraft.get.gui.ClickGuiScreen;
import net.lax1dude.eaglercraft.get.gui.HudEditorScreen;
import net.lax1dude.eaglercraft.get.hud.HudManager;
import net.lax1dude.eaglercraft.get.hud.elements.ArmorHud;
import net.lax1dude.eaglercraft.get.hud.elements.AttackIndicatorHud;
import net.lax1dude.eaglercraft.get.hud.elements.BiomeHud;
import net.lax1dude.eaglercraft.get.hud.elements.ChatHud;
import net.lax1dude.eaglercraft.get.hud.elements.ClockHud;
import net.lax1dude.eaglercraft.get.hud.elements.ComboHud;
import net.lax1dude.eaglercraft.get.hud.elements.CoordsHud;
import net.lax1dude.eaglercraft.get.hud.elements.CpsHud;
import net.lax1dude.eaglercraft.get.hud.elements.DirectionHud;
import net.lax1dude.eaglercraft.get.hud.elements.DurabilityHud;
import net.lax1dude.eaglercraft.get.hud.elements.FpsHud;
import net.lax1dude.eaglercraft.get.hud.elements.HeldItemHud;
import net.lax1dude.eaglercraft.get.hud.elements.KeystrokesHud;
import net.lax1dude.eaglercraft.get.hud.elements.MinimapHud;
import net.lax1dude.eaglercraft.get.hud.elements.PingHud;
import net.lax1dude.eaglercraft.get.hud.elements.PotionHud;
import net.lax1dude.eaglercraft.get.hud.elements.ReachHud;
import net.lax1dude.eaglercraft.get.hud.elements.ScoreboardHud;
import net.lax1dude.eaglercraft.get.hud.elements.ServerInfoHud;
import net.lax1dude.eaglercraft.get.hud.elements.SessionStatsHud;
import net.lax1dude.eaglercraft.get.hud.elements.SpeedHud;
import net.lax1dude.eaglercraft.get.hud.elements.TabListHud;
import net.lax1dude.eaglercraft.get.hud.elements.TargetHud;
import net.lax1dude.eaglercraft.get.hud.elements.WaypointHud;
import net.lax1dude.eaglercraft.get.module.Module;
import net.lax1dude.eaglercraft.get.module.ModuleManager;
import net.lax1dude.eaglercraft.get.modules.combat.CustomCrosshairModule;
import net.lax1dude.eaglercraft.get.modules.combat.FreelookModule;
import net.lax1dude.eaglercraft.get.modules.combat.HitColorModule;
import net.lax1dude.eaglercraft.get.modules.combat.LowHealthWarningModule;
import net.lax1dude.eaglercraft.get.modules.combat.ToggleSneakModule;
import net.lax1dude.eaglercraft.get.modules.combat.ToggleSprintModule;
import net.lax1dude.eaglercraft.get.modules.combat.ZoomModule;
import net.lax1dude.eaglercraft.get.modules.misc.AutoGgModule;
import net.lax1dude.eaglercraft.get.modules.misc.BetterF3Module;
import net.lax1dude.eaglercraft.get.modules.misc.ChatSearchModule;
import net.lax1dude.eaglercraft.get.modules.misc.ChatTimestampsModule;
import net.lax1dude.eaglercraft.get.modules.misc.InventorySortingModule;
import net.lax1dude.eaglercraft.get.modules.misc.ItemSearchModule;
import net.lax1dude.eaglercraft.get.modules.misc.ScreenshotManagerModule;
import net.lax1dude.eaglercraft.get.modules.misc.WaypointsModule;
import net.lax1dude.eaglercraft.get.modules.performance.DynamicFpsModule;
import net.lax1dude.eaglercraft.get.modules.performance.PerformanceModule;
import net.lax1dude.eaglercraft.get.modules.visual.CustomNameTagsModule;
import net.lax1dude.eaglercraft.get.modules.visual.FullbrightModule;
import net.lax1dude.eaglercraft.get.modules.visual.ItemFrameVisibilityModule;
import net.lax1dude.eaglercraft.get.notification.NotificationManager;
import net.lax1dude.eaglercraft.get.util.ClickTracker;
import net.lax1dude.eaglercraft.get.util.ComboTracker;
import net.lax1dude.eaglercraft.get.util.SessionTracker;
import net.lax1dude.eaglercraft.get.util.WaypointManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;

/**
 * Bootstraps the GET Client module/HUD framework and exposes the small set of static
 * hooks called from patched vanilla classes (Minecraft, IngameGui, KeyboardListener, etc).
 */
public final class GetClient {

    public static final int KEY_CLICKGUI = 344; // GLFW RIGHT_SHIFT
    public static final int KEY_HUD_EDITOR = 260; // GLFW INSERT

    private static boolean initialized = false;

    private GetClient() {
    }

    public static void init() {
        if (initialized) {
            return;
        }
        initialized = true;

        ModuleManager mm = ModuleManager.getInstance();
        mm.register(new ToggleSprintModule());
        mm.register(new ToggleSneakModule());
        mm.register(new LowHealthWarningModule());
        mm.register(new CustomCrosshairModule());
        mm.register(new HitColorModule());
        mm.register(new ZoomModule());
        mm.register(new FreelookModule());
        mm.register(new FullbrightModule());
        mm.register(new CustomNameTagsModule());
        mm.register(new AutoGgModule());
        mm.register(new ChatTimestampsModule());
        mm.register(new ItemFrameVisibilityModule());
        mm.register(new BetterF3Module());
        mm.register(new ItemSearchModule());
        mm.register(new InventorySortingModule());
        mm.register(new ChatSearchModule());
        mm.register(new ScreenshotManagerModule());
        mm.register(new WaypointsModule());
        mm.register(new PerformanceModule());
        mm.register(new DynamicFpsModule());

        HudManager hm = HudManager.getInstance();
        hm.register(new FpsHud());
        hm.register(new PingHud());
        hm.register(new CoordsHud());
        hm.register(new BiomeHud());
        hm.register(new DirectionHud());
        hm.register(new ServerInfoHud());
        hm.register(new ClockHud());
        hm.register(new SpeedHud());
        hm.register(new SessionStatsHud());
        hm.register(new ScoreboardHud());
        hm.register(new TabListHud());
        hm.register(new ChatHud());
        hm.register(new DurabilityHud());
        hm.register(new HeldItemHud());
        hm.register(new TargetHud());
        hm.register(new KeystrokesHud());
        hm.register(new CpsHud());
        hm.register(new PotionHud());
        hm.register(new ArmorHud());
        hm.register(new ReachHud());
        hm.register(new ComboHud());
        hm.register(new AttackIndicatorHud());
        hm.register(new WaypointHud());
        hm.register(new MinimapHud());

        ConfigManager.getInstance().init();
    }

    public static void onTick() {
        Minecraft mc = Minecraft.getInstance();
        ModuleManager.getInstance().onTick();
        ComboTracker.tick();
        if (mc.player != null) {
            SessionTracker.updatePosition(mc.player.posX, mc.player.posY, mc.player.posZ);
        }
    }

    public static void onRenderOverlay(float partialTicks) {
        ModuleManager.getInstance().onRender(partialTicks);
        HudManager.getInstance().render(partialTicks);
        NotificationManager.getInstance().render(partialTicks);
    }

    /** Called from KeyboardListener while no screen is open and a key was just pressed. */
    public static boolean onKeyPress(int key) {
        if (key == KEY_CLICKGUI) {
            Minecraft.getInstance().displayGuiScreen(new ClickGuiScreen());
            return true;
        }
        if (key == KEY_HUD_EDITOR) {
            Minecraft.getInstance().displayGuiScreen(new HudEditorScreen());
            return true;
        }
        return ModuleManager.getInstance().onKeyPress(key);
    }

    public static void onLeftClick() {
        ClickTracker.recordLeftClick();
    }

    public static void onRightClick() {
        ClickTracker.recordRightClick();
    }

    /** Called right after the vanilla attack call in Minecraft.clickMouse(). */
    public static void onEntityHit(Entity target) {
        ComboTracker.recordHit();
        if (target instanceof LivingEntity && ((LivingEntity) target).getHealth() <= 0.0F) {
            SessionTracker.recordKill();
        }
    }

    public static <T extends Module> boolean isEnabled(Class<T> type) {
        Module m = ModuleManager.getInstance().get(type);
        return m != null && m.isEnabled();
    }

    public static <T extends Module> T get(Class<T> type) {
        return ModuleManager.getInstance().get(type);
    }

    public static boolean isFreelookActive() {
        FreelookModule m = ModuleManager.getInstance().get(FreelookModule.class);
        return m != null && m.isEnabled();
    }

    public static void accumulateFreelook(double dYaw, double dPitch) {
        FreelookModule m = ModuleManager.getInstance().get(FreelookModule.class);
        if (m != null) {
            m.accumulate(dYaw, dPitch);
        }
    }

    public static float[] getFreelookAngles() {
        FreelookModule m = ModuleManager.getInstance().get(FreelookModule.class);
        return m != null ? new float[]{m.getYaw(), m.getPitch()} : new float[]{0, 0};
    }

    public static ITextComponent applyChatTimestamp(ITextComponent original) {
        ChatTimestampsModule m = ModuleManager.getInstance().get(ChatTimestampsModule.class);
        if (m == null || !m.isEnabled()) {
            return original;
        }
        long totalSeconds = (System.currentTimeMillis() / 1000L) % 86400L;
        String time = String.format("[%02d:%02d:%02d] ", totalSeconds / 3600, (totalSeconds % 3600) / 60, totalSeconds % 60);
        return new StringTextComponent(time).applyTextStyle(TextFormatting.GRAY).appendSibling(original);
    }

    public static String enhanceNameTag(Entity entity, String baseName) {
        CustomNameTagsModule m = ModuleManager.getInstance().get(CustomNameTagsModule.class);
        if (m == null || !m.isEnabled() || !m.shouldShowHealth() || !(entity instanceof LivingEntity)) {
            return baseName;
        }
        LivingEntity living = (LivingEntity) entity;
        return baseName + " " + TextFormatting.RED + (int) living.getHealth() + "\u2764" + TextFormatting.RESET;
    }

    public static boolean isItemFramesHidden() {
        return isEnabled(ItemFrameVisibilityModule.class);
    }

    /** Called from ContainerScreen.render() after everything else has drawn. */
    public static void renderItemSearchOverlay(int guiLeft, int guiTop, FontRenderer font, Container container) {
        ItemSearchModule m = ModuleManager.getInstance().get(ItemSearchModule.class);
        if (m != null) {
            m.renderOverlay(guiLeft, guiTop, font, container);
        }
    }

    /** Called from ContainerScreen.charTyped(); returns true if the character was consumed by item search. */
    public static boolean handleItemSearchChar(char c) {
        ItemSearchModule m = ModuleManager.getInstance().get(ItemSearchModule.class);
        return m != null && m.handleChar(c);
    }

    public static void appendDebugInfo(java.util.List<String> list) {
        BetterF3Module m = ModuleManager.getInstance().get(BetterF3Module.class);
        if (m != null && m.isEnabled()) {
            m.appendInfo(list);
        }
    }

    /** Called from Minecraft.loadWorld() once a world/server connection is established. */
    public static void onWorldJoin(String worldKey) {
        ConfigManager.getInstance().onServerJoin(worldKey);
        WaypointManager.getInstance().onWorldChanged(worldKey);
    }

    /** Called from Minecraft when the current world/connection is torn down. */
    public static void onWorldLeave() {
        ConfigManager.getInstance().onServerLeave();
    }
}
