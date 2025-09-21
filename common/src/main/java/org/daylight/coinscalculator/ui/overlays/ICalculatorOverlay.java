package org.daylight.coinscalculator.ui.overlays;

import org.daylight.coinscalculator.ModColors;
import org.daylight.coinscalculator.UiState;
import org.daylight.coinscalculator.replacements.*;
import org.daylight.coinscalculator.ui.UIUpdateRequests;
import org.daylight.coinscalculator.ui.elements.*;
import org.daylight.coinscalculator.util.CoinChangeLimited;
import org.daylight.coinscalculator.util.tuples.Quartet;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import java.util.function.Supplier;

public abstract class ICalculatorOverlay implements IOverlay {
    protected UIPanel mainFloatingPanel;
    UIVerticalLayout modesAndStackPanel;
    protected UIStackLayout pagesStackPanel;
    final AtomicReference<UIAxisLayout> modesPanel = new AtomicReference<>();
    protected UIVerticalLayout page1VLayout;
    protected UIVerticalLayout page2VLayout;
    protected UIEditBox conversionInput;
    protected Quartet<Integer, Integer, Integer, Integer> lastOverlayPosition = new Quartet<>(0, 0, 0, 0);

    public Quartet<Integer, Integer, Integer, Integer> getLastOverlayPosition() {
        return lastOverlayPosition;
    }

    private float fontScaleText = 1.0f; // 0.7f;
    private float fontScaleTitle = 1.0f; // 0.9f;
    private float fontScaleButton = 1.0f; // 0.8f;

    public void setOverlayActive(boolean value) {
        if(mainFloatingPanel != null) {
            mainFloatingPanel.setEnabled(value);
        }
    }

    public boolean isInitialized() {
        return mainFloatingPanel != null;
    }

    public abstract boolean shouldRenderOnScreen(IScreen screen);

    //    @Override
    public abstract void render(@NotNull IGuiGraphics IGuiGraphics, float partialTick, Integer mouseX, Integer mouseY);

    public void updateLayout() {
        if(mainFloatingPanel != null) mainFloatingPanel.layoutElements();
    }

    protected int positionAnimationStartY = 0;
    protected int positionAnimationEndY = 0;
    protected boolean positionAnimationActive = false;
    protected long positionAnimationStartTime = 0;

    public abstract void replacePositionAnimationData();

    protected float easeOutCubic(float t) {
        return 1 - (float)Math.pow(1 - t, 3);
    }

    protected float easeInOutSine(float t) {
        return -(float)Math.cos(Math.PI * t) / 2 + 0.5f;
    }

    protected void runPositionAnimation(int positionAnimationDuration) {
//        System.out.println(positionAnimationActive + " " + positionAnimationStartTime + " " + positionAnimationDuration);
        if(!positionAnimationActive) return;
        long currentTime = Calendar.getInstance().getTimeInMillis();
//        System.out.println("positionAnimationDuration: " + positionAnimationDuration);
        if(currentTime - positionAnimationStartTime < positionAnimationDuration) {
            float t = (currentTime - positionAnimationStartTime) / (float) positionAnimationDuration;
            if (t > 1.0f) t = 1.0f;

            float progress = easeInOutSine(t);
//            float newY = easeOutCubic(t);
            float newY = positionAnimationStartY + (positionAnimationEndY - positionAnimationStartY) * progress;
//            System.out.println("Progress: " + progress + ", newY: " + newY);

            float prevY = mainFloatingPanel.getY();
            mainFloatingPanel.setBounds(mainFloatingPanel.getX(), (int) newY, mainFloatingPanel.getWidth(), mainFloatingPanel.getHeight());
            if(t >= 1.0f) { // Math.abs(newY - prevY) < 0.5
                positionAnimationActive = false;
                mainFloatingPanel.setBounds(mainFloatingPanel.getX(), positionAnimationEndY, mainFloatingPanel.getWidth(), mainFloatingPanel.getHeight());
            }
        } else {
            mainFloatingPanel.setBounds(mainFloatingPanel.getX(), positionAnimationEndY, mainFloatingPanel.getWidth(), mainFloatingPanel.getHeight());
            positionAnimationActive = false;
        }
    }

    private void updateVisibilityIfAvailable(UIElement el, int amountNow) {
        el.setEnabled(amountNow > 0);
    }

    public void setConversionText(UIText text, String name, int amount) {
//        updateVisibilityIfAvailable(text, amount);
        if(amount > 0) text.setText(amount + " * " + name);
    }

