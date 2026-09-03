package net.lax1dude.eaglercraft.get.gui;

import java.util.ArrayList;
import java.util.List;

import net.lax1dude.eaglercraft.EagRuntime;
import net.lax1dude.eaglercraft.get.Category;
import net.lax1dude.eaglercraft.get.config.ConfigManager;
import net.lax1dude.eaglercraft.get.config.ConfigProfile;
import net.lax1dude.eaglercraft.get.module.Module;
import net.lax1dude.eaglercraft.get.module.ModuleManager;
import net.lax1dude.eaglercraft.get.settings.BooleanSetting;
import net.lax1dude.eaglercraft.get.settings.ColorSetting;
import net.lax1dude.eaglercraft.get.settings.EnumSetting;
import net.lax1dude.eaglercraft.get.settings.Setting;
import net.lax1dude.eaglercraft.get.settings.SliderSetting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.text.StringTextComponent;

/**
 * The RIGHT SHIFT client menu: category tabs, searchable module list, inline settings,
 * keybind editing, profile switching and config import/export/reset.
 */
public class ClickGuiScreen extends Screen {

    private static final int SIDEBAR_W = 110;
    private static final int ROW_H = 16;
    private static final int SETTING_ROW_H = 16;

    private final long openedAt = EagRuntime.steadyTimeMillis();
    private Category selectedCategory = Category.COMBAT;
    private String search = "";
    private final List<Module> expanded = new ArrayList<>();
    private final List<net.lax1dude.eaglercraft.get.hud.HudElement> expandedHud = new ArrayList<>();
    private double scroll = 0;

    private Module listeningForKey = null;
    private Setting<?> draggingSlider = null;
    private ColorSetting editingColor = null;
    private int pickerX, pickerY, pickerW = 160;
    private int draggingChannel = -1;
    private boolean importMode = false;
    private String importBuffer = "";

    public ClickGuiScreen() {
        super(new StringTextComponent("GET Client"));
    }

    @Override
    protected void init() {
    }

    private float openProgress() {
        long age = EagRuntime.steadyTimeMillis() - openedAt;
        return Math.min(1.0F, age / 120.0F);
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
        renderBackground();
        float progress = openProgress();
        int panelW = 480;
        int panelH = 300;
        int px = (this.width - panelW) / 2;
        int py = (this.height - panelH) / 2 + (int) ((1.0F - progress) * 20);

        AbstractGui.fill(px, py, px + panelW, py + panelH, 0xE0101216);
        AbstractGui.fill(px, py, px + panelW, py + 22, 0xE01A1D24);
        this.font.drawStringWithShadow("GET Client", px + 8, py + 7, 0xFFFFFFFF);
        String hint = "RIGHT SHIFT to close";
        this.font.drawStringWithShadow(hint, px + panelW - this.font.getStringWidth(hint) - 8, py + 7, 0xFF8A8A94);

        renderSidebar(px, py + 22, panelH - 22, mouseX, mouseY);
        renderModuleList(px + SIDEBAR_W, py + 22, panelW - SIDEBAR_W, panelH - 22, mouseX, mouseY);

        if (editingColor != null) {
            renderColorPicker(mouseX, mouseY);
        }
        if (importMode) {
            renderImportBox();
        }
    }

    private void renderSidebar(int x, int y, int h, int mouseX, int mouseY) {
        AbstractGui.fill(x, y, x + SIDEBAR_W, y + h, 0xD0161822);
        int cy = y + 4;
        for (Category c : Category.values()) {
            boolean sel = c == selectedCategory;
            boolean hover = mouseX >= x && mouseX < x + SIDEBAR_W && mouseY >= cy && mouseY < cy + ROW_H;
            int bg = sel ? 0xFF2A5CDB : hover ? 0x30FFFFFF : 0x00000000;
            AbstractGui.fill(x + 2, cy, x + SIDEBAR_W - 2, cy + ROW_H - 2, bg);
            this.font.drawStringWithShadow(c.getDisplayName(), x + 8, cy + 4, sel ? 0xFFFFFFFF : 0xFFB0B0BA);
            cy += ROW_H;
        }

        cy += 6;
        this.font.drawStringWithShadow("Profile", x + 8, cy, 0xFF8A8A94);
        cy += 11;
        for (ConfigProfile p : ConfigProfile.values()) {
            boolean sel = ConfigManager.getInstance().getCurrentProfile() == p;
            boolean hover = mouseX >= x && mouseX < x + SIDEBAR_W && mouseY >= cy && mouseY < cy + 12;
            AbstractGui.fill(x + 2, cy, x + SIDEBAR_W - 2, cy + 11, hover ? 0x30FFFFFF : 0x00000000);
            this.font.drawStringWithShadow(p.getDisplayName(), x + 8, cy + 1, sel ? 0xFF4C8DFF : 0xFF8A8A94);
            cy += 12;
        }

        int bottomY = y + h - 46;
        drawFooterButton(x, bottomY, "Reset All", mouseX, mouseY);
        drawFooterButton(x, bottomY + 15, importMode ? "Cancel Import" : "Import/Export", mouseX, mouseY);
    }

