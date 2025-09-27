package org.daylight.numismaticscalculator.mixins;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.collection.DefaultedList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;

@Mixin(PlayerInventory.class)
public class PlayerInventoryMixin {
    @Shadow
    public List<DefaultedList<ItemStack>> combinedInventory;
}
