package org.daylight.numismaticscalculator.fabric.event;

import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableTextContent;
import org.daylight.numismaticscalculator.ModColors;
import org.daylight.numismaticscalculator.fabric.config.ConfigHandler;
import org.daylight.numismaticscalculator.fabric.replacements.FabricCoinValues;

public class FabricTooltipEvents {
    private static final Text holdShiftHintComponent = Text.literal("Hold ").setStyle(Style.EMPTY.withColor(ModColors.tooltipGrayColor)) // gray
            .append(Text.literal("SHIFT").setStyle(Style.EMPTY.withColor(ModColors.tooltipHighlightColor).withBold(true)))
            .append(Text.literal(" to view total value").setStyle(Style.EMPTY.withColor(ModColors.tooltipGrayColor)));

    public static void register() {
        ItemTooltipCallback.EVENT.register((stack, tooltipContext, tooltipType, lines) -> {
            Integer value = FabricCoinValues.ITEM_TO_VALUE.get(stack.getItem());
            if (value != null && value > 0) {
                if (!Screen.hasShiftDown() && ConfigHandler.requireShiftForTotalTooltip.get()) {
                    lines.add(holdShiftHintComponent);
                } else {
                    Text newLine = Text.literal("Value (Total): ")
                            .setStyle(Style.EMPTY.withColor(0xFFFFFF))
                            .append(Text.literal((value * stack.getCount()) + "¤")
                                    .setStyle(Style.EMPTY.withColor(ModColors.tooltipHighlightColor))); // gold

                    // заменяем старую строку
                    for (int i = 0; i < lines.size(); i++) {
                        Text component = lines.get(i);
                        // Проверяем, является ли компонент translatable и содержит ли нужный ключ
                        if (component.getContent() instanceof TranslatableTextContent translatable) {
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
