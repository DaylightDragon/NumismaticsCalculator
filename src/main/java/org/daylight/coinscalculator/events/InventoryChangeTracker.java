package org.daylight.coinscalculator.events;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.daylight.coinscalculator.ui.overlays.CalculatorOverlay;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber(value = Dist.CLIENT)
public class InventoryChangeTracker {
    private static final Minecraft MC = Minecraft.getInstance();
    private static long[] lastHashes;
    private static int tickCounter = 0;
    private static final int CHECK_INTERVAL = 10; // каждые 10 тиков (~0.5 сек)
    private static final List<Runnable> listeners = new ArrayList<>();

    static {
        addListener(CalculatorOverlay::requestInventorySnapshot);
    }

    public static void addListener(Runnable runnable) {
        listeners.add(runnable);
    }

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.ClientTickEvent.Phase.END) return;

        tickCounter++;
        if (tickCounter < CHECK_INTERVAL && lastHashes != null) return;
        tickCounter = 0;

        Player player = MC.player;
        if (player == null) return;

        var items = player.getInventory().items; // Только 36 слотов
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

        if (changed) {
            onInventoryChange();
        }
    }

    private static long computeHash(ItemStack stack) {
        if (stack.isEmpty()) return 0L;
        long h = stack.getItem().hashCode();
        h = h * 31 + stack.getCount();
        if (stack.hasTag()) h = h * 31 + 1; // Только факт наличия NBT, без деталей
        return h;
    }

    private static void onInventoryChange() {
        System.out.println("Inventory changed");
        for(Runnable r : listeners) r.run();
    }
}
