package org.daylight.numismaticscalculator.neoforge.events;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;
import org.daylight.numismaticscalculator.ModColors;
import org.daylight.numismaticscalculator.neoforge.config.ConfigData;
import org.daylight.numismaticscalculator.neoforge.replacements.NeoForgeCoinValues;

import java.util.List;

//@Mod.EventBusSubscriber(modid = CoinsCalculator.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
@OnlyIn(Dist.CLIENT)
public class TooltipEvents {
    private static final Component holdShiftHintComponent = Component.translatable("item.numismaticscalculator.coin.tooltip.total_value.inactive");

    @SubscribeEvent
    public static void onTooltip(ItemTooltipEvent event) {
        ItemStack stack = event.getItemStack();

        Integer value = NeoForgeCoinValues.ITEM_TO_VALUE.get(stack.getItem());
        if (value != null && value > 0) {
            if(!Screen.hasShiftDown() && ConfigData.requireShiftForTotalTooltip.get()) {
                event.getToolTip().add(holdShiftHintComponent);
            } else {
                Component newLine = Component.translatable("item.numismaticscalculator.coin.tooltip.total_value.active", value * stack.getCount());

                List<Component> tooltip = event.getToolTip();

                // Замена
                for (int i = 0; i < tooltip.size(); i++) {
                    Component component = tooltip.get(i);
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