    private void drawFooterButton(int x, int y, String label, int mouseX, int mouseY) {
        boolean hover = mouseX >= x + 2 && mouseX < x + SIDEBAR_W - 2 && mouseY >= y && mouseY < y + 13;
        AbstractGui.fill(x + 2, y, x + SIDEBAR_W - 2, y + 13, hover ? 0x40FFFFFF : 0x20FFFFFF);
        this.font.drawStringWithShadow(label, x + 6, y + 2, 0xFFE8E8E8);
    }

    private List<Module> visibleModules() {
        List<Module> all = search.isEmpty() ? ModuleManager.getInstance().getModules(selectedCategory) : ModuleManager.getInstance().getModules();
        List<Module> result = new ArrayList<>();
        for (Module m : all) {
            if (search.isEmpty() || m.getName().toLowerCase().contains(search.toLowerCase())) {
                result.add(m);
            }
        }
        return result;
    }

    private void renderModuleList(int x, int y, int w, int h, int mouseX, int mouseY) {
        AbstractGui.fill(x, y, x + w, y + 20, 0xD0161822);
        String searchLabel = search.isEmpty() ? (selectedCategory == Category.HUD ? "Search HUD elements..." : "Search modules...") : search;
        this.font.drawStringWithShadow(searchLabel, x + 8, y + 6, search.isEmpty() ? 0xFF6A6A74 : 0xFFE8E8E8);

        int listY = y + 22;
        int listH = h - 22;
        AbstractGui.fill(x, listY, x + w, listY + listH, 0x00000000);

        if (selectedCategory == Category.HUD) {
            renderHudList(x, listY, w, listH, mouseX, mouseY);
            return;
        }

        List<Module> modules = visibleModules();
        int totalHeight = 0;
        for (Module m : modules) {
            totalHeight += ROW_H;
            if (expanded.contains(m)) {
                totalHeight += m.getSettings().size() * SETTING_ROW_H + 4;
            }
        }
        scroll = Math.max(0, Math.min(scroll, Math.max(0, totalHeight - listH)));

        int cy = listY - (int) scroll;
        for (Module m : modules) {
            if (cy + ROW_H > listY && cy < listY + listH) {
                drawModuleRow(x, cy, w, m, mouseX, mouseY);
            }
            cy += ROW_H;
            if (expanded.contains(m)) {
                for (Setting<?> s : m.getSettings()) {
                    if (cy + SETTING_ROW_H > listY && cy < listY + listH) {
                        drawSetting(x + 14, cy, w - 18, s, mouseX, mouseY);
                    }
                    cy += SETTING_ROW_H;
                }
                cy += 4;
            }
        }
    }

    private List<net.lax1dude.eaglercraft.get.hud.HudElement> visibleHudElements() {
        List<net.lax1dude.eaglercraft.get.hud.HudElement> result = new ArrayList<>();
        for (net.lax1dude.eaglercraft.get.hud.HudElement e : net.lax1dude.eaglercraft.get.hud.HudManager.getInstance().getElements()) {
            if (search.isEmpty() || e.getName().toLowerCase().contains(search.toLowerCase())) {
                result.add(e);
            }
        }
        return result;
    }

