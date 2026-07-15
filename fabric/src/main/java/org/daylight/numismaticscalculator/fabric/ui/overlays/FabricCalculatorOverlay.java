package org.daylight.numismaticscalculator.fabric.ui.overlays;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import org.daylight.numismaticscalculator.UiState;
import org.daylight.numismaticscalculator.fabric.config.ConfigHandler;
import org.daylight.numismaticscalculator.fabric.replacements.api.FabricAbstractContainerScreen;
import org.daylight.numismaticscalculator.fabric.replacements.api.FabricGuiGraphics;
import org.daylight.numismaticscalculator.fabric.replacements.api.FabricScreen;
import org.daylight.numismaticscalculator.fabric.replacements.api.FabricSlot;
import org.daylight.numismaticscalculator.fabric.ui.FabricSelectionRenderer;
import org.daylight.numismaticscalculator.replacements.*;
import org.daylight.numismaticscalculator.ui.overlays.ICalculatorOverlay;
import org.daylight.numismaticscalculator.util.tuples.Quartet;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class FabricCalculatorOverlay extends ICalculatorOverlay {
    @Override
    public boolean shouldRenderOnScreen(IScreen screenOriginal) {
        if(!(screenOriginal instanceof FabricScreen FabricScreen)) throw new IllegalArgumentException();
        Screen screen = FabricScreen.getDelegate();

        return screen instanceof AbstractContainerScreen<?>;
    }

    @Override
    public void render(@NotNull IGuiGraphics guiGraphics, float partialTick, Integer mouseX, Integer mouseY) {
        if(!(guiGraphics instanceof FabricGuiGraphics forgeGraphics)) throw new IllegalArgumentException();

//        System.out.println(UiState.coinCalculatorOverlayActive);
        if(!UiState.coinCalculatorOverlayActive) return;
        if (shouldRenderOnScreen(new FabricScreen(Minecraft.getInstance().screen))) {
            AbstractContainerScreen<?> screen = (AbstractContainerScreen<?>) Minecraft.getInstance().screen;
            if (mainFloatingPanel == null) {
                init(new FabricAbstractContainerScreen<>(screen));
            }
            FabricSelectionRenderer.renderSelection(forgeGraphics.getDelegate(), screen);
//            System.out.println("runPositionAnimation");
            runPositionAnimation(ConfigHandler.overlayAnimationDuration.get());

//            System.out.println(mainFloatingPanel);
            if (mainFloatingPanel != null) {
                Minecraft mc = Minecraft.getInstance();
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
        if(Minecraft.getInstance().screen == null || !(Minecraft.getInstance().screen instanceof AbstractContainerScreen)) return;

        Quartet<Integer, Integer, Integer, Integer> lastOverlayPosition = getOverlayBoundsForScreen(new FabricAbstractContainerScreen<>((AbstractContainerScreen<?>) Minecraft.getInstance().screen));
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
        if(!(slot.container instanceof Inventory || slot.container instanceof SimpleContainer)) return false;
        if(slot.container instanceof Inventory && slot.index >= ((Inventory) slot.container).items.size()) return false;
        return true;
    }

    @Override
    public List<ISlot> getPlayerInventorySlots(IAbstractContainerScreen<?> screenOrig) {
        if(!(screenOrig instanceof FabricAbstractContainerScreen<?> forgeAbstractContainerScreen)) throw new IllegalArgumentException();
        AbstractContainerScreen<?> screen = forgeAbstractContainerScreen.getDelegate();

        AbstractContainerMenu menu = screen.getMenu();
        List<ISlot> slots = new ArrayList<>(Collections.nCopies(menu.slots.size(), null));
        for(Slot slot : menu.slots) {
            if(slot.container instanceof Inventory playerInventory) {
                slots.set(playerInventory.items.indexOf(slot.getItem()), new FabricSlot(slot));
            }
        }
        return slots;
    }

    @Override
    public int getRealSlotIndex(IAbstractContainerScreen<?> screenOrig, ISlot slotOrig, Class<?> overwriteTargetClass) {
        if(!(screenOrig instanceof FabricAbstractContainerScreen<?> forgeAbstractContainerScreen)) throw new IllegalArgumentException();
        AbstractContainerScreen<?> screen = forgeAbstractContainerScreen.getDelegate();

        if(!(slotOrig instanceof FabricSlot forgeSlot)) throw new IllegalArgumentException();
        Slot slot = forgeSlot.getDelegate();

        Class<?> targetContainerClass = UiState.selectionContainerClass;
        if(overwriteTargetClass != null) targetContainerClass = overwriteTargetClass;
        if(targetContainerClass == null) {
            if(screen instanceof InventoryScreen || screen instanceof CreativeModeInventoryScreen) targetContainerClass = Inventory.class;
        }

        for (Slot menuSlot : screen.getMenu().slots) {
            if (menuSlot.container.getClass().equals(targetContainerClass) && menuSlot.index == slot.index) {
                return menuSlot.index;
            }
        }

//        System.out.println("returning default");

        return screen.getMenu().slots.indexOf(slot);
    }

    @Override
    public ISlot getRealInventorySlot(IAbstractContainerScreen<?> screenOrig, int slotIndex) {
        if(!(screenOrig instanceof FabricAbstractContainerScreen<?> forgeAbstractContainerScreen)) throw new IllegalArgumentException();
        AbstractContainerScreen<?> screen = forgeAbstractContainerScreen.getDelegate();

        for (Slot slot : screen.getMenu().slots) {
            if (slot.container.getClass().equals(UiState.selectionContainerClass) && slot.index == slotIndex) {
                return new FabricSlot(slot);
            }
        }

        try {
            return new FabricSlot(screen.getMenu().getSlot(slotIndex));
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
