package org.daylight.coinscalculator;

import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class CoinValues {
    public enum CoinTypes {
        SPUR,
        BEVEL,
        SPROCKET,
        COG,
        CROWN,
        SUN
    }

    public static final Map<Item, Integer> ITEM_TO_VALUE = new Object2IntOpenHashMap<>();
    public static final Map<Integer, ResourceLocation> VALUE_TO_RESOURCE_LOCATION = new Int2ObjectOpenHashMap<>();
    public static final Map<CoinTypes, Integer> ITEM_NAME_TO_VALUE = new HashMap<>();
    public static final Map<Integer, CoinTypes> VALUE_TO_COIN_TYPE = new HashMap<>();
    public static final Map<CoinTypes, Consumer<Integer>> TYPE_TO_SET_MAIN = new HashMap<>(); // ask ai later
    public static final Map<CoinTypes, Consumer<Integer>> TYPE_TO_SET_RETURN = new HashMap<>(); // ask ai later

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
                VALUE_TO_RESOURCE_LOCATION.put(value, id);
            }
        }

        ITEM_NAME_TO_VALUE.put(CoinTypes.SPUR, 1);
        ITEM_NAME_TO_VALUE.put(CoinTypes.BEVEL, 8);
        ITEM_NAME_TO_VALUE.put(CoinTypes.SPROCKET, 16);
        ITEM_NAME_TO_VALUE.put(CoinTypes.COG, 64);
        ITEM_NAME_TO_VALUE.put(CoinTypes.CROWN, 512);
        ITEM_NAME_TO_VALUE.put(CoinTypes.SUN, 4096);

        VALUE_TO_COIN_TYPE.put(1, CoinTypes.SPUR);
        VALUE_TO_COIN_TYPE.put(8, CoinTypes.BEVEL);
        VALUE_TO_COIN_TYPE.put(16, CoinTypes.SPROCKET);
        VALUE_TO_COIN_TYPE.put(64, CoinTypes.COG);
        VALUE_TO_COIN_TYPE.put(512, CoinTypes.CROWN);
        VALUE_TO_COIN_TYPE.put(4096, CoinTypes.SUN);

        TYPE_TO_SET_MAIN.put(CoinTypes.SPUR, value -> UiState.conversionSpurMain = value);
        TYPE_TO_SET_MAIN.put(CoinTypes.BEVEL, value -> UiState.conversionBevelMain = value);
        TYPE_TO_SET_MAIN.put(CoinTypes.SPROCKET, value -> UiState.conversionSprocketMain = value);
        TYPE_TO_SET_MAIN.put(CoinTypes.COG, value -> UiState.conversionCogMain = value);
        TYPE_TO_SET_MAIN.put(CoinTypes.CROWN, value -> UiState.conversionCrownMain = value);
        TYPE_TO_SET_MAIN.put(CoinTypes.SUN, value -> UiState.conversionSunMain = value);

        TYPE_TO_SET_RETURN.put(CoinTypes.SPUR, value -> UiState.conversionSpurOverpay = value);
        TYPE_TO_SET_RETURN.put(CoinTypes.BEVEL, value -> UiState.conversionBevelOverpay = value);
        TYPE_TO_SET_RETURN.put(CoinTypes.SPROCKET, value -> UiState.conversionSprocketOverpay = value);
        TYPE_TO_SET_RETURN.put(CoinTypes.COG, value -> UiState.conversionCogOverpay = value);
        TYPE_TO_SET_RETURN.put(CoinTypes.CROWN, value -> UiState.conversionCrownOverpay = value);
        TYPE_TO_SET_RETURN.put(CoinTypes.SUN, value -> UiState.conversionSunOverpay = value);
    }
}