    private void renderHudList(int x, int listY, int w, int listH, int mouseX, int mouseY) {
        List<net.lax1dude.eaglercraft.get.hud.HudElement> elements = visibleHudElements();
        int totalHeight = 0;
        for (net.lax1dude.eaglercraft.get.hud.HudElement e : elements) {
            totalHeight += ROW_H;
            if (expandedHud.contains(e)) {
                totalHeight += e.getSettings().size() * SETTING_ROW_H + SETTING_ROW_H + 4;
            }
        }
        scroll = Math.max(0, Math.min(scroll, Math.max(0, totalHeight - listH)));

        int cy = listY - (int) scroll;
        for (net.lax1dude.eaglercraft.get.hud.HudElement e : elements) {
            if (cy + ROW_H > listY && cy < listY + listH) {
                drawHudRow(x, cy, w, e, mouseX, mouseY);
            }
            cy += ROW_H;
            if (expandedHud.contains(e)) {
                if (cy + SETTING_ROW_H > listY && cy < listY + listH) {
                    drawScaleRow(x + 14, cy, w - 18, e);
                }
                cy += SETTING_ROW_H;
                for (Setting<?> s : e.getSettings()) {
                    if (cy + SETTING_ROW_H > listY && cy < listY + listH) {
                        drawSetting(x + 14, cy, w - 18, s, mouseX, mouseY);
                    }
                    cy += SETTING_ROW_H;
                }
                cy += 4;
            }
        }
    }

    private void drawHudRow(int x, int y, int w, net.lax1dude.eaglercraft.get.hud.HudElement e, int mouseX, int mouseY) {
        boolean hover = mouseX >= x && mouseX < x + w && mouseY >= y && mouseY < y + ROW_H;
        AbstractGui.fill(x, y, x + w, y + ROW_H - 1, hover ? 0x20FFFFFF : 0x00000000);
        int dotColor = e.isEnabled() ? 0xFF3FD16B : 0xFF565660;
        AbstractGui.fill(x + 8, y + 6, x + 12, y + 10, dotColor);
        this.font.drawStringWithShadow(e.getName(), x + 18, y + 4, e.isEnabled() ? 0xFFFFFFFF : 0xFFC0C0C8);

        int resetW = 46;
        int resetX = x + w - resetW - 4;
        boolean resetHover = mouseX >= resetX && mouseX < resetX + resetW && mouseY >= y + 2 && mouseY < y + ROW_H - 2;
        AbstractGui.fill(resetX, y + 2, resetX + resetW, y + ROW_H - 2, resetHover ? 0x40FFFFFF : 0x25FFFFFF);
        net.lax1dude.eaglercraft.get.util.RenderUtil.drawCentered(this.font, "Reset", resetX + resetW / 2, y + 4, 0xFFE8E8E8);

        String arrow = expandedHud.contains(e) ? "-" : "+";
        this.font.drawStringWithShadow(arrow, resetX - 14, y + 4, 0xFF8A8A94);
    }

    private void drawScaleRow(int x, int y, int w, net.lax1dude.eaglercraft.get.hud.HudElement e) {
        this.font.drawStringWithShadow(String.format("Scale: %.2fx", e.getScale()), x, y - 1, 0xFFC0C0C8);
        int barY = y + 9;
        AbstractGui.fill(x, barY, x + w, barY + 3, 0xFF303038);
        double pct = (e.getScale() - 0.5) / 1.5;
        AbstractGui.fill(x, barY, x + (int) (w * pct), barY + 3, 0xFF4C8DFF);
    }

    private void drawModuleRow(int x, int y, int w, Module m, int mouseX, int mouseY) {
        boolean hover = mouseX >= x && mouseX < x + w && mouseY >= y && mouseY < y + ROW_H;
        AbstractGui.fill(x, y, x + w, y + ROW_H - 1, hover ? 0x20FFFFFF : 0x00000000);
        int dotColor = m.isEnabled() ? 0xFF3FD16B : 0xFF565660;
        AbstractGui.fill(x + 8, y + 6, x + 12, y + 10, dotColor);
        this.font.drawStringWithShadow(m.getName(), x + 18, y + 4, m.isEnabled() ? 0xFFFFFFFF : 0xFFC0C0C8);

        String keyLabel = m.getKeyCode() == -1 ? "NONE" : keyName(m.getKeyCode());
        if (listeningForKey == m) {
            keyLabel = "...";
        }
        int keyW = 46;
        int keyX = x + w - keyW - 4;
        boolean keyHover = mouseX >= keyX && mouseX < keyX + keyW && mouseY >= y + 2 && mouseY < y + ROW_H - 2;
        AbstractGui.fill(keyX, y + 2, keyX + keyW, y + ROW_H - 2, listeningForKey == m ? 0xFF4C8DFF : keyHover ? 0x40FFFFFF : 0x25FFFFFF);
        net.lax1dude.eaglercraft.get.util.RenderUtil.drawCentered(this.font, keyLabel, keyX + keyW / 2, y + 4, 0xFFE8E8E8);

        if (!m.getSettings().isEmpty()) {
            String arrow = expanded.contains(m) ? "-" : "+";
            this.font.drawStringWithShadow(arrow, keyX - 14, y + 4, 0xFF8A8A94);
        }
    }