    private void onConversionTextUpdate(String text) {
        if(text.isBlank()) text = "0";
        int value;
        try {
            value = Integer.parseInt(text);
        } catch (NumberFormatException e) {
            return;
        }
        UiState.conversionValue = value;

        int[] values = {4096, 512, 64, 16, 8, 1};

        CoinChangeLimited.Result res;

//        System.out.println("Method " + UiState.conversionModeUseAvailable);

        if(UiState.conversionModeUseAvailable) {
            int i = 0;
            int[] types = new int[UiState.inventorySnapshotCoinsAmounts.size()];
            int[] amounts = new int[UiState.inventorySnapshotCoinsAmounts.size()];
            for (Map.Entry<Integer, Integer> entry : UiState.inventorySnapshotCoinsAmounts.entrySet()) {
                types[i] = entry.getKey();
                amounts[i] = entry.getValue();
                i++;
            }

            res = CoinChangeLimited.solveLimitedFast(value, types, amounts);
        }
        else res = CoinChangeLimited.solveInfinite(value, values);
        if(res == null) return;
//        System.out.println(res);
        setConversionValues(res);

        UIUpdateRequests.updateConversionValuesMain = true;
        UIUpdateRequests.updateConversionReturns = true;

        conversionOutputMain.updateInternalValues();
        conversionOutputMain.layoutElements(); // MAY BE OPTIMIZED IN LAZY WAY IDK
        conversionOutputReturns.updateInternalValues();
//            conversionOutputReturns.layoutElements();
        mainFloatingPanel.layoutElements();
        replacePositionAnimationData();
    }

    private UIElement createConversionLine(String itemName, String displayName, Supplier<Integer> amount, IFont font) {
        UIHorizontalLayout coinMain = new UIHorizontalLayout() {
            private boolean updatedInternalValues = false;
            @Override
            public void updateInternalValues() {
                super.updateInternalValues();
//                System.out.println("main amount for " + displayName + ": " + amount.get());
                if(!updatedInternalValues || UIUpdateRequests.updateConversionValuesMain || UIUpdateRequests.updateConversionReturns) updateVisibilityIfAvailable(this, amount.get());
                updatedInternalValues = true;
            }
        };
        UiImage sunCoinImage = new UiImage(SingletonInstances.COIN_VALUES.getAtlasSpriteByName(itemName), 16, 16);
        coinMain.setEnabled(false);
        coinMain.addElement(sunCoinImage);
        coinMain.addElement(new UIText("", font, fontScaleText, ModColors.uiSecondaryText) {
            private boolean updatedInternalValues = false;
            @Override
            public void updateInternalValues() {
                super.updateInternalValues();
                if(!updatedInternalValues || UIUpdateRequests.updateConversionValuesMain || UIUpdateRequests.updateConversionReturns) setConversionText(this, displayName, amount.get());
                updatedInternalValues = true;
            }
        });
        return coinMain;
    }

    public Quartet<Integer, Integer, Integer, Integer> getOverlayBoundsForScreen(IAbstractContainerScreen<?> screen) {
        int prefWGlobal = mainFloatingPanel.getPreferredWidth();
        int prefHGlobal = mainFloatingPanel.getPreferredHeight();

        int screenW = screen.width();
        int screenH = screen.height();

        int invLeft = screen.getGuiLeft();
        int left = invLeft - prefWGlobal - 10;
        int top = (screenH - prefHGlobal) / 2;
        int right = left + prefWGlobal;
        int bottom = top + prefHGlobal;

        if(left < 5) left = 5;
        if(top < 5) top = 5;

        if(bottom > screenH - 5) {
            int newBottom = screenH - 5;
            top -= bottom - newBottom;
            bottom = newBottom;

            if (top < 5) {
                top = 5;
//                bottom = top + prefHGlobal;
            }
        }

//        System.out.println("Right:" + right + ", invleft: " + invLeft + ", left: " + left);

        if(right > invLeft - 5) {
            int newRight = invLeft - 5;
            left -= right - newRight;
            right = newRight;

            if (left < 5) {
                left = 5;
            }
        }

//        System.out.println(left + " " + right + " | " + invLeft + " " + screenH);
//        System.out.println(right - left);

        return new Quartet<>(left, top, right - left, bottom - top); //  + prefHGlobal * 0.4

//      return new Quartet<>(0, 0, prefWGlobal, prefHGlobal);
    }

    private UIVerticalLayout conversionOutputMain;
    private UIVerticalLayout conversionOutputReturns;

