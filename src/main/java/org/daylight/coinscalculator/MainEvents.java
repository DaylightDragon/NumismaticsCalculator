package org.daylight.coinscalculator;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.ForgeRegistries;

public class MainEvents {
    @SubscribeEvent
    public static void onTooltip(ItemTooltipEvent event) {
        ItemStack stack = event.getItemStack();

        // Проверяем конкретный предмет (например, из другого мода)
        if (stack.getItem() == ForgeRegistries.ITEMS.getValue(ResourceLocation.parse("othermod:example_item"))) {
            // Удаляем все старые строки тултипа (если нужно)
            event.getToolTip().clear();

            // Добавляем свой тултип
            event.getToolTip().add(Component.literal("My Custom Tooltip").withStyle(ChatFormatting.GOLD));

            // Можно динамически добавить данные (например, NBT)
            if (stack.hasTag()) {
                event.getToolTip().add(Component.literal("NBT: " + stack.getTag().toString()));
            }
        }
    }

}