    private void drawSetting(int x, int y, int w, Setting<?> s, int mouseX, int mouseY) {
        if (s instanceof BooleanSetting) {
            BooleanSetting b = (BooleanSetting) s;
            this.font.drawStringWithShadow(s.getName(), x, y + 3, 0xFFC0C0C8);
            int boxX = x + w - 26;
            AbstractGui.fill(boxX, y + 2, boxX + 22, y + 12, b.get() ? 0xFF3FD16B : 0xFF3A3A42);
        } else if (s instanceof SliderSetting) {
            SliderSetting sl = (SliderSetting) s;
            this.font.drawStringWithShadow(s.getName() + ": " + sl.display(), x, y - 1, 0xFFC0C0C8);
            int barY = y + 9;
            AbstractGui.fill(x, barY, x + w, barY + 3, 0xFF303038);
            double pct = (sl.get() - sl.getMin()) / (sl.getMax() - sl.getMin());
            AbstractGui.fill(x, barY, x + (int) (w * pct), barY + 3, 0xFF4C8DFF);
        } else if (s instanceof EnumSetting) {
            EnumSetting<?> e = (EnumSetting<?>) s;
            this.font.drawStringWithShadow(s.getName(), x, y + 3, 0xFFC0C0C8);
            String val = e.get().toString();
            this.font.drawStringWithShadow(val, x + w - this.font.getStringWidth(val), y + 3, 0xFF4C8DFF);
        } else if (s instanceof ColorSetting) {
            ColorSetting cs = (ColorSetting) s;
            this.font.drawStringWithShadow(s.getName(), x, y + 3, 0xFFC0C0C8);
            int boxX = x + w - 20;
            AbstractGui.fill(boxX, y + 1, boxX + 16, y + 12, 0xFF000000 | cs.get());
        }
    }

    private void renderColorPicker(int mouseX, int mouseY) {
        int pw = 160, ph = 90;
        int px = Math.min(this.width - pw - 4, mouseX + 8);
        int py = Math.min(this.height - ph - 4, mouseY + 8);
        pickerX = px;
        pickerY = py;
        pickerW = pw;
        AbstractGui.fill(px, py, px + pw, py + ph, 0xF0161822);
        String[] labels = {"A", "R", "G", "B"};
        int[] values = {editingColor.getAlpha(), editingColor.getRed(), editingColor.getGreen(), editingColor.getBlue()};
        for (int i = 0; i < 4; i++) {
            int ry = py + 6 + i * 20;
            this.font.drawStringWithShadow(labels[i] + ": " + values[i], px + 6, ry, 0xFFE8E8E8);
            AbstractGui.fill(px + 6, ry + 10, px + pw - 6, ry + 13, 0xFF303038);
            AbstractGui.fill(px + 6, ry + 10, px + 6 + (int) ((pw - 12) * (values[i] / 255.0)), ry + 13, 0xFF4C8DFF);
        }
    }

