package org.daylight.numismaticscalculator.fabric.replacements;

import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.daylight.numismaticscalculator.UiState;
import org.daylight.numismaticscalculator.fabric.replacements.api.FabricItem;
import org.daylight.numismaticscalculator.fabric.replacements.api.FabricTextureAtlasSprite;
import org.daylight.numismaticscalculator.replacements.ICoinValues;
import org.daylight.numismaticscalculator.replacements.IItem;
import org.daylight.numismaticscalculator.replacements.ITextureAtlasSprite;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class FabricCoinValues implements ICoinValues {
    public static final Map<Item, Integer> ITEM_TO_VALUE = new Object2IntOpenHashMap<>();
    public static final Map<Integer, ResourceLocation> VALUE_TO_IDENTIFIER = new Int2ObjectOpenHashMap<>();
    public static final Map<CoinTypes, Integer> TYPE_TO_VALUE = new HashMap<>();
    public static final Map<Integer, CoinTypes> VALUE_TO_COIN_TYPE = new HashMap<>();
    public static final Map<CoinTypes, Consumer<Integer>> TYPE_TO_SET_MAIN = new HashMap<>();
    public static final Map<CoinTypes, Consumer<Integer>> TYPE_TO_SET_RETURN = new HashMap<>();
    private static final Map<String, TextureAtlasSprite> NAME_TO_SPRITE = new HashMap<>();

    private static final List<ResourceLocation> numismaticsCoinIds = List.of(
            ResourceLocation.fromNamespaceAndPath("numismatics", "spur"),
            ResourceLocation.fromNamespaceAndPath("numismatics", "bevel"),
            ResourceLocation.fromNamespaceAndPath("numismatics", "sprocket"),
            ResourceLocation.fromNamespaceAndPath("numismatics", "cog"),
            ResourceLocation.fromNamespaceAndPath("numismatics", "crown"),
            ResourceLocation.fromNamespaceAndPath("numismatics", "sun")
    );

    public static void init() {
        for (ResourceLocation id : numismaticsCoinIds) {
            Item item = BuiltInRegistries.ITEM.get(id);
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

    private static final ResourceLocation BLOCK_ATLAS = ResourceLocation.fromNamespaceAndPath("minecraft", "textures/atlas/blocks.png");
    private static final ResourceLocation MISSINGNO = ResourceLocation.fromNamespaceAndPath("minecraft", "missingno");

    public static TextureAtlasSprite getMissingNo() {
        return Minecraft.getInstance()
                .getTextureAtlas(BLOCK_ATLAS)
                .apply(MISSINGNO);
    }

    private static TextureAtlasSprite getCoinSprite(String itemName) {
        Item item = BuiltInRegistries.ITEM.get(ResourceLocation.fromNamespaceAndPath("numismatics", itemName));
        if (item == null) {
            return null;
        }
        BakedModel model = Minecraft.getInstance().getItemRenderer().getModel(new ItemStack(item), null, null, 0);
        List<BakedQuad> quads = model.getQuads(null, null, RandomSource.create());
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
        TextureAtlasSprite sprite = getCoinSprite(name);
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
