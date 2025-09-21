package org.daylight.coinscalculator.ui.overlays;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.CreativeInventoryScreen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import org.daylight.coinscalculator.UiState;
import org.daylight.coinscalculator.config.ConfigHandler;
import org.daylight.coinscalculator.replacements.*;
import org.daylight.coinscalculator.replacements.api.FabricAbstractContainerScreen;
import org.daylight.coinscalculator.replacements.api.FabricGuiGraphics;
import org.daylight.coinscalculator.replacements.api.FabricScreen;
import org.daylight.coinscalculator.replacements.api.FabricSlot;
import org.daylight.coinscalculator.ui.FabricSelectionRenderer;
import org.daylight.coinscalculator.util.tuples.Quartet;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class FabricCalculatorOverlay extends ICalculatorOverlay {
    @Override
    public boolean shouldRenderOnScreen(IScreen screenOriginal) {
        if(!(screenOriginal instanceof FabricScreen FabricScreen)) throw new IllegalArgumentException();
        Screen screen = FabricScreen.getDelegate();

        return screen instanceof HandledScreen<?>;
    }

    @Override
    public void render(@NotNull IGuiGraphics guiGraphics, float partialTick, Integer mouseX, Integer mouseY) {
        if(!(guiGraphics instanceof FabricGuiGraphics forgeGraphics)) throw new IllegalArgumentException();

//        System.out.println(UiState.coinCalculatorOverlayActive);
        if(!UiState.coinCalculatorOverlayActive) return;
        if (shouldRenderOnScreen(new FabricScreen(MinecraftClient.getInstance().currentScreen))) {
            HandledScreen<?> screen = (HandledScreen<?>) MinecraftClient.getInstance().currentScreen;
            if (mainFloatingPanel == null) {
                init(new FabricAbstractContainerScreen<>(screen));
            }
            FabricSelectionRenderer.renderSelection(forgeGraphics.getDelegate(), screen);
//            System.out.println("runPositionAnimation");
            runPositionAnimation(ConfigHandler.overlayAnimationDuration.get());

//            System.out.println(mainFloatingPanel);
            if (mainFloatingPanel != null) {
                MinecraftClient mc = MinecraftClient.getInstance();
                if (mouseX == null) mouseX = SingletonInstances.INPUT_UTILS.getMouseX();
                if (mouseY == null) mouseY = SingletonInstances.INPUT_UTILS.getMouseY();

//                System.out.println("Rendering actual ui");
//            RenderSystem.disableDepthTest();
                if (mainFloatingPanel != null) mainFloatingPanel.render(guiGraphics, mouseX, mouseY, partialTick);
//            RenderSystem.enableDepthTest();
            }
        }
    }

    @Override
    public void replacePositionAnimationData() {
//        System.out.println("replacePositionAnimationData");
        if(!ConfigHandler.overlayAnimationEnabled.get()) return;
        if(MinecraftClient.getInstance().currentScreen == null || !(MinecraftClient.getInstance().currentScreen instanceof HandledScreen)) return;

        Quartet<Integer, Integer, Integer, Integer> lastOverlayPosition = getOverlayBoundsForScreen(new FabricAbstractContainerScreen<>((HandledScreen<?>) MinecraftClient.getInstance().currentScreen));
//        System.out.println(lastOverlayPosition + " " + mainFloatingPanel.getY());
        if (lastOverlayPosition != null && lastOverlayPosition.getB() != mainFloatingPanel.getY()) {
            positionAnimationStartY = mainFloatingPanel.getY();
            positionAnimationEndY = lastOverlayPosition.getB();
            positionAnimationActive = true;
            positionAnimationStartTime = Calendar.getInstance().getTimeInMillis();
//            System.out.println("start");

//            System.out.println("Start: " + positionAnimationStartY);
//            System.out.println("End: " + positionAnimationEndY);
        }
    }

    @Override
    protected boolean isSlotValidForSelection(ISlot slotOrig) {
        if(slotOrig == null) return false;
        if(!(slotOrig instanceof FabricSlot fabricSlot)) throw new IllegalArgumentException();
        Slot slot = fabricSlot.getDelegate();
        if(slot == null) return false;
        if(!(slot.inventory instanceof PlayerInventory || slot.inventory instanceof SimpleInventory)) return false;
        if(slot.inventory instanceof PlayerInventory && slot.getIndex() >= ((PlayerInventory) slot.inventory).main.size()) return false;
        return true;
    }

    @Override
    public List<ISlot> getPlayerInventorySlots(IAbstractContainerScreen<?> screenOrig) {
        if(!(screenOrig instanceof FabricAbstractContainerScreen<?> forgeAbstractContainerScreen)) throw new IllegalArgumentException();
        HandledScreen<?> screen = forgeAbstractContainerScreen.getDelegate();

        ScreenHandler menu = screen.getScreenHandler();
        List<ISlot> slots = new ArrayList<>(Collections.nCopies(menu.slots.size(), null));
        for(Slot slot : menu.slots) {
            if(slot.inventory instanceof PlayerInventory playerInventory) {
                slots.set(playerInventory.main.indexOf(slot.getStack()), new FabricSlot(slot));
            }
        }
        return slots;
    }

    @Override
    public int getRealSlotIndex(IAbstractContainerScreen<?> screenOrig, ISlot slotOrig, Class<?> overwriteTargetClass) {
        if(!(screenOrig instanceof FabricAbstractContainerScreen<?> forgeAbstractContainerScreen)) throw new IllegalArgumentException();
        HandledScreen<?> screen = forgeAbstractContainerScreen.getDelegate();

        if(!(slotOrig instanceof FabricSlot forgeSlot)) throw new IllegalArgumentException();
        Slot slot = forgeSlot.getDelegate();

        Class<?> targetContainerClass = UiState.selectionContainerClass;
        if(overwriteTargetClass != null) targetContainerClass = overwriteTargetClass;
        if(targetContainerClass == null) {
            if(screen instanceof InventoryScreen || screen instanceof CreativeInventoryScreen) targetContainerClass = PlayerInventory.class;
        }

        for (Slot menuSlot : screen.getScreenHandler().slots) {
            if (menuSlot.inventory.getClass().equals(targetContainerClass) && menuSlot.getIndex() == slot.getIndex()) {
                return menuSlot.getIndex();
            }
        }

        System.out.println("returning default");

        return screen.getScreenHandler().slots.indexOf(slot);
    }

    @Override
    public ISlot getRealInventorySlot(IAbstractContainerScreen<?> screenOrig, int slotIndex) {
        if(!(screenOrig instanceof FabricAbstractContainerScreen<?> forgeAbstractContainerScreen)) throw new IllegalArgumentException();
        HandledScreen<?> screen = forgeAbstractContainerScreen.getDelegate();

        for (Slot slot : screen.getScreenHandler().slots) {
            if (slot.inventory.getClass().equals(UiState.selectionContainerClass) && slot.getIndex() == slotIndex) {
                return new FabricSlot(slot);
            }
        }

        try {
            return new FabricSlot(screen.getScreenHandler().getSlot(slotIndex));
        } catch (IndexOutOfBoundsException e) {
            return null;
        }
    }

    @Override
    public void requestInventorySnapshot() {
        SingletonInstances.MINECRAFT_UTILS.execute(() ->
                backgroundProcessor.submit(() ->
                        processInventory(SingletonInstances.MINECRAFT_UTILS.getInventoryItems())));
    }
}