    private void renderImportBox() {
        int pw = 320, ph = 120;
        int px = (this.width - pw) / 2;
        int py = (this.height - ph) / 2;
        AbstractGui.fill(px, py, px + pw, py + ph, 0xF0161822);
        this.font.drawStringWithShadow("Paste config JSON, then press Enter. Ctrl+C copies current config.", px + 6, py + 6, 0xFFE8E8E8);
        String display = importBuffer.length() > 200 ? importBuffer.substring(importBuffer.length() - 200) : importBuffer;
        AbstractGui.fill(px + 6, py + 20, px + pw - 6, py + ph - 6, 0xFF101216);
        this.font.drawStringWithShadow(display, px + 10, py + 24, 0xFFC0C0C8);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        int panelW = 480, panelH = 300;
        int px = (this.width - panelW) / 2;
        int py = (this.height - panelH) / 2;

        if (editingColor != null) {
            if (handleColorPickerClick(mouseX, mouseY)) {
                return true;
            }
            editingColor = null;
        }

        if (importMode) {
            return true;
        }

        int sbX = px;
        int sbY = py + 22;
        int cy = sbY + 4;
        for (Category c : Category.values()) {
            if (mouseX >= sbX && mouseX < sbX + SIDEBAR_W && mouseY >= cy && mouseY < cy + ROW_H) {
                selectedCategory = c;
                scroll = 0;
                return true;
            }
            cy += ROW_H;
        }
        cy += 6 + 11;
        for (ConfigProfile p : ConfigProfile.values()) {
            if (mouseX >= sbX && mouseX < sbX + SIDEBAR_W && mouseY >= cy && mouseY < cy + 12) {
                ConfigManager.getInstance().switchProfile(p);
                return true;
            }
            cy += 12;
        }

        int bottomY = sbY + (panelH - 22) - 46;
        if (mouseX >= sbX + 2 && mouseX < sbX + SIDEBAR_W - 2 && mouseY >= bottomY && mouseY < bottomY + 13) {
            ConfigManager.getInstance().resetToDefaults();
            return true;
        }
        if (mouseX >= sbX + 2 && mouseX < sbX + SIDEBAR_W - 2 && mouseY >= bottomY + 15 && mouseY < bottomY + 28) {
            importMode = !importMode;
            importBuffer = importMode ? ConfigManager.getInstance().exportToString() : "";
            return true;
        }

        int listX = px + SIDEBAR_W;
        int listY = py + 22 + 22;
        int listW = panelW - SIDEBAR_W;

        if (selectedCategory == Category.HUD) {
            handleHudListClick(mouseX, mouseY, listX, listY, listW, button);
            return true;
        }

        List<Module> modules = visibleModules();
        int ry = listY - (int) scroll;
        for (Module m : modules) {
            if (mouseY >= ry && mouseY < ry + ROW_H) {
                handleModuleRowClick(mouseX, listX, listW, m);
                return true;
            }
            ry += ROW_H;
            if (expanded.contains(m)) {
                for (Setting<?> s : m.getSettings()) {
                    if (mouseY >= ry && mouseY < ry + SETTING_ROW_H) {
                        handleSettingClick(mouseX, listX + 14, listW - 18, s, button);
                        return true;
                    }
                    ry += SETTING_ROW_H;
                }
                ry += 4;
            }
        }
        return true;
    }

    private void handleHudListClick(double mouseX, double mouseY, int listX, int listY, int listW, int button) {
        List<net.lax1dude.eaglercraft.get.hud.HudElement> elements = visibleHudElements();
        int ry = listY - (int) scroll;
        for (net.lax1dude.eaglercraft.get.hud.HudElement e : elements) {
            if (mouseY >= ry && mouseY < ry + ROW_H) {
                int resetW = 46;
                int resetX = listX + listW - resetW - 4;
                if (mouseX >= resetX && mouseX < resetX + resetW) {
                    e.resetPosition();
                    ConfigManager.getInstance().markDirty();
                    return;
                }
                if (mouseX >= resetX - 16 && mouseX < resetX - 2) {
                    if (expandedHud.contains(e)) {
                        expandedHud.remove(e);
                    } else {
                        expandedHud.add(e);
                    }
                    return;
                }
                e.setEnabled(!e.isEnabled());
                ConfigManager.getInstance().markDirty();
                return;
            }
            ry += ROW_H;
            if (expandedHud.contains(e)) {
                ry += SETTING_ROW_H; // scale row (drag handled via mouseDragged on the whole row area not needed; use scroll wheel instead)
                for (Setting<?> s : e.getSettings()) {
                    if (mouseY >= ry && mouseY < ry + SETTING_ROW_H) {
                        handleSettingClick(mouseX, listX + 14, listW - 18, s, button);
                        return;
                    }
                    ry += SETTING_ROW_H;
                }
                ry += 4;
            }
        }
    }

    private boolean handleColorPickerClick(double mouseX, double mouseY) {
        for (int i = 0; i < 4; i++) {
            int ry = pickerY + 6 + i * 20;
            if (mouseX >= pickerX + 6 && mouseX < pickerX + pickerW - 6 && mouseY >= ry + 8 && mouseY < ry + 15) {
                draggingChannel = i;
                applyColorDrag(mouseX);
                return true;
            }
        }
        return false;
    }

    private void applyColorDrag(double mouseX) {
        if (editingColor == null || draggingChannel < 0) {
            return;
        }
        double pct = Math.max(0, Math.min(1, (mouseX - (pickerX + 6)) / (double) (pickerW - 12)));
        int v = (int) Math.round(pct * 255);
        int a = editingColor.getAlpha(), r = editingColor.getRed(), g = editingColor.getGreen(), b = editingColor.getBlue();
        switch (draggingChannel) {
            case 0: a = v; break;
            case 1: r = v; break;
            case 2: g = v; break;
            case 3: b = v; break;
        }
        editingColor.setChannel(a, r, g, b);
        ConfigManager.getInstance().markDirty();
    }