    public void init(IAbstractContainerScreen<?> screen) {
        if(mainFloatingPanel != null) return;
//        System.out.println("INIT");
        IFont font = SingletonInstances.MINECRAFT_UTILS.getMinecraftFont();
//        Font font1 = new Font()

        conversionOutputMain = createConversionMainLayout();
        conversionOutputReturns = createConversionReturnsLayout();

        mainFloatingPanel = new UIVerticalLayout() {
            @Override
            public void layoutElements() {
                super.layoutElements();
                lastOverlayPosition = new Quartet<>(x, y, width, height);
                UIAxisLayout modesPanelT = modesPanel.get();
                modesPanelT.setBounds(modesPanelT.getX(), modesPanelT.getY(),
                        (int) (mainFloatingPanel.getWidth() - mainFloatingPanel.getPadding() * 2), modesPanelT.getHeight());
                SingletonInstances.GUI_MANAGER_OVERLAY.updateOverlayPosition(screen);
//                replacePositionAnimationData();
            }
        };
        mainFloatingPanel.setId("Main Pages VERTICAL Panel");
        mainFloatingPanel.setPadding(7);
        mainFloatingPanel.setMinWidth(135);
//        mainFloatingPanel.setPosition(invLeft - 120, IScreen.getGuiTop());
        mainFloatingPanel.setBackgroundVisible(true);
        mainFloatingPanel.setBackgroundColor(ModColors.uiBg);
        mainFloatingPanel.setOutlineWidth(2);
        mainFloatingPanel.setOutlineColor(ModColors.uiOutline);

        pagesStackPanel = new UIStackLayout();
        pagesStackPanel.setId("Main Pages STACK Panel");
        pagesStackPanel.setPadding(0);

        modesPanel.set(new UIHorizontalLayout());
        modesPanel.get().setId("Modes Panel");
        modesPanel.get().setBackgroundVisible(true);
        modesPanel.get().setBackgroundColor(ModColors.modeSwitchPanelBg);
        modesPanel.get().setOutlineColor(ModColors.modeSwitchPanelOutline);
        modesPanel.get().setOutlineWidth(1);
        modesPanel.get().setPadding(6);
        modesPanel.get().setSpacing(8);
//        modesPanel.get().setMinWidth(100);
        modesPanel.get().setCrossAxisExcludedFromLayout(true);

        modesPanel.get().setCrossAlignment(CrossAlignment.STRETCH);
        modesPanel.get().setMainDistribution(MainDistribution.FILL);

        final AtomicReference<UIButton> sumModeBtn = new AtomicReference<>();
        final AtomicReference<UIButton> conversionModeBtn = new AtomicReference<>();

        sumModeBtn.set(new UIButton("", font, fontScaleButton, () -> {}) {
            @Override
            public boolean onClick(double mouseX, double mouseY) {
                boolean result = super.onClick(mouseX, mouseY);
                if(!result) return false;

                setBgColorNormal(ModColors.modeSwitchButtonBgSelected);
                setOutlineColor(ModColors.modeSwitchButtonOutlineSelected);
                setOutlineWidth(1);
                if(conversionModeBtn.get() != null) {
//                    conversionModeBtn.get().setOutlineWidth(0);
                    conversionModeBtn.get().setBgColorNormal(ModColors.modeSwitchButtonBgNormal);
                    conversionModeBtn.get().setOutlineColor(ModColors.modeSwitchButtonOutlineNormal);
                }

                if(pagesStackPanel != null) pagesStackPanel.setActiveIndex(0);
                if(mainFloatingPanel != null) mainFloatingPanel.layoutElements();
                replacePositionAnimationData();
                return true;
            }
        });
        sumModeBtn.get().setPadding(2, 2);
        sumModeBtn.get().setBgColorNormal(ModColors.modeSwitchButtonBgNormal);
        sumModeBtn.get().setBgColorHover(ModColors.modeSwitchButtonBgHovered);
        sumModeBtn.get().setOutlineColor(ModColors.modeSwitchButtonOutlineSelected);
        sumModeBtn.get().setBgColorNormal(ModColors.modeSwitchButtonBgSelected); // since it's default
        sumModeBtn.get().setImagePosition(UIButton.ImagePosition.IMAGE_RIGHT_KINDA);
        sumModeBtn.get().setIcon(SingletonInstances.MOD_RESOURCES.getResourceLocation(IModResources.Type.SUM_ICON), 12, 12);
        sumModeBtn.get().setOutlineWidth(1);
        modesPanel.get().addElement(sumModeBtn.get());

        conversionModeBtn.set(new UIButton("", font, fontScaleButton, () -> {}) {
            @Override
            public boolean onClick(double mouseX, double mouseY) {
                boolean result = super.onClick(mouseX, mouseY);
                if(!result) return false;

                setBgColorNormal(ModColors.modeSwitchButtonBgSelected);
                setOutlineColor(ModColors.modeSwitchButtonOutlineSelected);
                setOutlineWidth(1);
                if(sumModeBtn.get() != null) {
//                    sumModeBtn.get().setOutlineWidth(0);
                    sumModeBtn.get().setBgColorNormal(ModColors.modeSwitchButtonBgNormal);
                    sumModeBtn.get().setOutlineColor(ModColors.modeSwitchButtonOutlineNormal);
                }

                if(pagesStackPanel != null) pagesStackPanel.setActiveIndex(1);
                if(mainFloatingPanel != null) mainFloatingPanel.layoutElements();
                replacePositionAnimationData();
                return true;
            }
        });
        conversionModeBtn.get().setPadding(2, 2);
        conversionModeBtn.get().setBgColorNormal(ModColors.modeSwitchButtonBgNormal);
        conversionModeBtn.get().setBgColorHover(ModColors.modeSwitchButtonBgHovered);
        conversionModeBtn.get().setOutlineColor(ModColors.modeSwitchButtonOutlineSelected);
        conversionModeBtn.get().setImagePosition(UIButton.ImagePosition.IMAGE_RIGHT_KINDA);
        conversionModeBtn.get().setIcon(SingletonInstances.MOD_RESOURCES.getResourceLocation(IModResources.Type.WEIGHING_MACHINE_ICON), 12, 12);
        conversionModeBtn.get().setOutlineWidth(1);
        conversionModeBtn.get().setOutlineColor(ModColors.modeSwitchButtonOutlineNormal);
        modesPanel.get().addElement(conversionModeBtn.get());

//        mainFloatingPanel.addElement(modeSwitchBtn);
        modesAndStackPanel = new UIVerticalLayout() {
            @Override
            public void layoutElements() {
                super.layoutElements();
                if(modesPanel.get() != null) {
//                    modesPanel.get().setMinWidth((int) (getMaxChildSize(false) - getPadding() * 2));
//                    modesPanel.get().setBounds(modesPanel.get().getX(), modesPanel.get().getY(), modesAndStackPanel.getWidth(), modesPanel.get().getHeight());
//                    modesPanel.get().setMinWidth(Math.min((int) (getMaxChildSize(false) - getPadding() * 2), 100)); // auto width max 100 df
                    modesPanel.get().layoutElements();
                }
            }
        };
        modesAndStackPanel.addElement(modesPanel.get());
        modesAndStackPanel.addElement(pagesStackPanel);
//        tempPanel.setPadding(0);
        mainFloatingPanel.addElement(modesAndStackPanel);

        page1VLayout = new UIVerticalLayout();
        page1VLayout.setId("Page 1");
        page1VLayout.setSpacing(8);
        page1VLayout.addElement(new UIText("", font, fontScaleText, ModColors.uiPrimaryText) {
            private boolean updatedInternalValues = false;
            @Override
            public void updateInternalValues() {
                super.updateInternalValues();
                if(!updatedInternalValues || UIUpdateRequests.updateTotalCoinsValue) {
                    setText("Total Available: " + UiState.inventorySnapshotTotalCoins + " ¤"); // Value
                    UIUpdateRequests.updateTotalCoinsValue = false;
                }
                updatedInternalValues = true;
            }
        });
//        page1VLayout.addElement(new UiSpace(0, 5));
        page1VLayout.addElement(new UIText("", font, fontScaleText, ModColors.uiPrimaryText) {
            private boolean updatedInternalValues = false;
            @Override
            public void updateInternalValues() {
                super.updateInternalValues();
                if(!updatedInternalValues || UIUpdateRequests.updateSelectedCoinsValue) {
                    setText("Selected: " + UiState.selectedCoinsValue + " ¤");
                    UIUpdateRequests.updateSelectedCoinsValue = false;
                }
                updatedInternalValues = true;
            }
        });
        UIButton selectSwitchBtn = new UIButton("Select", font, fontScaleButton, () -> {
//            System.out.println("Selecting...");
            UiState.selectionModeActive = !UiState.selectionModeActive;
            if(!UiState.selectionModeActive) {
                clearAllSelectionData();
            }
//            System.out.println("selectionModeActive after click: " + UiState.selectionModeActive);
        }) {
            @Override
            public boolean onClick(double mouseX, double mouseY) {
                boolean result = super.onClick(mouseX, mouseY);
//                if(UiState.selectionModeActive) label = "Selecting...";
//                else label = "Select";
                if(UiState.selectionModeActive) {
//                    setIcon(ModResources.SELECTION_ACTIVE, 16, 16);
                    setOutlineWidth(1);
                }
                else {
//                    setIcon(ModResources.SELECTION_DEFAULT, 16, 16);
                    setOutlineWidth(0);
                }
                mainFloatingPanel.layoutElements();
                return result;
            }
        };
        selectSwitchBtn.setIcon(SingletonInstances.MOD_RESOURCES.getResourceLocation(IModResources.Type.SELECTION_DEFAULT), 16, 16);
        selectSwitchBtn.setPadding(2, 2);
        selectSwitchBtn.setImagePosition(UIButton.ImagePosition.IMAGE_LEFT);
        selectSwitchBtn.setOutlineColor(ModColors.uiSelected);

        page1VLayout.addElement(selectSwitchBtn);
        pagesStackPanel.addElement(page1VLayout);

        page2VLayout = new UIVerticalLayout();
        page2VLayout.setId("Page 2");
        page2VLayout.setSpacing(8);
        page2VLayout.addElement(new UIText("Convert ¤ to Coins", font, fontScaleTitle, ModColors.uiPrimaryText));

        conversionInput = new UIEditBox(font, 80, 20).allowOnlyNumeric();
//        event.addListener(conversionInput.getEditBox()); // commented
        conversionInput.setOnValueChange(this::onConversionTextUpdate);
        page2VLayout.addElement(conversionInput);

        UiCheckBox useAvailable = new UiCheckBox("Only Available", font, 1.0f, () -> {}) {
            @Override
            public boolean onClick(double mouseX, double mouseY) {
                boolean result = super.onClick(mouseX, mouseY);
                UiState.conversionModeUseAvailable = selected;
                onConversionTextUpdate(conversionInput.getEditBox().getValue());
                return result;
            }
        };
        page2VLayout.addElement(useAvailable);

        conversionOutputMain.addElement(new UIText("Value in Coins:", font, fontScaleTitle, ModColors.uiPrimaryText));

        conversionOutputMain.addElement(createConversionLine("sun", "Sun", () -> UiState.conversionSunMain, font));
        conversionOutputMain.addElement(createConversionLine("crown", "Crown", () -> UiState.conversionCrownMain, font));
        conversionOutputMain.addElement(createConversionLine("cog", "Cog", () -> UiState.conversionCogMain, font));
        conversionOutputMain.addElement(createConversionLine("sprocket", "Sprocket", () -> UiState.conversionSprocketMain, font));
        conversionOutputMain.addElement(createConversionLine("bevel", "Bevel", () -> UiState.conversionBevelMain, font));
        conversionOutputMain.addElement(createConversionLine("spur", "Spur", () -> UiState.conversionSpurMain, font));

        conversionOutputMain.addElement(new UiSpace(0, 1));
        conversionOutputMain.addElement(new UIText("Expected in return:", font, fontScaleTitle, ModColors.uiPrimaryText) {
            private boolean updatedInternalValues = false;
            @Override
            public void updateInternalValues() {
                super.updateInternalValues();
                if(!updatedInternalValues || UIUpdateRequests.updateConversionValuesMain || UIUpdateRequests.updateConversionReturns) {
                    if(UiState.conversionSummedOverpay >= 0) {
                        if(UiState.conversionModeUseAvailable && UiState.conversionSummedOverpay != 0) {
                            setEnabled(true);
                            setColor(ModColors.uiWarningText);
                            setText("Overpaying: " + UiState.conversionSummedOverpay);
                        } else {
                            setEnabled(false);
                        }
//                        setColor(ModColors.uiPrimaryText);
//                        setText("Expected in return: " + UiState.conversionSummedOverpay);
                    } else {
                        setEnabled(true);
                        setColor(ModColors.uiErrorText);
                        setText("Missing amount: " + -UiState.conversionSummedOverpay);
                    }
                }
                updatedInternalValues = true;
            }
        });

        conversionOutputReturns.addElement(createConversionLine("sun", "Sun", () -> UiState.conversionSunOverpay, font));
        conversionOutputReturns.addElement(createConversionLine("crown", "Crown", () -> UiState.conversionCrownOverpay, font));
        conversionOutputReturns.addElement(createConversionLine("cog", "Cog", () -> UiState.conversionCogOverpay, font));
        conversionOutputReturns.addElement(createConversionLine("sprocket", "Sprocket", () -> UiState.conversionSprocketOverpay, font));
        conversionOutputReturns.addElement(createConversionLine("bevel", "Bevel", () -> UiState.conversionBevelOverpay, font));
        conversionOutputReturns.addElement(createConversionLine("spur", "Spur", () -> UiState.conversionSpurOverpay, font));

        page2VLayout.addElement(conversionOutputMain);
//        page2VLayout.addElement(new UiSpace(0, 2));
//        page2VLayout.addElement(conversionOutputReturns);

        pagesStackPanel.addElement(page2VLayout);

        mainFloatingPanel.layoutElements();
        mainFloatingPanel.updateInternalValues();

        Quartet<Integer, Integer, Integer, Integer> bounds = getOverlayBoundsForScreen(screen);
        lastOverlayPosition = bounds;
        mainFloatingPanel.setBounds(bounds.getA(), bounds.getB(), bounds.getC(), bounds.getD());
    }

