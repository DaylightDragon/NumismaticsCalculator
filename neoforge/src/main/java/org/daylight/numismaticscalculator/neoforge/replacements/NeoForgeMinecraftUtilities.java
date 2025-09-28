package org.daylight.numismaticscalculator.neoforge.replacements;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.item.ItemStack;
import org.daylight.numismaticscalculator.neoforge.replacements.api.NeoForgeFont;
import org.daylight.numismaticscalculator.neoforge.replacements.api.NeoForgeItemStack;
import org.daylight.numismaticscalculator.neoforge.replacements.api.NeoForgeScreen;
import org.daylight.numismaticscalculator.replacements.IFont;
import org.daylight.numismaticscalculator.replacements.IItemStack;
import org.daylight.numismaticscalculator.replacements.IMinecraftUtilities;
import org.daylight.numismaticscalculator.replacements.IScreen;

import java.util.ArrayList;
import java.util.List;

public class NeoForgeMinecraftUtilities implements IMinecraftUtilities {
    @Override
    public IFont getMinecraftFont() {
        return new NeoForgeFont(Minecraft.getInstance().font);
    }

    @Override
    public void execute(Runnable runnable) {
        Minecraft.getInstance().execute(runnable);
    }

    @Override
    public List<IItemStack> getInventoryItems() {
        LocalPlayer player = Minecraft.getInstance().player;
        if (player == null) return new ArrayList<>();

        List<IItemStack> snapshot = new ArrayList<>();
        for (ItemStack stack : player.getInventory().items) {
            if (!stack.isEmpty()) snapshot.add(new NeoForgeItemStack(stack.copy()));
        }

        return snapshot;
    }

    @Override
    public IScreen getScreen() {
        return new NeoForgeScreen(Minecraft.getInstance().screen);
    }
}
