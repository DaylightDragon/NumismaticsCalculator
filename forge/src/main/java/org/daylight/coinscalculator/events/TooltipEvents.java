package org.daylight.coinscalculator.events;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.daylight.coinscalculator.replacements.ForgeCoinValues;
import org.daylight.coinscalculator.config.ConfigData;

import java.util.List;

//@Mod.EventBusSubscriber(modid = CoinsCalculator.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
@OnlyIn(Dist.CLIENT)
public class TooltipEvents {
    private static final Component holdShiftHintComponent = Component.literal("Hold ").withStyle(ChatFormatting.GRAY)
            .append(Component.literal("SHIFT").withStyle(ChatFormatting.GOLD).withStyle(ChatFormatting.BOLD))
            .append(Component.literal(" to view total value").withStyle(ChatFormatting.GRAY));

    @SubscribeEvent
    public static void onTooltip(ItemTooltipEvent event) {
        ItemStack stack = event.getItemStack();

        Integer value = ForgeCoinValues.ITEM_TO_VALUE.get(stack.getItem());
        if (value != null && value > 0) {
            if(!Screen.hasShiftDown() && ConfigData.requireShiftForTotalTooltip.get()) {
                event.getToolTip().add(holdShiftHintComponent);
            } else {
                Component newLine = Component.literal("Value (Total): ").withStyle(ChatFormatting.WHITE)
                        .append(Component.literal(value * stack.getCount() + "¤").withStyle(ChatFormatting.GOLD)); //.withStyle(ChatFormatting.BOLD))
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