    public void updateOverlayPosition(IScreen screen) {
        if(screen.isAbstractContainerScreen()) {
            Quartet<Integer, Integer, Integer, Integer> bounds = getOverlayBoundsForScreen(screen.getAsAbstractContainerScreen());
//            System.out.println("ICalculatorOverlay bounds: " + bounds);
            lastOverlayPosition = bounds;
            mainFloatingPanel.setBounds(bounds.getA(), bounds.getB(), bounds.getC(), bounds.getD());
        }
    }

    private static @NotNull UIVerticalLayout createConversionReturnsLayout() {
        UIVerticalLayout conversionOutputReturns = new UIVerticalLayout() {
            private boolean updatedInternalValues = false;
            @Override
            public void updateInternalValues() {
                if(!updatedInternalValues || UIUpdateRequests.updateConversionReturns) {
                    super.updateInternalValues();
                    UIUpdateRequests.updateConversionReturns = false;
                } else super.updateInternalValues();
                updatedInternalValues = true;
            }
        };
        conversionOutputReturns.setPadding(0);
        return conversionOutputReturns;
    }

    private static @NotNull UIVerticalLayout createConversionMainLayout() {
        UIVerticalLayout conversionOutputMain = new UIVerticalLayout() {
            private boolean updatedInternalValues = false;
            @Override
            public void updateInternalValues() {
                if(!updatedInternalValues || UIUpdateRequests.updateConversionValuesMain) {
                    super.updateInternalValues();
                    UIUpdateRequests.updateConversionValuesMain = false;
                } else super.updateInternalValues();
                updatedInternalValues = true;
            }
        };
        conversionOutputMain.setPadding(0);
        return conversionOutputMain;
    }

