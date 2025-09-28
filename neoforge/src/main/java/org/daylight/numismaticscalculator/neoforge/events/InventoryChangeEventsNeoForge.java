package org.daylight.numismaticscalculator.neoforge.events;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.common.NeoForge;
import org.daylight.numismaticscalculator.replacements.SingletonInstances;

import java.util.ArrayList;
import java.util.List;

@OnlyIn(Dist.CLIENT) // scary
public class InventoryChangeEventsNeoForge {
    private static final Minecraft MC = Minecraft.getInstance();
    private static long[] lastHashes;
    private static int tickCounter = 0;
    private static final int CHECK_INTERVAL = 10; // every ticks
    private static final List<Runnable> listeners = new ArrayList<>();

    public static void init() {
        addListener(SingletonInstances.CALCULATOR_OVERLAY::requestInventorySnapshot);
    }

    public static void addListener(Runnable runnable) {
        listeners.add(runnable);
    }

    public static void register(IEventBus modBus) {
        modBus.addListener(InventoryChangeEventsNeoForge::onClientTick);
    }


    @SubscribeEvent
    public static void onClientTick(ClientTickEvent.Post event) {
//        if (event.phase != TickEvent.ClientTickEvent.Phase.END) return;

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
        if (!stack.getComponents().isEmpty()) h = h * 31 + 1;
        return h;
    }

    private static void onInventoryChange() {
//        System.out.println("Inventory changed");
        for(Runnable r : listeners) r.run();
    }
}
