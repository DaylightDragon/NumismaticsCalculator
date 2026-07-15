package org.daylight.numismaticscalculator.fabric.event;

import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.contents.TranslatableContents;
import org.daylight.numismaticscalculator.ModColors;
import org.daylight.numismaticscalculator.fabric.config.ConfigHandler;
import org.daylight.numismaticscalculator.fabric.replacements.FabricCoinValues;

public class FabricTooltipEvents {
    private static final Component holdShiftHintComponent = Component.translatable("item.numismaticscalculator.coin.tooltip.total_value.inactive");

    public static void register() {
        ItemTooltipCallback.EVENT.register((stack, tooltipContext, tooltipType, lines) -> {
            Integer value = FabricCoinValues.ITEM_TO_VALUE.get(stack.getItem());
            if (value != null && value > 0) {
                if (!Screen.hasShiftDown() && ConfigHandler.requireShiftForTotalTooltip.get()) {
                    lines.add(holdShiftHintComponent);
                } else {
                    Component newLine = Component.translatable("item.numismaticscalculator.coin.tooltip.total_value.active", value * stack.getCount());

                    // заменяем старую строку
                    for (int i = 0; i < lines.size(); i++) {
                        Component component = lines.get(i);
                        // Проверяем, является ли компонент translatable и содержит ли нужный ключ
                        if (component.getContents() instanceof TranslatableContents translatable) {
                            String key = translatable.getKey();
                            if ("item.numismatics.coin.tooltip.value.basic".equals(key) ||
                                    "item.numismatics.coin.tooltip.value".equals(key)) {
                                lines.set(i, newLine);
                                break;
                            }
                        }
                    }
                }
            }
        });
    }
}
