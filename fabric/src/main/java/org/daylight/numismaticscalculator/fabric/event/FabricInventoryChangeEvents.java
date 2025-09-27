package org.daylight.numismaticscalculator.fabric.event;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import org.daylight.numismaticscalculator.replacements.SingletonInstances;

import java.util.ArrayList;
import java.util.List;

public class FabricInventoryChangeEvents {
    private static final MinecraftClient MC = MinecraftClient.getInstance();
    private static long[] lastHashes;
    private static int tickCounter = 0;
    private static final int CHECK_INTERVAL = 10; // every ticks
    private static final List<Runnable> listeners = new ArrayList<>();

    static {
        addListener(SingletonInstances.CALCULATOR_OVERLAY::requestInventorySnapshot);
    }

    public static void addListener(Runnable runnable) {
        listeners.add(runnable);
    }

    public static void register() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> tick());
    }

    private static void tick() {
        tickCounter++;
        ClientPlayerEntity player = MC.player;
        if (player == null) return;

        PlayerInventory inventory = player.getInventory();

        List<ItemStack> items = new ArrayList<>();
        items.addAll(inventory.main);
        items.addAll(inventory.offHand);

        if (tickCounter < CHECK_INTERVAL && lastHashes != null) return;
        tickCounter = 0;

        if (lastHashes == null) {
            lastHashes = new long[items.size()];
            for (int i = 0; i < items.size(); i++) {
                lastHashes[i] = computeHash(items.get(i));
            }
            onInventoryChange();
            return;
        }

        boolean changed = false;
        for (int i = 0; i < items.size(); i++) {
            long newHash = computeHash(items.get(i));
            if (lastHashes[i] != newHash) {
                lastHashes[i] = newHash;
                changed = true;
            }
        }

        if (changed) onInventoryChange();
    }

    private static long computeHash(ItemStack stack) {
        if (stack.isEmpty()) return 0L;
        long h = stack.getItem().hashCode();
        h = h * 31 + stack.getCount();
        if (stack.getNbt() != null) h = h * 31 + 1;
        return h;
    }

    public static void onInventoryChange() {
        for (Runnable r : listeners) r.run();
    }
}
