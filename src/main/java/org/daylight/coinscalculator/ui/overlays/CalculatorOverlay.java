package org.daylight.coinscalculator.ui.overlays;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.client.extensions.IForgeBakedModel;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;
import net.minecraftforge.client.model.data.ModelData;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.ForgeRegistries;
import org.daylight.coinscalculator.CoinValues;
import org.daylight.coinscalculator.UiState;
import org.daylight.coinscalculator.ui.SelectionRenderer;
import org.daylight.coinscalculator.ui.UIUpdateRequests;
import org.daylight.coinscalculator.ui.elements.*;
import org.daylight.coinscalculator.util.CoinChangeLimited;
import org.daylight.coinscalculator.util.tuples.Quartet;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class CalculatorOverlay {
    private static CalculatorOverlay instance;
    public static CalculatorOverlay getInstance() {
        if(instance == null) instance = new CalculatorOverlay();
        return instance;
    }

    private UIPanel mainFloatingPanel;
    private UIStackLayout pagesStackPanel;
    private UIPanel page1VLayout;
    private UIPanel page2VLayout;
    private UIEditBox conversionInput;

    private float fontScaleText = 1.0f; // 0.7f;
    private float fontScaleTitle = 1.0f; // 0.9f;
    private float fontScaleButton = 1.0f; // 0.8f;

    public void setOverlayActive(boolean value) {
        if(mainFloatingPanel != null) {
            mainFloatingPanel.setEnabled(value);
        }
    }

    public static boolean shouldRenderOnScreen(Screen screen) {
        return (screen instanceof AbstractContainerScreen) || (screen instanceof CreativeModeInventoryScreen) || (screen instanceof InventoryScreen);
    }

    //    @Override
    public void render(GuiGraphics guiGraphics, float partialTick, Integer mouseX, Integer mouseY) {
        if(!UiState.coinCalculatorOverlayActive) return;
        if (shouldRenderOnScreen(Minecraft.getInstance().screen)) {
            AbstractContainerScreen<?> screen = (AbstractContainerScreen<?>) Minecraft.getInstance().screen;
            if (mainFloatingPanel == null) {
                init(screen);
            }
            SelectionRenderer.renderSelection(guiGraphics, screen);

            if (mainFloatingPanel != null) {
                Minecraft mc = Minecraft.getInstance();
                if (mouseX == null)
                    mouseX = (int) (mc.mouseHandler.xpos() * mc.getWindow().getGuiScaledWidth() / mc.getWindow().getWidth());
                if (mouseY == null)
                    mouseY = (int) (mc.mouseHandler.ypos() * mc.getWindow().getGuiScaledHeight() / mc.getWindow().getHeight());

//            System.out.println("Rendering actual ui");
//            RenderSystem.disableDepthTest();
                if (mainFloatingPanel != null) mainFloatingPanel.render(guiGraphics, mouseX, mouseY, partialTick);
//            RenderSystem.enableDepthTest();
            }
        }
    }

    public void updateLayout() {
        if(mainFloatingPanel != null) mainFloatingPanel.layoutElements();
    }

    private void togglePanelPages() {
//        System.out.println(mainFloatingPanel != null);
        if(mainFloatingPanel != null) {
            pagesStackPanel.setNextIndex();
            mainFloatingPanel.layoutElements();
        }
    }

    public static final ResourceLocation BLOCK_ATLAS = ResourceLocation.fromNamespaceAndPath("minecraft", "textures/atlas/blocks.png");

    private TextureAtlasSprite getCoinResourceLocation(String itemName) {
        Item item = ForgeRegistries.ITEMS.getValue(ResourceLocation.parse("numismatics:" + itemName));
        if(item == null || Minecraft.getInstance().level == null) {
//            return ResourceLocation.fromNamespaceAndPath("minecraft", "missingno");
            Minecraft.getInstance().getTextureAtlas(TextureAtlas.LOCATION_BLOCKS).apply(ResourceLocation.parse("minecraft:missingno"));
        }
        BakedModel model = Minecraft.getInstance().getItemRenderer().getModel(new ItemStack(item), null, null, 0);

        List<BakedQuad> quads = ((IForgeBakedModel) model).getQuads(
                null,                // BlockState (null для предмета)
                null,                // Direction (null = все стороны)
                RandomSource.create(), // Случайный источник
                ModelData.EMPTY,     // Нет дополнительных данных
                null                 // RenderType (null = все)
        );

        if (!quads.isEmpty()) {
            return quads.get(0).getSprite();
        }

        return Minecraft.getInstance().getTextureAtlas(BLOCK_ATLAS).apply(ResourceLocation.parse("minecraft:missingno"));

//        return ResourceLocation.fromNamespaceAndPath("numismatics", "textures/item/coin/" + itemName + ".png");
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
//        int[] counts = {1000, 1000, 1000, 1000, 1000, 1000}; //TODO

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

            res = CoinChangeLimited.solveFast(value, types, amounts);
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
    }

    private UIElement createConversionLine(String itemName, String displayName, Supplier<Integer> amount, Font font) {
        UIHorizontalLayout sunCoinMain = new UIHorizontalLayout() {
            private boolean updatedInternalValues = false;
            @Override
            public void updateInternalValues() {
                super.updateInternalValues();
                if(!updatedInternalValues || UIUpdateRequests.updateConversionValuesMain || UIUpdateRequests.updateConversionReturns) updateVisibilityIfAvailable(this, amount.get());
                updatedInternalValues = true;
            }
        };
        UiImage sunCoinImage = new UiImage(getCoinResourceLocation(itemName), 16, 16);
        sunCoinMain.setEnabled(false);
        sunCoinMain.addElement(sunCoinImage);
        sunCoinMain.addElement(new UIText("", font, fontScaleText) {
            private boolean updatedInternalValues = false;
            @Override
            public void updateInternalValues() {
                super.updateInternalValues();
                if(!updatedInternalValues || UIUpdateRequests.updateConversionValuesMain || UIUpdateRequests.updateConversionReturns) setConversionText(this, displayName, amount.get());
                updatedInternalValues = true;
            }
        });
        return sunCoinMain;
    }

    private Quartet<Integer, Integer, Integer, Integer> getOverlayBoundsForScreen(AbstractContainerScreen<?> screen) {
        int prefWGlobal = mainFloatingPanel.getPreferredWidth();
        int prefHGlobal = mainFloatingPanel.getPreferredHeight();

        int invLeft = screen.getGuiLeft();
        int left = invLeft - prefWGlobal - 10;
        int top = screen.getGuiTop() + (screen.getYSize() - prefHGlobal) / 2;
        int right = left + prefWGlobal;
        int bottom = top + prefHGlobal;

        if(left < 5) left = 5;
        if(top < 5) top = 5;

        if(bottom > screen.getGuiTop() + screen.getYSize() - 5) {
            int newBottom = screen.getGuiTop() + screen.getYSize() - 5;
            top -= bottom - newBottom;
            bottom = newBottom;

            if (top < 5) {
                top = 5;
//                bottom = top + prefHGlobal;
            }
        }

        return new Quartet<>(left, top, right, bottom); //  + prefHGlobal * 0.4

//      return new Quartet<>(0, 0, prefWGlobal, prefHGlobal);
    }

    private UIVerticalLayout conversionOutputMain;
    private UIVerticalLayout conversionOutputReturns;

    public void init(AbstractContainerScreen<?> screen) {
//        System.out.println("INIT");
        Font font = Minecraft.getInstance().font;
//        Font font1 = new Font()

        conversionOutputMain = createConversionMainLayout();
        conversionOutputReturns = createConversionReturnsLayout();

        mainFloatingPanel = new UIVerticalLayout();
        mainFloatingPanel.setId("Main Pages VERTICAL Panel");
//        mainFloatingPanel.setPosition(invLeft - 120, screen.getGuiTop());
        mainFloatingPanel.setBackgroundVisible(true);

        pagesStackPanel = new UIStackLayout();
        pagesStackPanel.setId("Main Pages STACK Panel");

        mainFloatingPanel.addElement(new UIButton("Count | Convert", font, fontScaleButton, this::togglePanelPages));
        mainFloatingPanel.addElement(pagesStackPanel);

        page1VLayout = new UIVerticalLayout();
        page1VLayout.setId("Page 1");
        page1VLayout.addElement(new UIText("", font, fontScaleText) {
            private boolean updatedInternalValues = false;
            @Override
            public void updateInternalValues() {
                super.updateInternalValues();
                if(!updatedInternalValues || UIUpdateRequests.updateTotalCoinsValue) {
                    setText("Total Available: " + UiState.inventorySnapshotTotalCoins + " ¤");
                    UIUpdateRequests.updateTotalCoinsValue = false;
                }
                updatedInternalValues = true;
            }
        });
        page1VLayout.addElement(new UiSpace(0, 5));
        page1VLayout.addElement(new UIText("", font, fontScaleText) {
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
        page1VLayout.addElement(new UIButton("Select", font, fontScaleButton, () -> {
//            System.out.println("Selecting...");
            UiState.selectionModeActive = !UiState.selectionModeActive;
            if(!UiState.selectionModeActive) {
                clearAllSelectionCoords();
            }
        }));
        pagesStackPanel.addElement(page1VLayout);

        page2VLayout = new UIVerticalLayout();
        page2VLayout.setId("Page 2");
//            page2VLayout.setEnabled(false);
        page2VLayout.addElement(new UIText("Convert ¤ to coins", font, fontScaleTitle));

        conversionInput = new UIEditBox(font).allowOnlyNumeric();
//        event.addListener(conversionInput.getEditBox()); // commented
        conversionInput.setOnValueChange(this::onConversionTextUpdate);
        page2VLayout.addElement(conversionInput);

        page2VLayout.addElement(new UICheckBox(font, "Only available", false).setOnValueChange(value -> {
//            System.out.println("Set to " + value);
            UiState.conversionModeUseAvailable = value;
            onConversionTextUpdate(conversionInput.getEditBox().getValue());
        }));

        conversionOutputMain.addElement(new UIText("Value in coins:", font, fontScaleTitle));

        conversionOutputMain.addElement(createConversionLine("sun", "Sun", () -> UiState.conversionSunMain, font));
        conversionOutputMain.addElement(createConversionLine("crown", "Crown", () -> UiState.conversionCrownMain, font));
        conversionOutputMain.addElement(createConversionLine("cog", "Cog", () -> UiState.conversionCogMain, font));
        conversionOutputMain.addElement(createConversionLine("sprocket", "Sprocket", () -> UiState.conversionSprocketMain, font));
        conversionOutputMain.addElement(createConversionLine("bevel", "Bevel", () -> UiState.conversionBevelMain, font));
        conversionOutputMain.addElement(createConversionLine("spur", "Spur", () -> UiState.conversionSpurMain, font));

        conversionOutputMain.addElement(new UIText("Expected in return:", font, fontScaleTitle) {
            private boolean updatedInternalValues = false;
            @Override
            public void updateInternalValues() {
                super.updateInternalValues();
                if(!updatedInternalValues || UIUpdateRequests.updateConversionValuesMain || UIUpdateRequests.updateConversionReturns) setText("Expected in return: " + UiState.conversionSummedOverpay);
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

        pagesStackPanel.addElement(page2VLayout);

        mainFloatingPanel.layoutElements();
        mainFloatingPanel.updateInternalValues();

        Quartet<Integer, Integer, Integer, Integer> bounds = getOverlayBoundsForScreen(screen);
        mainFloatingPanel.setBounds(bounds.getA(), bounds.getB(), bounds.getC(), bounds.getD());
    }

    public void updateOverlayPosition(Screen screen) {
        if(screen instanceof AbstractContainerScreen<?> abstractContainerScreen) {
            Quartet<Integer, Integer, Integer, Integer> bounds = getOverlayBoundsForScreen(abstractContainerScreen);
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
        for (Consumer<Integer> value : CoinValues.TYPE_TO_SET_MAIN.values()) {
            value.accept(0);
        }
        for (Consumer<Integer> value : CoinValues.TYPE_TO_SET_RETURN.values()) {
            value.accept(0);
        }
        for(Map.Entry<Integer, Integer> entry : res.getComposition().entrySet()) {
            CoinValues.CoinTypes type = CoinValues.VALUE_TO_COIN_TYPE.get(entry.getKey());
            if(type == null) return;
            Consumer<Integer> consumerSetMain = CoinValues.TYPE_TO_SET_MAIN.get(type);
            Consumer<Integer> consumerSetReturn = CoinValues.TYPE_TO_SET_RETURN.get(type);
            if(consumerSetMain != null) consumerSetMain.accept(entry.getValue());
//            if(consumerSetReturn != null) consumerSetReturn.accept(entry.getValue());
        }
        UiState.conversionSummedOverpay = res.getOverpay();
        Objects.requireNonNull(CoinValues.TYPE_TO_SET_RETURN.get(CoinValues.CoinTypes.SPUR)).accept(res.getOverpay());
    }

    private static boolean isSlotValidForSelection(Slot slot) {
        if(slot == null) return false;
        if(!(slot.container instanceof Inventory || slot.container instanceof SimpleContainer)) return false;
        if(slot.container instanceof Inventory && slot.getContainerSlot() >= 36) return false;
        return true;
    }

    private void clearAllSelectionCoords() {
        UiState.selectionStartPointX = -1;
        UiState.selectionStartPointY = -1;

        UiState.selectionEndPointX = -1;
        UiState.selectionEndPointY = -1;

        UiState.selectionStartPointSlotIndex = -1;
        UiState.selectionEndPointSlotIndex = -1;
    }

    private void updateSelectedValue(AbstractContainerScreen<?> screen) {
//        System.out.println("updateSelectedValue" + screen.getClass().getSimpleName());
        UiState.selectionSlotValuesCoins.clear();
        List<Integer> slots = getSlotIndexesInSelection(screen, UiState.selectionStartPointSlotIndex, UiState.selectionEndPointSlotIndex);
        for(Integer slotIndex : slots) {
            Slot slot = screen.getMenu().getSlot(slotIndex);
            Integer value = CoinValues.ITEM_TO_VALUE.get(slot.getItem().getItem());
            if(value != null) UiState.selectionSlotValuesCoins.put(slotIndex, value * slot.getItem().getCount());
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

    public static List<Slot> getPlayerInventorySlots(AbstractContainerScreen<?> screen) {
        AbstractContainerMenu menu = screen.getMenu();
        List<Slot> slots = new ArrayList<>(Collections.nCopies(Inventory.INVENTORY_SIZE, null));
        for(Slot slot : menu.slots) {
            if(slot.container instanceof Inventory inventory) {
                slots.set(inventory.items.indexOf(slot.getItem()), slot);
            }
        }
        System.out.println("Full Slots List: " + slots);
        return slots;
    }

    public static List<Integer> getSlotIndexesInSelection(AbstractContainerScreen<?> screen, int startIndex, int endIndex) {
        AbstractContainerMenu menu = screen.getMenu();
        List<Slot> slots = menu.slots;
//        if(screen instanceof InventoryScreen) {
//            slots = getPlayerInventorySlots(screen);
//            for(int i = 0; i < slots.size(); i++) {
//                System.out.println("Inv Slots: " + i + " " + slots.get(i).getContainerSlot() + " " + slots.get(i).getSlotIndex());
//            }
//        }

        if (startIndex < 0 || endIndex < 0 || startIndex >= slots.size() || endIndex >= slots.size()) {
            return List.of();
        }

//        if(screen instanceof InventoryScreen inventoryScreen) {
////            inventoryScreen.getMenu().getGridWidth()
//            if(Minecraft.getInstance().player != null) {
//                Minecraft.getInstance().player.getInventory().items
//            }
//        }

        int initialX = getInitialX(menu, 8);
        int initialY = getInitialY(menu, 84);
        int slotDx = getSlotDx(menu, 18);
        int slotDy = getSlotDy(menu, 18);

        Slot start = slots.get(startIndex);
        Slot end   = slots.get(endIndex);

        System.out.println("Start-End Slots: " + startIndex + " " + endIndex + " (" + start.getContainerSlot() + " " + end.getContainerSlot() + ")");
        System.out.println("Start-End Containers: " + start.container.getClass().getSimpleName() + " " + end.container.getClass().getSimpleName());

        int startX;
        int startY;
        int endX;
        int endY;

//        startX = getXForHotbar(slots, start, initialX, slotDx);
//        endX = getXForHotbar(slots, end, initialX, slotDx);
//
//        startY = getYForHotbar(slots, start, initialY, slotDy);
//        endY = getYForHotbar(slots, end, initialY, slotDy);

        startX = getXApproximated(slots, start, initialX, slotDx);
        endX = getXApproximated(slots, end, initialX, slotDx);

        startY = getYApproximated(slots, start, initialY, slotDy);
        endY = getYApproximated(slots, end, initialY, slotDy);

//        System.out.println(initialX + " " + initialY + " + " + slotDx + " " + slotDy);

        System.out.println(end.getContainerSlot() + " " + (end.container instanceof Inventory) + " end slot X: " + endX + ", Y: " + endY);

        int x1 = Math.min(startX, endX);
        int y1 = Math.min(startY, endY);
        int x2 = Math.max(startX, endX);
        int y2 = Math.max(startY, endY);

//        System.out.println("Start #" + startIndex + ": X " + startX + ", Y " + startY
//                + " - End #" + endIndex + ": X " + endX + ", Y " + endY);

        List<Integer> result = new ArrayList<>();
        for (Slot slot : slots) {
            if (!(slot.container instanceof Inventory)) continue;

            int localX = getXApproximated(slots, slot, initialX, slotDx);
            int localY = getYApproximated(slots, slot, initialY, slotDy);

//            int localX = slot.x;
//            int localY = slot.y;

            // проверяем пересечение
            if (localX >= x1 && localX <= x2 && localY >= y1 && localY <= y2) {
                result.add(slots.indexOf(slot)); // slot.getContainerSlot()
            }
        }
        return result;
    }

    private static int getInitialX(AbstractContainerMenu menu, int defaultValue) {
        Slot slot9 = menu.slots.size() > 9 ? menu.slots.get(9) : null;
        if(slot9 != null) {
            return slot9.x;
        } else return defaultValue;
    }

    private static int getInitialY(AbstractContainerMenu menu, int defaultValue) {
        Slot slot9 = menu.slots.size() > 9 ? menu.slots.get(9) : null;
        if(slot9 != null) {
            return slot9.y;
        } else return defaultValue;
    }

    private static int getSlotDx(AbstractContainerMenu menu, int defaultValue) {
        Slot slot9 = menu.slots.size() > 9 ? menu.slots.get(9) : null;
        Slot slot10 = menu.slots.size() > 10 ? menu.slots.get(10) : null;
        if (slot9 != null && slot10 != null) {
            return slot10.x - slot9.x;
        } else return defaultValue;
    }

    private static int getSlotDy(AbstractContainerMenu menu, int defaultValue) {
        Slot slot9 = menu.slots.size() > 9 ? menu.slots.get(9) : null;
        Slot slot18 = menu.slots.size() > 18 ? menu.slots.get(18) : null;
        if (slot9 != null && slot18 != null) {
            return slot18.y - slot9.y;
        } else return defaultValue;
    }

    private static int getXApproximated(List<Slot> slots, Slot slot, int initialX, int dX) {
        if (slot.container instanceof Inventory) {
            int index = slots.indexOf(slot); // slot.getContainerSlot()
            int result = initialX + (index % 9) * dX;
//                System.out.println("Returning overriden X " + result + " for Slot " + slot.getContainerSlot());
            return result;
        }
        return slot.x;
    }

    private static int getYApproximated(List<Slot> slots, Slot slot, int initialY, int dY) {
        if (slot.container instanceof Inventory) {
            int index = slots.indexOf(slot); // slot.getContainerSlot()
            int result = initialY + (index / 9) * dY;
//                System.out.println("Returning overriden Y " + result + " for Slot " + slot.getContainerSlot());
            return result;
        }
        return slot.y;
    }

    private static int getXForHotbar(List<Slot> slots, Slot slot, int initialX, int dX) {
        if (slot.container instanceof Inventory) {
            int index = slots.indexOf(slot); // slot.getContainerSlot()
            if(index > 8) return slot.x;
            else {
                int result = initialX + index * dX;
//                System.out.println("Returning overriden X " + result + " for Slot " + slot.getContainerSlot());
                return result;
            }
        }
        return slot.x;
    }

    private static int getYForHotbar(List<Slot> slots, Slot slot, int initialY, int dY) {
        if (slot.container instanceof Inventory) {
            int index = slots.indexOf(slot); // slot.getContainerSlot()
            if(index > 8) return slot.y;
            else {
                int result = initialY + (4) * dY;
//                System.out.println("Returning overriden Y " + result + " for Slot " + slot.getContainerSlot());
                return result;
            }
        }
        return slot.y;
    }

    private static final ExecutorService backgroundProcessor = Executors.newSingleThreadExecutor();

    public static void requestInventorySnapshot() {
        System.out.println("requestInventorySnapshot called");
        Minecraft.getInstance().execute(() -> {
            System.out.println("requestInventorySnapshot");
            // На главном потоке: снимаем snapshot инвентаря
            LocalPlayer player = Minecraft.getInstance().player;
            if (player == null) return;

            List<ItemStack> snapshot = new ArrayList<>();
            for (ItemStack stack : player.getInventory().items) {
                if (!stack.isEmpty()) snapshot.add(stack.copy());
            }

            // Отправляем snapshot обратно в фон для обработки
            backgroundProcessor.submit(() -> getInstance().processInventory(snapshot));
        });
    }

    // Executed in a thread pool
    private void processInventory(List<ItemStack> snapshot) {
        int totalValue = 0;
        final Map<Integer, Integer> tempMap = new HashMap<>();
        for (ItemStack stack : snapshot) {
            Integer value = CoinValues.ITEM_TO_VALUE.get(stack.getItem());
            if (value != null) {
                totalValue += value * stack.getCount();
                if(!tempMap.containsKey(value)) tempMap.put(value, stack.getCount());
                else tempMap.put(value, tempMap.get(value) + stack.getCount());
            }
        }

        final int totalValueCopy = totalValue;
        Minecraft.getInstance().execute(() -> {
            UiState.inventorySnapshotCoinsAmounts.clear();
            UiState.inventorySnapshotCoinsAmounts.putAll(tempMap);
            UiState.inventorySnapshotTotalCoins = totalValueCopy;
            System.out.println(UiState.inventorySnapshotTotalCoins);

            if(conversionInput != null) onConversionTextUpdate(conversionInput.getEditBox().getValue());
            UIUpdateRequests.updateTotalCoinsValue = true;
        });
    }

    public boolean onMouseClick(double mouseX, double mouseY, int button, Screen screenOrig) {
        boolean result = false;
        if (shouldRenderOnScreen(screenOrig)) {
            AbstractContainerScreen<?> screen = (AbstractContainerScreen<?>) screenOrig;
            if(mainFloatingPanel != null) mainFloatingPanel.onClick(mouseX, mouseY);
            Slot slot = screen.getSlotUnderMouse();
            if(isSlotValidForSelection(slot)) {
                if(UiState.selectionModeActive) {
//                    event.setCanceled(true);
                    UiState.selectionRendered = true;
                    result = true;
                }
                UiState.selectionStartPointX = mouseX;
                UiState.selectionStartPointY = mouseY;
                UiState.selectionStartPointSlotIndex = screen.getSlotUnderMouse().getContainerSlot();

                UiState.selectionSlotValuesCoins.clear();
            }
//            System.out.println(screen.getSlotUnderMouse());
        }
        return result; // true if cancel
    }

    public boolean onMouseDrag(double mouseX, double mouseY, int button, Screen screenOrig) {
        if (shouldRenderOnScreen(screenOrig)) {
            AbstractContainerScreen<?> screen = (AbstractContainerScreen<?>) screenOrig;
            if(UiState.selectionModeActive) {
                Slot slot = screen.getSlotUnderMouse();
//                if(slot != null) System.out.println(slot.getContainerSlot() + " " + slot.container.getClass().getSimpleName());
                if(isSlotValidForSelection(slot)) {
//                    event.setCanceled(true);
                    UiState.selectionEndPointX = mouseX;
                    UiState.selectionEndPointY = mouseY;

                    System.out.println("Setting " + screen.getSlotUnderMouse().getContainerSlot() + " slot as end in " + (screen.getSlotUnderMouse().container.getClass().getSimpleName()));
                    UiState.selectionEndPointSlotIndex = screen.getSlotUnderMouse().getContainerSlot();

                    updateSelectedValue(screen);
                    return true;
                }
            }
        }
        return false; // don't cancel
    }

    public void onMouseRelease(double mouseX, double mouseY, int button, Screen screenOrig) {
        if (shouldRenderOnScreen(screenOrig)) {
            AbstractContainerScreen<?> screen = (AbstractContainerScreen<?>) screenOrig;
            if(UiState.selectionModeActive) {
                UiState.selectionRendered = false;
                Slot slot = screen.getSlotUnderMouse();
                if(!isSlotValidForSelection(slot)) {
                    updateSelectedValue(screen);
                    displaySelectedValue();
                    clearAllSelectionCoords();
                    return;
                }

                UiState.selectionEndPointX = mouseX;
                UiState.selectionEndPointY = mouseY;

                UiState.selectionEndPointSlotIndex = screen.getSlotUnderMouse().getContainerSlot();
//                UiState.selectionModeActive = false;

//                screen.getMenu().getGridWidth()
                updateSelectedValue(screen);
                displaySelectedValue();

                clearAllSelectionCoords();
            }
        }
    }

    public void onCharTyped(char codePoint, int modifiers) {
        if(conversionInput != null) conversionInput.getEditBox().charTyped(codePoint, modifiers);
    }

    public void onKeyPressed(int keyCode, int scanCode, int modifiers) {
//        if(conversionInput != null) conversionInput.getEditBox().keyPressed(keyCode, scanCode, modifiers);
    }

    public void onMouseScrolled(double mouseX, double mouseY, double delta) {
//        if(conversionInput != null) conversionInput.getEditBox().mouseScrolled(mouseX, mouseY, delta);
    }

    public void relinkListeners(ScreenEvent.Init.Post event) {
        if(event.getScreen() instanceof AbstractContainerScreen<?> abstractContainerScreen) {
            if (mainFloatingPanel == null) init(abstractContainerScreen);
        }
        mainFloatingPanel.relinkListeners(event);
    }
}
