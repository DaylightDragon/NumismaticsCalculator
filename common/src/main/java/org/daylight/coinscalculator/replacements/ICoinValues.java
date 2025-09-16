package org.daylight.coinscalculator.replacements;

import java.util.function.Consumer;

public interface ICoinValues {
    void resetAllMainCoins();

    void resetAllReturnCoins();

    Integer getCoinValueByItem(IItem item);

    public enum CoinTypes {
        SPUR,
        BEVEL,
        SPROCKET,
        COG,
        CROWN,
        SUN
    }

    ITextureAtlasSprite getAtlasSpriteByName(String name);
    Consumer<Integer> getMainCoinSetter(CoinTypes type);
    Consumer<Integer> getReturnCoinSetter(CoinTypes type);
    CoinTypes getCoinTypeByValue(int value);
}
