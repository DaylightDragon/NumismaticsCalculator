package org.daylight.coinscalculator.replacements;

import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.BakedQuad;
import net.minecraft.client.texture.Sprite;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.random.Random;
import org.daylight.coinscalculator.UiState;
import org.daylight.coinscalculator.replacements.api.FabricItem;
import org.daylight.coinscalculator.replacements.api.FabricTextureAtlasSprite;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class FabricCoinValues implements ICoinValues {
    public static final Map<Item, Integer> ITEM_TO_VALUE = new Object2IntOpenHashMap<>();
    public static final Map<Integer, Identifier> VALUE_TO_IDENTIFIER = new Int2ObjectOpenHashMap<>();
    public static final Map<CoinTypes, Integer> TYPE_TO_VALUE = new HashMap<>();
    public static final Map<Integer, CoinTypes> VALUE_TO_COIN_TYPE = new HashMap<>();
    public static final Map<CoinTypes, Consumer<Integer>> TYPE_TO_SET_MAIN = new HashMap<>();
    public static final Map<CoinTypes, Consumer<Integer>> TYPE_TO_SET_RETURN = new HashMap<>();
    private static final Map<String, Sprite> NAME_TO_SPRITE = new HashMap<>();

    private static final List<Identifier> numismaticsCoinIds = List.of(
            new Identifier("numismatics", "spur"),
            new Identifier("numismatics", "bevel"),
            new Identifier("numismatics", "sprocket"),
            new Identifier("numismatics", "cog"),
            new Identifier("numismatics", "crown"),
            new Identifier("numismatics", "sun")
    );

    public static void init() {
        for (Identifier id : numismaticsCoinIds) {
            Item item = Registries.ITEM.get(id);
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
                VALUE_TO_IDENTIFIER.put(value, id);
            }
        }

        TYPE_TO_VALUE.put(CoinTypes.SPUR, 1);
        TYPE_TO_VALUE.put(CoinTypes.BEVEL, 8);
        TYPE_TO_VALUE.put(CoinTypes.SPROCKET, 16);
        TYPE_TO_VALUE.put(CoinTypes.COG, 64);
        TYPE_TO_VALUE.put(CoinTypes.CROWN, 512);
        TYPE_TO_VALUE.put(CoinTypes.SUN, 4096);

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

    private static final Identifier BLOCK_ATLAS = new Identifier("minecraft", "textures/atlas/blocks.png");
    private static final Identifier MISSINGNO = new Identifier("minecraft", "missingno");

    public static Sprite getMissingNo() {
        return MinecraftClient.getInstance()
                .getSpriteAtlas(BLOCK_ATLAS)
                .apply(MISSINGNO);
    }

    private static Sprite getCoinSprite(String itemName) {
        Item item = Registries.ITEM.get(new Identifier("numismatics", itemName));
        if (item == null) {
            return null;
        }
        BakedModel model = MinecraftClient.getInstance().getItemRenderer().getModel(new ItemStack(item), null, null, 0);
        List<BakedQuad> quads = model.getQuads(null, null, Random.create());
        if (!quads.isEmpty()) {
            return quads.get(0).getSprite();
        }
        return null;
    }


    @Override
    public ITextureAtlasSprite getAtlasSpriteByName(String name) {
        if (NAME_TO_SPRITE.containsKey(name)) {
            return new FabricTextureAtlasSprite(NAME_TO_SPRITE.get(name));
        }
        Sprite sprite = getCoinSprite(name);
        if (sprite != null) {
            NAME_TO_SPRITE.put(name, sprite);
            return new FabricTextureAtlasSprite(sprite);
        }
        return new FabricTextureAtlasSprite(getMissingNo());
    }

    @Override
    public Consumer<Integer> getReturnCoinSetter(CoinTypes type) {
        return TYPE_TO_SET_RETURN.get(type);
    }

    @Override
    public Consumer<Integer> getMainCoinSetter(CoinTypes type) {
        return TYPE_TO_SET_MAIN.get(type);
    }

    @Override
    public CoinTypes getCoinTypeByValue(int value) {
        return VALUE_TO_COIN_TYPE.get(value);
    }

    @Override
    public Integer getCoinValueByItem(IItem item) {
        if(!(item instanceof FabricItem fabricItem)) throw new IllegalArgumentException();
        return ITEM_TO_VALUE.getOrDefault(fabricItem.getDelegate(), 0);
    }

    @Override
    public void resetAllMainCoins() {
        for(Consumer<Integer> consumer : TYPE_TO_SET_MAIN.values()) {
            consumer.accept(0);
        }
    }

    @Override
    public void resetAllReturnCoins() {
        for(Consumer<Integer> consumer : TYPE_TO_SET_RETURN.values()) {
            consumer.accept(0);
        }
    }
}
