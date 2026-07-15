package org.daylight.numismaticscalculator.forge.events;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.daylight.numismaticscalculator.ModColors;
import org.daylight.numismaticscalculator.forge.config.ConfigData;
import org.daylight.numismaticscalculator.forge.replacements.ForgeCoinValues;

import java.util.List;

//@Mod.EventBusSubscriber(modid = CoinsCalculator.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
@OnlyIn(Dist.CLIENT)
public class TooltipEvents {
    private static final Component holdShiftHintComponent = Component.translatable("item.numismaticscalculator.coin.tooltip.total_value.inactive");

    @SubscribeEvent
    public static void onTooltip(ItemTooltipEvent event) {
        ItemStack stack = event.getItemStack();

        Integer value = ForgeCoinValues.ITEM_TO_VALUE.get(stack.getItem());
        if (value != null && value > 0) {
            if(!Screen.hasShiftDown() && ConfigData.requireShiftForTotalTooltip.get()) {
                event.getToolTip().add(holdShiftHintComponent);
            } else {
                Component newLine = Component.translatable("item.numismaticscalculator.coin.tooltip.total_value.active", value * stack.getCount());

                List<Component> tooltip = event.getToolTip();

                // Замена
                for (int i = 0; i < tooltip.size(); i++) {
                    Component component = tooltip.get(i);
                    // Проверяем, является ли компонент translatable и содержит ли нужный ключ
                    if (component.getContents() instanceof TranslatableContents translatable) {
                        String key = translatable.getKey();
                        if ("item.numismatics.coin.tooltip.value.basic".equals(key) ||
                                "item.numismatics.coin.tooltip.value".equals(key)) {
                            tooltip.set(i, newLine);
                            break;
                        }
                    }
                }
            }
        }
    }
}
