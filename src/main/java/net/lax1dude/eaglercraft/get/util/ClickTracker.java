package net.lax1dude.eaglercraft.get.util;

import java.util.ArrayDeque;
import java.util.Deque;

import net.lax1dude.eaglercraft.EagRuntime;

/** Tracks left/right click timestamps to compute an accurate clicks-per-second value. */
public final class ClickTracker {

    private static final long WINDOW_MS = 1000L;

    private static final Deque<Long> leftClicks = new ArrayDeque<>();
    private static final Deque<Long> rightClicks = new ArrayDeque<>();

    private ClickTracker() {
    }

    public static void recordLeftClick() {
        leftClicks.addLast(EagRuntime.steadyTimeMillis());
        trim(leftClicks);
    }

    public static void recordRightClick() {
        rightClicks.addLast(EagRuntime.steadyTimeMillis());
        trim(rightClicks);
    }

    private static void trim(Deque<Long> deque) {
        long now = EagRuntime.steadyTimeMillis();
        while (!deque.isEmpty() && now - deque.peekFirst() > WINDOW_MS) {
            deque.pollFirst();
        }
    }

    public static int getLeftCps() {
        trim(leftClicks);
        return leftClicks.size();
    }

    public static int getRightCps() {
        trim(rightClicks);
        return rightClicks.size();
    }
}