    private void setConversionValues(CoinChangeLimited.Result res) {
        SingletonInstances.COIN_VALUES.resetAllMainCoins();
        SingletonInstances.COIN_VALUES.resetAllReturnCoins();

        for(Map.Entry<Integer, Integer> entry : res.getComposition().entrySet()) {
            ICoinValues.CoinTypes type = SingletonInstances.COIN_VALUES.getCoinTypeByValue(entry.getKey());
            if(type == null) continue;
            Consumer<Integer> consumerSetMain = SingletonInstances.COIN_VALUES.getMainCoinSetter(type);
            Consumer<Integer> consumerSetReturn = SingletonInstances.COIN_VALUES.getReturnCoinSetter(type);
            if(consumerSetMain != null) consumerSetMain.accept(entry.getValue());
//            System.out.println("Set " + entry.getValue() + " to " + type);
//            if(consumerSetReturn != null) consumerSetReturn.accept(entry.getValue());
        }

        UiState.conversionSummedOverpay = res.getOverpay();
//        System.out.println("conversionSummedOverpay " + UiState.conversionSummedOverpay);
        Objects.requireNonNull(SingletonInstances.COIN_VALUES.getReturnCoinSetter(ICoinValues.CoinTypes.SPUR)).accept(res.getOverpay());
    }