    private void handleModuleRowClick(double mouseX, int x, int w, Module m) {
        int keyW = 46;
        int keyX = x + w - keyW - 4;
        if (mouseX >= keyX && mouseX < keyX + keyW) {
            listeningForKey = m;
            return;
        }
        if (!m.getSettings().isEmpty() && mouseX >= keyX - 16 && mouseX < keyX - 2) {
            if (expanded.contains(m)) {
                expanded.remove(m);
            } else {
                expanded.add(m);
            }
            return;
        }
        m.toggle();
    }

    private void handleSettingClick(double mouseX, int x, int w, Setting<?> s, int button) {
        if (s instanceof BooleanSetting) {
            ((BooleanSetting) s).toggle();
            ConfigManager.getInstance().markDirty();
        } else if (s instanceof SliderSetting) {
            draggingSlider = s;
            applySliderDrag((SliderSetting) s, mouseX, x, w);
        } else if (s instanceof EnumSetting) {
            ((EnumSetting<?>) s).cycle();
            ConfigManager.getInstance().markDirty();
        } else if (s instanceof ColorSetting) {
            editingColor = (ColorSetting) s;
        }
    }

    private void applySliderDrag(SliderSetting slider, double mouseX, int x, int w) {
        double pct = Math.max(0, Math.min(1, (mouseX - x) / (double) w));
        slider.set(slider.getMin() + pct * (slider.getMax() - slider.getMin()));
        ConfigManager.getInstance().markDirty();
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        if (draggingChannel >= 0 && editingColor != null) {
            applyColorDrag(mouseX);
            return true;
        }
        if (draggingSlider instanceof SliderSetting) {
            int panelW = 480;
            int px = (this.width - panelW) / 2;
            int listX = px + SIDEBAR_W + 14;
            int listW = panelW - SIDEBAR_W - 18;
            applySliderDrag((SliderSetting) draggingSlider, mouseX, listX, listW);
            return true;
        }
        return super.mouseDragged(mouseX, mouseY, button, dragX, dragY);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        draggingSlider = null;
        draggingChannel = -1;
        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
        scroll -= delta * ROW_H;
        return true;
    }

    @Override
    public boolean charTyped(char typedChar, int keyCode) {
        if (importMode) {
            importBuffer += typedChar;
            return true;
        }
        if (listeningForKey == null) {
            search += typedChar;
            return true;
        }
        return super.charTyped(typedChar, keyCode);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (listeningForKey != null) {
            Module conflict = ModuleManager.getInstance().findKeyConflict(keyCode, listeningForKey);
            if (conflict != null) {
                net.lax1dude.eaglercraft.get.notification.NotificationManager.getInstance().notify(
                        "Key already used by " + conflict.getName(), net.lax1dude.eaglercraft.get.notification.NotificationType.ERROR);
            } else {
                listeningForKey.setKeyCode(keyCode == 256 ? -1 : keyCode);
                ConfigManager.getInstance().markDirty();
            }
            listeningForKey = null;
            return true;
        }
        if (importMode) {
            if (keyCode == 257) {
                ConfigManager.getInstance().importFromString(importBuffer);
                importMode = false;
            } else if (keyCode == 259 && !importBuffer.isEmpty()) {
                importBuffer = importBuffer.substring(0, importBuffer.length() - 1);
            }
            return true;
        }
        if (keyCode == 259 && !search.isEmpty()) {
            search = search.substring(0, search.length() - 1);
            return true;
        }
        if (keyCode == net.lax1dude.eaglercraft.get.GetClient.KEY_CLICKGUI || keyCode == 256) {
            Minecraft.getInstance().displayGuiScreen(null);
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    private static String keyName(int glfwKey) {
        if (glfwKey >= 65 && glfwKey <= 90) {
            return String.valueOf((char) glfwKey);
        }
        if (glfwKey >= 48 && glfwKey <= 57) {
            return String.valueOf((char) glfwKey);
        }
        switch (glfwKey) {
            case 32: return "SPACE";
            case 258: return "TAB";
            case 340: return "L-SHIFT";
            case 344: return "R-SHIFT";
            case 341: return "L-CTRL";
            case 342: return "L-ALT";
            case 260: return "INSERT";
            case 261: return "DELETE";
            default:
                if (glfwKey >= 290 && glfwKey <= 301) {
                    return "F" + (glfwKey - 289);
                }
                return "#" + glfwKey;
        }
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
