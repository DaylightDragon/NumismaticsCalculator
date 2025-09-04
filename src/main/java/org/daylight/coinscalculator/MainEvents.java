package org.daylight.coinscalculator;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.client.extensions.IForgeBakedModel;
import net.minecraftforge.client.model.data.ModelData;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.registries.ForgeRegistries;
import org.daylight.coinscalculator.ui.CoinsOverlay;
import org.daylight.coinscalculator.ui.SelectionRenderer;
import org.daylight.coinscalculator.ui.UIUpdateRequests;
import org.daylight.coinscalculator.ui.elements.*;
import org.daylight.coinscalculator.util.CoinChangeLimited;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class MainEvents {
    private static final Component holdShiftHintComponent = Component.literal("Hold ").withStyle(ChatFormatting.GRAY)
            .append(Component.literal("SHIFT").withStyle(ChatFormatting.GOLD).withStyle(ChatFormatting.BOLD))
            .append(Component.literal(" to view total value").withStyle(ChatFormatting.GRAY));

    @SubscribeEvent
    public void onTooltip(ItemTooltipEvent event) {
        ItemStack stack = event.getItemStack();

        Integer value = CoinValues.ITEM_TO_VALUE.get(stack.getItem());
        if (value != null && value > 0) {
            if(!Screen.hasShiftDown()) {
                event.getToolTip().add(holdShiftHintComponent);
            } else {
                Component newLine = Component.literal("Value (Total): ").withStyle(ChatFormatting.WHITE)
                                .append(Component.literal(value * stack.getCount() + "¤").withStyle(ChatFormatting.GOLD)); //.withStyle(ChatFormatting.BOLD))
//                                .append(Component.literal("").withStyle(ChatFormatting.WHITE))

                List<Component> tooltip = event.getToolTip();
                for(int i = 0; i < tooltip.size(); i++) {
                    Component component = tooltip.get(i);
                    if(component.getString().contains("Value:")) {
                        tooltip.set(i, newLine);
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void onClientSetup(FMLClientSetupEvent event) {

    }

    @SubscribeEvent
    public static void onRegisterGuiOverlays(RegisterGuiOverlaysEvent event) {
        event.registerAboveAll("coins_overlay", new CoinsOverlay());
        CoinsCalculator.LOGGER.info("Registering CoinsCalculator overlay");
    }

    private static final int PANEL_WIDTH = 80;
    private static final int PANEL_HEIGHT = 120;
    private UIPanel mainFloatingPanel;
    private UIStackLayout pagesStackPanel;
    private UIPanel page1VLayout;
    private UIPanel page2VLayout;

    @SubscribeEvent
    public void onScreenRender(ScreenEvent.Render.Post event) {
        if (event.getScreen() instanceof InventoryScreen screen) {
            if(mainFloatingPanel != null) mainFloatingPanel.render(event.getGuiGraphics(), event.getMouseX(), event.getMouseY(), event.getPartialTick());
            SelectionRenderer.renderSelection(event.getGuiGraphics(), screen);
        }
    }

    private void togglePanelPages() {
        if(mainFloatingPanel != null) {
//            if(page1VLayout.isEnabled()) {
//                page1VLayout.setEnabled(false);
//                page2VLayout.setEnabled(true);
//            } else {
//                page1VLayout.setEnabled(true);
//                page2VLayout.setEnabled(false);
//            }
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
        sunCoinMain.addElement(new UIText("", font) {
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

    @SubscribeEvent
    public void onScreenInit(ScreenEvent.Init.Post event) {
        if (event.getScreen() instanceof InventoryScreen screen) {
            int invLeft = screen.getGuiLeft();
            Font font = screen.getMinecraft().font;

            final UIVerticalLayout conversionOutputMain = createConversionMainLayout();
            final UIVerticalLayout conversionOutputReturns = createConversionReturnsLayout();

            mainFloatingPanel = new UIVerticalLayout();
            mainFloatingPanel.setId("Main Pages VERTICAL Panel");
            mainFloatingPanel.setPosition(invLeft - 120, screen.getGuiTop());
            mainFloatingPanel.setBackgroundVisible(true);

            pagesStackPanel = new UIStackLayout();
            pagesStackPanel.setId("Main Pages STACK Panel");

            mainFloatingPanel.addElement(new UIButton("Change mode", font, this::togglePanelPages));
            mainFloatingPanel.addElement(pagesStackPanel);

            page1VLayout = new UIVerticalLayout();
            page1VLayout.setId("Page 1");
            page1VLayout.addElement(new UIText("Total Available: 124 ¤", font));
            page1VLayout.addElement(new UiSpace(0, 5 ));
            page1VLayout.addElement(new UIText("", font) {
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
            page1VLayout.addElement(new UIButton("Select", font, () -> {
                System.out.println("Selecting...");
                UiState.selectionModeActive = !UiState.selectionModeActive;
                if(!UiState.selectionModeActive) {
                    clearAllSelectionCoords();
                }
            }));
            pagesStackPanel.addElement(page1VLayout);

            page2VLayout = new UIVerticalLayout();
            page2VLayout.setId("Page 2");
//            page2VLayout.setEnabled(false);
            page2VLayout.addElement(new UIText("Convert ¤ to coins", font));

            UIEditBox conversionInput = new UIEditBox(font).allowOnlyNumeric();
            event.addListener(conversionInput.getEditBox());
            conversionInput.setOnValueChange(text -> {
                if(text.isBlank()) text = "0";
                int value;
                try {
                    value = Integer.parseInt(text);
                } catch (NumberFormatException e) {
                    return;
                }
                UiState.conversionValue = value;

                int[] values = {4096, 512, 64, 16, 8, 1};
//                int[] counts = {1000, 1000, 1000, 1000, 1000, 1000};

                CoinChangeLimited.Result res = CoinChangeLimited.solveInfinite(value, values);
                System.out.println(res);
                setConversionValues(res);

                UIUpdateRequests.updateConversionValuesMain = true;
                UIUpdateRequests.updateConversionReturns = true;

                conversionOutputMain.updateInternalValues();
                conversionOutputMain.layoutElements(); // MAY BE OPTIMIZED IN LAZY WAY IDK
                conversionOutputReturns.updateInternalValues();
                conversionOutputReturns.layoutElements();
            });
            page2VLayout.addElement(conversionInput);

            conversionOutputMain.addElement(new UIText("Value in coins:", font));

            conversionOutputMain.addElement(createConversionLine("sun", "Sun", () -> UiState.conversionSunMain, font));
            conversionOutputMain.addElement(createConversionLine("crown", "Crown", () -> UiState.conversionCrownMain, font));
            conversionOutputMain.addElement(createConversionLine("cog", "Cog", () -> UiState.conversionCogMain, font));
            conversionOutputMain.addElement(createConversionLine("sprocket", "Sprocket", () -> UiState.conversionSprocketMain, font));
            conversionOutputMain.addElement(createConversionLine("bevel", "Bevel", () -> UiState.conversionBevelMain, font));
            conversionOutputMain.addElement(createConversionLine("spur", "Spur", () -> UiState.conversionSpurMain, font));

            conversionOutputMain.addElement(new UIText("Expected a return:", font));

            conversionOutputReturns.addElement(createConversionLine("sun", "Sun", () -> UiState.conversionSunOverpay, font));
            conversionOutputReturns.addElement(createConversionLine("crown", "Crown", () -> UiState.conversionCrownOverpay, font));
            conversionOutputReturns.addElement(createConversionLine("cog", "Cog", () -> UiState.conversionCogOverpay, font));
            conversionOutputReturns.addElement(createConversionLine("sprocket", "Sprocket", () -> UiState.conversionSprocketOverpay, font));
            conversionOutputReturns.addElement(createConversionLine("bevel", "Bevel", () -> UiState.conversionBevelOverpay, font));
            conversionOutputReturns.addElement(createConversionLine("spur", "Spur", () -> UiState.conversionSpurOverpay, font));

            page2VLayout.addElement(conversionOutputMain);

//            page2VLayout.addElement(new UIText("2 Cog", font));
//            page2VLayout.addElement(new UIText("5 Sprocket", font));
//            page2VLayout.addElement(new UIText("1 Bevel", font));
            pagesStackPanel.addElement(page2VLayout);

//            int prefWPage1 = pagesPanel.getPreferredWidth();
//            int prefHPage1 = pagesPanel.getPreferredHeight();
//
//            int prefWPage2 = pagesPanel.getPreferredWidth();
//            int prefHPage2 = pagesPanel.getPreferredHeight();

            // Math.max(0,

//            page1VLayout.setBounds(invLeft - prefWPage1 - 10, screen.getGuiTop(), prefWPage1, prefHPage1);
//            page2VLayout.setBounds(invLeft - prefWPage2 - 10, screen.getGuiTop(), prefWPage2, prefHPage2);

//            pagesPanel.setBounds(invLeft - prefWGlobal - 10, screen.getGuiTop(), prefWGlobal, prefHGlobal);

//            CoinsCalculator.LOGGER.info("Before layout: panel width=" + page1VLayout.width + ", height=" + page1VLayout.height);
//            page1VLayout.layoutElements();
            mainFloatingPanel.layoutElements();
            mainFloatingPanel.updateInternalValues();
//            CoinsCalculator.LOGGER.info("After layout: panel width=" + page1VLayout.width + ", height=" + page1VLayout.height);

            int prefWGlobal = mainFloatingPanel.getPreferredWidth();
            int prefHGlobal = mainFloatingPanel.getPreferredHeight();
            mainFloatingPanel.setBounds(invLeft - prefWGlobal - 10, (int) (screen.getGuiTop() + prefHGlobal * 0.1), prefWGlobal, prefHGlobal); //  + prefHGlobal * 0.4

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
        for(Map.Entry<Integer, Integer> entry : res.getComposition().entrySet()) {
            CoinValues.CoinTypes type = CoinValues.VALUE_TO_COIN_TYPE.get(entry.getKey());
            if(type == null) return;
            Consumer<Integer> consumerSetMain = CoinValues.TYPE_TO_SET_MAIN.get(type);
            Consumer<Integer> consumerSetReturn = CoinValues.TYPE_TO_SET_RETURN.get(type);
            if(consumerSetMain != null) consumerSetMain.accept(entry.getValue());
//            if(consumerSetReturn != null) consumerSetReturn.accept(entry.getValue());
        }
        Objects.requireNonNull(CoinValues.TYPE_TO_SET_RETURN.get(CoinValues.CoinTypes.SPUR)).accept(res.getCoins());
    }

    @SubscribeEvent
    public void onMouseClick(ScreenEvent.MouseButtonPressed.Pre event) {
        if (event.getScreen() instanceof InventoryScreen screen) {
            if(mainFloatingPanel != null) mainFloatingPanel.onClick(event.getMouseX(), event.getMouseY());
            Slot slot = screen.getSlotUnderMouse();
            if(isSlotValidForSelection(slot)) {
                if(UiState.selectionModeActive) {
                    event.setCanceled(true);
                    UiState.selectionRendered = true;
                }
                UiState.selectionStartPointX = event.getMouseX();
                UiState.selectionStartPointY = event.getMouseY();
                UiState.selectionStartPointSlotIndex = screen.getSlotUnderMouse().getContainerSlot();

                UiState.selectionSlotValuesCoins.clear();
            }
//            System.out.println(screen.getSlotUnderMouse());
        }
    }

    @SubscribeEvent
    public void onMouseDrag(ScreenEvent.MouseDragged.Pre event) {
        if (event.getScreen() instanceof InventoryScreen screen) {
            if(UiState.selectionModeActive) {
                Slot slot = screen.getSlotUnderMouse();
//                if(slot != null) System.out.println(slot.getContainerSlot() + " " + slot.container.getClass().getSimpleName());
                if(isSlotValidForSelection(slot)) {
                    if(UiState.selectionModeActive) event.setCanceled(true);
                    UiState.selectionEndPointX = event.getMouseX();
                    UiState.selectionEndPointY = event.getMouseY();

                    System.out.println("Setting " + screen.getSlotUnderMouse().getContainerSlot() + " slot as end in " + (screen.getSlotUnderMouse().container.getClass().getSimpleName()));
                    UiState.selectionEndPointSlotIndex = screen.getSlotUnderMouse().getContainerSlot();

                    updateSelectedValue(screen);
                }
            }
        }
    }

    private static boolean isSlotValidForSelection(Slot slot) {
        if(slot == null) return false;
        if(!(slot.container instanceof Inventory || slot.container instanceof SimpleContainer)) return false;
        if(slot.container instanceof Inventory && slot.getContainerSlot() >= 36) return false;
        return true;
    }

    @SubscribeEvent
    public void onMouseRelease(ScreenEvent.MouseButtonReleased.Pre event) {
        if (event.getScreen() instanceof InventoryScreen screen) {
            if(UiState.selectionModeActive) {
                UiState.selectionRendered = false;
                Slot slot = screen.getSlotUnderMouse();
                if(!isSlotValidForSelection(slot)) {
                    updateSelectedValue(screen);
                    displaySelectedValue();
                    clearAllSelectionCoords();
                    return;
                }

                UiState.selectionEndPointX = event.getMouseX();
                UiState.selectionEndPointY = event.getMouseY();

                UiState.selectionEndPointSlotIndex = screen.getSlotUnderMouse().getContainerSlot();
//                UiState.selectionModeActive = false;

//                screen.getMenu().getGridWidth()
                updateSelectedValue(screen);
                displaySelectedValue();

                clearAllSelectionCoords();
            }
        }
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

    // ScreenEvent.Render.Post
    @SubscribeEvent
    public void onRenderOverlay(RenderGuiOverlayEvent.Post event) {
//        Minecraft mc = Minecraft.getInstance();
//        if(mc.screen == null) return;
////        if (event.getOverlay() != VanillaGuiOverlay.ALL) return;
//
////        CoinsCalculator.LOGGER.info(mc.screen.getClass().getSimpleName());
//        if (!(mc.screen instanceof InventoryScreen || mc.screen instanceof CreativeModeInventoryScreen)) return;
////        CoinsCalculator.LOGGER.info("INVENTORY");
//
//        int width = event.getWindow().getGuiScaledWidth();
//        int height = event.getWindow().getGuiScaledHeight();
//
//        int x = width / 2 + 10;  // пример позиции справа от игрока
//        int y = height / 2 - 20; // пример позиции выше центра
//
//        MainWidget.drawWidget(mc, event, x, y);
    }
}
