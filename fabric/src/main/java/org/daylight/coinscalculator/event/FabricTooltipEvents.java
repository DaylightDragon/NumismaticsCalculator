package org.daylight.coinscalculator.event;

import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import org.daylight.coinscalculator.config.ConfigHandler;
import org.daylight.coinscalculator.replacements.FabricCoinValues;

public class FabricTooltipEvents {
    private static final Text holdShiftHintComponent = Text.literal("Hold ").setStyle(Style.EMPTY.withColor(0x808080)) // gray
            .append(Text.literal("SHIFT").setStyle(Style.EMPTY.withColor(0xFFD700).withBold(true)))
            .append(Text.literal(" to view total value").setStyle(Style.EMPTY.withColor(0x808080)));

    public static void register() {
        ItemTooltipCallback.EVENT.register((stack, context, lines) -> {
            Integer value = FabricCoinValues.ITEM_TO_VALUE.get(stack.getItem());
            if (value != null && value > 0) {
                if (!Screen.hasShiftDown() && ConfigHandler.requireShiftForTotalTooltip.get()) {
                    lines.add(holdShiftHintComponent);
                } else {
                    Text newLine = Text.literal("Value (Total): ")
                            .setStyle(Style.EMPTY.withColor(0xFFFFFF))
                            .append(Text.literal((value * stack.getCount()) + "¤")
                                    .setStyle(Style.EMPTY.withColor(0xFFD700))); // gold

                    // заменяем старую строку с "Value:" если есть
                    for (int i = 0; i < lines.size(); i++) {
                        Text component = lines.get(i);
                        if (component.getString().contains("Value:")) {
                            lines.set(i, newLine);
                        }
                    }
                }
            }
        });
    }
}