    protected abstract boolean isSlotValidForSelection(ISlot slot);

    private void clearAllSelectionData() {
        UiState.selectionStartPointX = -1;
        UiState.selectionStartPointY = -1;

        UiState.selectionEndPointX = -1;
        UiState.selectionEndPointY = -1;

        UiState.selectionStartPointSlotIndex = -1;
        UiState.selectionEndPointSlotIndex = -1;

        UiState.selectionContainerClass = null;
    }

    private void updateSelectedValue(IAbstractContainerScreen<?> screen) {
        if(screen.countSlots() == 0) return;
//        System.out.println("updateSelectedValue" + IScreen.getClass().getSimpleName());
        UiState.selectionSlotValuesCoins.clear();
        List<Integer> slots = getSlotIndexesInSelection(screen, UiState.selectionStartPointSlotIndex, UiState.selectionEndPointSlotIndex, UiState.selectionContainerClass);
//        System.out.println(slots);
        for(Integer slotIndex : slots) {
            ISlot slot = getRealInventorySlot(screen, slotIndex); // IScreen.getMenu().getSlot(slotIndex);
            if(slot == null) continue;

            Integer value = SingletonInstances.COIN_VALUES.getCoinValueByItem(slot.getItemActual());
            if(value != null) UiState.selectionSlotValuesCoins.put(slotIndex, value * slot.getItemStack().getCount());
            else UiState.selectionSlotValuesCoins.put(slotIndex, null);
        }
    }

    private void displaySelectedValue() {
        int totalValue = 0;
        for(Integer slotIndex : UiState.selectionSlotValuesCoins.keySet()) {
            Integer value = UiState.selectionSlotValuesCoins.get(slotIndex);
            if(value != null) totalValue += value;
        }
        UiState.selectedCoinsValue = totalValue;
        UIUpdateRequests.updateSelectedCoinsValue = true;
    }

    @Deprecated
    public abstract List<ISlot> getPlayerInventorySlots(IAbstractContainerScreen<?> screen);

    public abstract int getRealSlotIndex(IAbstractContainerScreen<?> screen, ISlot slot, Class<?> overwriteTargetClass);

    public abstract ISlot getRealInventorySlot(IAbstractContainerScreen<?> screen, int slotIndex);

    public void onScreenChange(IScreen screen) {
        clearAllSelectionData();
        UiState.selectionRendered = false;
//        if(!(screen instanceof IAbstractContainerScreen<?>)) UiState.selectionModeActive = false;
    }

//    public void onScreenClose() {
//        clearAllSelectionCoords();
//        if(!(screen instanceof IAbstractContainerScreen<?>)) UiState.selectionModeActive = false;
//    }

