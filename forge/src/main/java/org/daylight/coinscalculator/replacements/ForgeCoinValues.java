package org.daylight.coinscalculator.replacements;

import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.extensions.IForgeBakedModel;
import net.minecraftforge.client.model.data.ModelData;
import net.minecraftforge.registries.ForgeRegistries;
import org.daylight.coinscalculator.UiState;
import org.daylight.coinscalculator.replacements.api.ForgeItem;
import org.daylight.coinscalculator.replacements.api.ForgeTextureAtlasSprite;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class ForgeCoinValues implements ICoinValues {
    public static final Map<Item, Integer> ITEM_TO_VALUE = new Object2IntOpenHashMap<>();
    public static final Map<Integer, ResourceLocation> VALUE_TO_RESOURCE_LOCATION = new Int2ObjectOpenHashMap<>();
    public static final Map<CoinTypes, Integer> TYPE_TO_VALUE = new HashMap<>();
    public static final Map<Integer, CoinTypes> VALUE_TO_COIN_TYPE = new HashMap<>();
    public static final Map<CoinTypes, Consumer<Integer>> TYPE_TO_SET_MAIN = new HashMap<>(); // ask ai about map impl  later
    public static final Map<CoinTypes, Consumer<Integer>> TYPE_TO_SET_RETURN = new HashMap<>(); // ask ai about map impl later
    private static final Map<String, TextureAtlasSprite> NAME_TO_TEXTURE_ATLAS_SPRITE = new HashMap<>();

    private static List<ResourceLocation> numismaticsCoinLocations = List.of(
            ResourceLocation.parse("numismatics:spur"),
            ResourceLocation.parse("numismatics:bevel"),
            ResourceLocation.parse("numismatics:sprocket"),
            ResourceLocation.parse("numismatics:cog"),
            ResourceLocation.parse("numismatics:crown"),
            ResourceLocation.parse("numismatics:sun")
    );

    public static void init() {
        for (ResourceLocation id : numismaticsCoinLocations) {
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
//                NAME_TO_TEXTURE_ATLAS_SPRITE.put(id.getPath(), getCoinResourceLocation(id.getPath()));
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
    private static final ResourceLocation MISSINGNO = ResourceLocation.parse("minecraft:missingno");

    public static TextureAtlasSprite getMissingNo() {
        return Minecraft.getInstance().getTextureAtlas(BLOCK_ATLAS).apply(MISSINGNO);
    }

    private static TextureAtlasSprite getCoinResourceLocation(String itemName) {
        Item item = ForgeRegistries.ITEMS.getValue(ResourceLocation.parse("numismatics:" + itemName));
        if(item == null) {
//            System.out.println("item: " + item + ", level: " + Minecraft.getInstance().level);
            return null;
        }
        BakedModel model = Minecraft.getInstance().getItemRenderer().getModel(new ItemStack(item), null, null, 0);

        List<BakedQuad> quads = ((IForgeBakedModel) model).getQuads(
                null,                // BlockState (null для предмета)
                null,                // Direction (null = all sides)
                RandomSource.create(), // Random source
                ModelData.EMPTY,     // No extra data
                null                 // RenderType (null = all)
        );

        if (!quads.isEmpty()) {
            return quads.get(0).getSprite();
        }

//        System.out.println("quads empty");
        return null;
    }

    @Override
    public ITextureAtlasSprite getAtlasSpriteByName(String name) {
        if(NAME_TO_TEXTURE_ATLAS_SPRITE.containsKey(name)) return new ForgeTextureAtlasSprite(NAME_TO_TEXTURE_ATLAS_SPRITE.get(name));
        TextureAtlasSprite sprite = getCoinResourceLocation(name);
        if(sprite != null) {
            NAME_TO_TEXTURE_ATLAS_SPRITE.put(name, sprite);
            return new ForgeTextureAtlasSprite(sprite);
        }
        else return new ForgeTextureAtlasSprite(getMissingNo());
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
        if(!(item instanceof ForgeItem forgeItem)) throw new IllegalArgumentException();
        return ITEM_TO_VALUE.getOrDefault(forgeItem.getDelegate(), 0);
    }

    @Override
    public void resetAllMainCoins() {
        for(Consumer<Integer> consumer : TYPE_TO_SET_RETURN.values()) {
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
