package org.daylight.numismaticscalculator.forge.replacements;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.item.ItemStack;
import org.daylight.numismaticscalculator.forge.replacements.api.ForgeFont;
import org.daylight.numismaticscalculator.forge.replacements.api.ForgeItemStack;
import org.daylight.numismaticscalculator.forge.replacements.api.ForgeScreen;
import org.daylight.numismaticscalculator.replacements.IFont;
import org.daylight.numismaticscalculator.replacements.IItemStack;
import org.daylight.numismaticscalculator.replacements.IMinecraftUtilities;
import org.daylight.numismaticscalculator.replacements.IScreen;

import java.util.ArrayList;
import java.util.List;

public class ForgeMinecraftUtilities implements IMinecraftUtilities {
    @Override
    public IFont getMinecraftFont() {
        return new ForgeFont(Minecraft.getInstance().font);
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
            if (!stack.isEmpty()) snapshot.add(new ForgeItemStack(stack.copy()));
        }

        return snapshot;
    }

    @Override
    public IScreen getScreen() {
        return new ForgeScreen(Minecraft.getInstance().screen);
    }
}
