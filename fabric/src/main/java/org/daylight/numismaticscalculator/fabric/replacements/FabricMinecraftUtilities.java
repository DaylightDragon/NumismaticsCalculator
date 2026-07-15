package org.daylight.numismaticscalculator.fabric.replacements;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.item.ItemStack;
import org.daylight.numismaticscalculator.fabric.replacements.api.FabricFont;
import org.daylight.numismaticscalculator.fabric.replacements.api.FabricItemStack;
import org.daylight.numismaticscalculator.fabric.replacements.api.FabricScreen;
import org.daylight.numismaticscalculator.replacements.IFont;
import org.daylight.numismaticscalculator.replacements.IItemStack;
import org.daylight.numismaticscalculator.replacements.IMinecraftUtilities;
import org.daylight.numismaticscalculator.replacements.IScreen;

import java.util.ArrayList;
import java.util.List;

public class FabricMinecraftUtilities implements IMinecraftUtilities {
    @Override
    public IFont getMinecraftFont() {
        return new FabricFont(Minecraft.getInstance().font);
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
            if (!stack.isEmpty()) snapshot.add(new FabricItemStack(stack.copy()));
        }
        for (ItemStack stack : player.getInventory().offhand) {
            if (!stack.isEmpty()) snapshot.add(new FabricItemStack(stack.copy()));
        }
        for (ItemStack stack : player.getInventory().armor) {
            if (!stack.isEmpty()) snapshot.add(new FabricItemStack(stack.copy()));
        }

        return snapshot;
    }

    @Override
    public IScreen getScreen() {
        return new FabricScreen(Minecraft.getInstance().screen);
    }
}
