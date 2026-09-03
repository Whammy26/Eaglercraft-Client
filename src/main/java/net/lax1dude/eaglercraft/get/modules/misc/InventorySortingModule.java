package net.lax1dude.eaglercraft.get.modules.misc;

import net.lax1dude.eaglercraft.get.Category;
import net.lax1dude.eaglercraft.get.module.Module;
import net.lax1dude.eaglercraft.get.notification.NotificationManager;
import net.lax1dude.eaglercraft.get.notification.NotificationType;
import net.minecraft.client.Minecraft;
import net.minecraft.inventory.container.ClickType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;

/**
 * Sorts the main inventory + hotbar using real click packets (the same swap sequence a
 * player performs by hand), so it works identically in survival and creative.
 */
public class InventorySortingModule extends Module {

    public InventorySortingModule() {
        super("Inventory Sorting", "Sorts your main inventory and hotbar by item type", Category.MISC, false, -1);
    }

    @Override
    protected void onEnable() {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || mc.player.openContainer == null) {
            setEnabled(false);
            return;
        }
        sort(mc);
        setEnabled(false);
    }

    private void sort(Minecraft mc) {
        int windowId = mc.player.openContainer.windowId;
        java.util.List<Slot> slots = mc.player.openContainer.inventorySlots;
        int start = -1, end = -1;
        for (int i = 0; i < slots.size(); i++) {
            if (slots.get(i).inventory == mc.player.inventory) {
                if (start == -1) {
                    start = i;
                }
                end = i;
            }
        }
        if (start == -1) {
            return;
        }
        // exclude armor/offhand slots which have restricted item types; only touch the 36 main+hotbar slots
        int count = Math.min(36, end - start + 1);

        // selection sort: for each position, find the smallest remaining item and swap it in,
        // re-reading live slot contents after every swap (windowClick predicts client-side instantly)
        for (int i = 0; i < count; i++) {
            int bestSlot = start + i;
            String bestKey = compareKey(slots.get(bestSlot).getStack());
            for (int j = i + 1; j < count; j++) {
                int candidate = start + j;
                String key = compareKey(slots.get(candidate).getStack());
                if (key.compareTo(bestKey) < 0) {
                    bestSlot = candidate;
                    bestKey = key;
                }
            }
            if (bestSlot != start + i) {
                swap(mc, windowId, start + i, bestSlot);
            }
        }
        NotificationManager.getInstance().notify("Inventory sorted", NotificationType.SUCCESS);
    }

    private static String compareKey(ItemStack stack) {
        if (stack.isEmpty()) {
            return "\uFFFF";
        }
        return String.valueOf(net.minecraft.util.registry.Registry.ITEM.getKey(stack.getItem()));
    }

    private void swap(Minecraft mc, int windowId, int a, int b) {
        if (a == b) {
            return;
        }
        mc.playerController.windowClick(windowId, a, 0, ClickType.PICKUP, mc.player);
        mc.playerController.windowClick(windowId, b, 0, ClickType.PICKUP, mc.player);
        mc.playerController.windowClick(windowId, a, 0, ClickType.PICKUP, mc.player);
    }
}