    public List<Integer> getSlotIndexesInSelection(IAbstractContainerScreen<?> screen, int startIndex, int endIndex, Class<?> containerClass) {
        IAbstractContainerMenu menu = screen.getMenu();
        List<ISlot> slots = menu.getSlots();

        if (startIndex < 0 || endIndex < 0 || startIndex >= slots.size() || endIndex >= slots.size()) {
            return List.of();
        }

        ISlot start = getRealInventorySlot(screen, startIndex); // slots.get(startIndex);
        ISlot end   = getRealInventorySlot(screen, endIndex); // slots.get(endIndex);

        if(start == null || end == null) return List.of();

//        System.out.println("Start-End Slots: " + startIndex + " " + endIndex + " (" + getRealSlotIndex(screen, start, null) + " " + getRealSlotIndex(screen, end, null) + ")");
//        System.out.println("Start-End Containers: " + start.getContainerClass().getSimpleName() + " " + end.getContainerClass().getSimpleName());

        int startX;
        int startY;
        int endX;
        int endY;

        startX = getXApproximated(start);
        startY = getYApproximated(start);
        endX = getXApproximated(end);
        endY = getYApproximated(end);

        int x1 = Math.min(startX, endX);
        int y1 = Math.min(startY, endY);
        int x2 = Math.max(startX, endX);
        int y2 = Math.max(startY, endY);

        List<Integer> result = new ArrayList<>();
        for (ISlot slot : slots) {
            int localX = getXApproximated(slot);
            int localY = getYApproximated(slot);

            if (localX >= x1 && localX <= x2 && localY >= y1 && localY <= y2) {
                result.add(getRealSlotIndex(screen, slot, null));  //slots.indexOf(slot)); // slot.getContainerSlot()
            }
        }
        return result;
    }

    private static int getXApproximated(ISlot slot) {
        return slot.x();
    }

    private static int getYApproximated(ISlot slot) {
        return slot.y();
    }

    protected static final ExecutorService backgroundProcessor = Executors.newSingleThreadExecutor();

    public abstract void requestInventorySnapshot(); // SingletonInstances.MINECRAFT_UTILS.execute(() -> backgroundProcessor.submit(() -> getInstance().processInventory(SingletonInstances.MINECRAFT_UTILS.getInventoryItems())));

    // Executed in a thread pool
    protected void processInventory(List<IItemStack> snapshot) {
        int totalValue = 0;
        final Map<Integer, Integer> tempMap = new HashMap<>();
        for (IItemStack stack : snapshot) {
            Integer value = SingletonInstances.COIN_VALUES.getCoinValueByItem(stack.getItem());
            if (value != null) {
                totalValue += value * stack.getCount();
                if(!tempMap.containsKey(value)) tempMap.put(value, stack.getCount());
                else tempMap.put(value, tempMap.get(value) + stack.getCount());
            }
        }

        final int totalValueCopy = totalValue;
        SingletonInstances.MINECRAFT_UTILS.execute(() -> {
            UiState.inventorySnapshotCoinsAmounts.clear();
            UiState.inventorySnapshotCoinsAmounts.putAll(tempMap);
            UiState.inventorySnapshotTotalCoins = totalValueCopy;
//            System.out.println(UiState.inventorySnapshotTotalCoins);

            if(conversionInput != null) onConversionTextUpdate(conversionInput.getEditBox().getValue());
            UIUpdateRequests.updateTotalCoinsValue = true;
        });
    }

    public boolean shouldBlockClicks(IScreen screenOrig, int mouseX, int mouseY) {
        boolean result = false;
        if (shouldRenderOnScreen(screenOrig)) {
            IAbstractContainerScreen<?> screen = screenOrig.getAsAbstractContainerScreen();
            ISlot slot = screen.getSlotUnderMouse();
            if (isSlotValidForSelection(slot) && pagesStackPanel.getActiveIndex() == 0 && UiState.coinCalculatorOverlayActive) {
                if (UiState.selectionModeActive) {
                    result = true;
                }
            }
        }

//        boolean result = UiState.selectionRendered;
//        System.out.println("shouldBlockClicks " + result);
        return result;
    }

