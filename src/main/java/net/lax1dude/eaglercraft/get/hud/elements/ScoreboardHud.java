package net.lax1dude.eaglercraft.get.hud.elements;

import java.util.Collection;

import net.lax1dude.eaglercraft.get.hud.HudElement;
import net.lax1dude.eaglercraft.get.util.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.Scoreboard;

/** A clean-styled replacement for the vanilla sidebar scoreboard. When enabled, the vanilla one is suppressed. */
public class ScoreboardHud extends HudElement {

    public ScoreboardHud() {
        super("Custom Scoreboard", 0.8, 0.28, false);
    }

    public static boolean isSuppressingVanilla() {
        net.lax1dude.eaglercraft.get.hud.HudElement e = net.lax1dude.eaglercraft.get.hud.HudManager.getInstance().getByName("Custom Scoreboard");
        return e != null && e.isEnabled();
    }

    @Override
    public void render(int screenWidth, int screenHeight, float partialTicks) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || mc.world == null) {
            return;
        }
        Scoreboard scoreboard = mc.world.getScoreboard();
        ScoreObjective objective = scoreboard.getObjectiveInDisplaySlot(1);
        if (objective == null) {
            return;
        }
        Collection<Score> scores = scoreboard.getSortedScores(objective);
        int x = getPixelX(screenWidth);
        int y = getPixelY(screenHeight);
        String title = objective.getDisplayName().getFormattedText();
        int width = mc.fontRenderer.getStringWidth(title) + 12;
        int lineHeight = 10;
        int shown = Math.min(scores.size(), 15);
        for (Score s : scores) {
            width = Math.max(width, mc.fontRenderer.getStringWidth(s.getPlayerName()) + mc.fontRenderer.getStringWidth(String.valueOf(s.getScorePoints())) + 24);
        }
        int height = 16 + shown * lineHeight;
        RenderUtil.panel(x, y, width, height);
        RenderUtil.drawCentered(mc.fontRenderer, title, x + width / 2, y + 3, RenderUtil.ACCENT | 0xFF000000);
        int row = 0;
        for (Score s : scores) {
            if (row >= shown) {
                break;
            }
            int ly = y + 16 + row * lineHeight;
            mc.fontRenderer.drawStringWithShadow(s.getPlayerName(), x + 4, ly, RenderUtil.TEXT);
            String pts = String.valueOf(s.getScorePoints());
            mc.fontRenderer.drawStringWithShadow(pts, x + width - mc.fontRenderer.getStringWidth(pts) - 4, ly, RenderUtil.TEXT_DIM);
            row++;
        }
        setLastSize(width, height);
    }
}
