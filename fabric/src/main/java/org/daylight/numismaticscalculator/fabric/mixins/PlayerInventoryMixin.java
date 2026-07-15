package org.daylight.numismaticscalculator.fabric.mixins;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;

@Mixin(Inventory.class)
public class PlayerInventoryMixin {
//    @Shadow
//    public List<DefaultedList<ItemStack>> combinedInventory;
}