    @Override
    public boolean onMouseClick(double mouseX, double mouseY, int button, IScreen screenOrig) {
        boolean result = false;
        if (shouldRenderOnScreen(screenOrig)) {
            IAbstractContainerScreen<?> screen = screenOrig.getAsAbstractContainerScreen();
            if(mainFloatingPanel != null) mainFloatingPanel.onClick(mouseX, mouseY);
            ISlot slot = screen.getSlotUnderMouse();
            if(isSlotValidForSelection(slot) && pagesStackPanel.getActiveIndex() == 0 && UiState.coinCalculatorOverlayActive) { // may change, dangerous a bit
                if(UiState.selectionModeActive) {
//                    event.setCanceled(true);
                    UiState.selectionRendered = true;
                    result = true;
                }
                UiState.selectionStartPointX = mouseX;
                UiState.selectionStartPointY = mouseY;
                ISlot slotUnderMouse = screen.getSlotUnderMouse();
                int realSlotIndexUnderMouse = getRealSlotIndex(screen, slotUnderMouse, slotUnderMouse.getContainerClass());
//                System.out.println(slotUnderMouse.getContainerClass().getSimpleName() + " start index: " + realSlotIndexUnderMouse);
                UiState.selectionStartPointSlotIndex = realSlotIndexUnderMouse; // was .getContainerSlot() before
                UiState.selectionContainerClass = screen.getSlotUnderMouse().getContainerClass();

                UiState.selectionSlotValuesCoins.clear();
            }
//            System.out.println(IScreen.getSlotUnderMouse());
        }
        return result; // true if cancel
    }

    private boolean isSelectionSlotTheSameType(ISlot slot) {
        if(slot == null || UiState.selectionContainerClass == null) return false;
        return UiState.selectionContainerClass.equals(slot.getContainerClass());
    }

    @Override
    public boolean onMouseDrag(double mouseX, double mouseY, int button, IScreen screenOrig) {
        if (shouldRenderOnScreen(screenOrig)) {
            IAbstractContainerScreen<?> screen = screenOrig.getAsAbstractContainerScreen();
            if(UiState.selectionModeActive) {
                ISlot slot = screen.getSlotUnderMouse();
//                if(slot != null) System.out.println(slot.getContainerSlot() + " " + slot.container.getClass().getSimpleName());
                if(isSlotValidForSelection(slot) && isSelectionSlotTheSameType(slot)) {
//                    event.setCanceled(true);
                    UiState.selectionEndPointX = mouseX;
                    UiState.selectionEndPointY = mouseY;

                    ISlot slotUnderMouse = screen.getSlotUnderMouse();
                    int realSlotIndexUnderMouse = getRealSlotIndex(screen, slotUnderMouse, null);
//                    System.out.println(slotUnderMouse.getContainerClass().getSimpleName() + " drag end index: " + realSlotIndexUnderMouse);
                    UiState.selectionEndPointSlotIndex = realSlotIndexUnderMouse;
                    UiState.selectionContainerClass = screen.getSlotUnderMouse().getContainerClass();
//                    System.out.println(IScreen.getSlotUnderMouse().container.getClass().getSimpleName());

                    updateSelectedValue(screen);
                    return true;
                }
            }
        }
        return false; // don't cancel
    }

    @Override
    public void onMouseRelease(double mouseX, double mouseY, int button, IScreen screenOrig) {
        if (shouldRenderOnScreen(screenOrig)) {
            IAbstractContainerScreen<?> screen = screenOrig.getAsAbstractContainerScreen();
            if(UiState.selectionModeActive) {
                UiState.selectionRendered = false;
                ISlot slot = screen.getSlotUnderMouse();
                if(!isSlotValidForSelection(slot) || !isSelectionSlotTheSameType(slot)) {
                    updateSelectedValue(screen);
                    displaySelectedValue();
                    clearAllSelectionData();
                    return;
                }

                UiState.selectionEndPointX = mouseX;
                UiState.selectionEndPointY = mouseY;

                ISlot slotUnderMouse = screen.getSlotUnderMouse();
                int realSlotIndexUnderMouse = getRealSlotIndex(screen, slotUnderMouse, null);
//                System.out.println(slotUnderMouse.getContainerClass().getSimpleName() + " end index: " + realSlotIndexUnderMouse);
                UiState.selectionEndPointSlotIndex = realSlotIndexUnderMouse;
//                UiState.selectionModeActive = false;

//                IScreen.getMenu().getGridWidth()
                updateSelectedValue(screen);
                displaySelectedValue();

                clearAllSelectionData();
            }
        }
    }

    public void onCharTyped(char codePoint, int modifiers) {
//        if(conversionInput != null) conversionInput.getEditBox().charTyped(codePoint, modifiers);
    }

    @Override
    public void onKeyPressed(IKeyPressEvent event) {
        if(mainFloatingPanel != null) mainFloatingPanel.keyPressed(event);
    }

//    public void onMouseScroll(double mouseX, double mouseY, double delta) {
////        if(conversionInput != null) conversionInput.getEditBox().mouseScrolled(mouseX, mouseY, delta);
//    }

    public void relinkListeners(IRegisterListenersEvent event) {
        if(event.isAbstractContainerScreen()) {
            if (mainFloatingPanel == null) init(event.getAsAbstractContainerScreen());
        }
        if(mainFloatingPanel != null) mainFloatingPanel.relinkListeners(event);
    }
}
