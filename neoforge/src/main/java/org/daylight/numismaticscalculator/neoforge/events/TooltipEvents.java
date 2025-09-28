package org.daylight.numismaticscalculator.neoforge.events;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
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
    private static final Component holdShiftHintComponent = Component.literal("Hold ").withStyle(style -> style.withColor(ModColors.tooltipGrayColor))
            .append(Component.literal("SHIFT").withStyle(style -> style.withColor(ModColors.tooltipHighlightColor)).withStyle(ChatFormatting.BOLD))
            .append(Component.literal(" to view total value").withStyle(style -> style.withColor(ModColors.tooltipGrayColor)));

    @SubscribeEvent
    public static void onTooltip(ItemTooltipEvent event) {
        ItemStack stack = event.getItemStack();

        Integer value = NeoForgeCoinValues.ITEM_TO_VALUE.get(stack.getItem());
        if (value != null && value > 0) {
            if(!Screen.hasShiftDown() && ConfigData.requireShiftForTotalTooltip.get()) {
                event.getToolTip().add(holdShiftHintComponent);
            } else {
                Component newLine = Component.literal("Value (Total): ").withStyle(ChatFormatting.WHITE)
                        .append(Component.literal(value * stack.getCount() + "¤").withStyle(style -> style.withColor(ModColors.tooltipHighlightColor))); //.withStyle(ChatFormatting.BOLD))
//                                .append(Component.literal("").withStyle(ChatFormatting.WHITE))

                List<Component> tooltip = event.getToolTip();
                for(int i = 0; i < tooltip.size(); i++) {
                    Component component = tooltip.get(i);
                    if(component.getString().contains("Value:")) {
                        tooltip.set(i, newLine);
                    }
                }
            }
        }
    }
}
