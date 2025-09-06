package org.daylight.coinscalculator;

import java.util.HashMap;
import java.util.Map;

public class UiState {
    public static boolean coinCalculatorOverlayActive = false;
    public static float globalPanelSpacingModifier = 0.8f;
    public static float globalPanelPaddingModifier = 0.8f;
    public static int selectedCoinsValue = 0;
    public static Map<Integer, Integer> selectionSlotValuesCoins = new HashMap<>();
    public static boolean selectionModeActive = false;
    public static boolean selectionRendered = false;
    public static double selectionStartPointX = -1;
    public static double selectionStartPointY = -1;
    public static int selectionStartPointSlotIndex = -1;
    public static double selectionEndPointX = -1;
    public static double selectionEndPointY = -1;
    public static int selectionEndPointSlotIndex = -1;

    public static int conversionValue = 0;
    public static int conversionMode = 1;
    public static int conversionSunMain = 0;
    public static int conversionSummedOverpay = 0;
    public static int conversionSunOverpay = 0;
    public static int conversionCrownMain = 0;
    public static int conversionCrownOverpay = 0;
    public static int conversionCogMain = 0;
    public static int conversionCogOverpay = 0;
    public static int conversionSprocketMain = 0;
    public static int conversionSprocketOverpay = 0;
    public static int conversionBevelMain = 0;
    public static int conversionBevelOverpay = 0;
    public static int conversionSpurMain = 0;
    public static int conversionSpurOverpay = 0;
}
