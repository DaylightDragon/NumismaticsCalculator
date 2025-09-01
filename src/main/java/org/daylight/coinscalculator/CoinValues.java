package org.daylight.coinscalculator;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;

public class CoinValues {
    public static final Map<Item, Integer> ITEM_TO_VALUE = new IdentityHashMap<>();

    static {
        for (ResourceLocation id : List.of(
                ResourceLocation.parse("numismatics:spur"),
                ResourceLocation.parse("numismatics:bevel"),
                ResourceLocation.parse("numismatics:sprocket"),
                ResourceLocation.parse("numismatics:cog"),
                ResourceLocation.parse("numismatics:crown"),
                ResourceLocation.parse("numismatics:sun")
        )) {
            Item item = ForgeRegistries.ITEMS.getValue(id);
            if (item != null) {
                int value = switch (id.getPath()) {
                    case "spur" -> 1;
                    case "bevel" -> 8;
                    case "sprocket" -> 16;
                    case "cog" -> 64;
                    case "crown" -> 512;
                    case "sun" -> 4096;
                    default -> 0;
                };
                ITEM_TO_VALUE.put(item, value);
            }
        }
    }
}
