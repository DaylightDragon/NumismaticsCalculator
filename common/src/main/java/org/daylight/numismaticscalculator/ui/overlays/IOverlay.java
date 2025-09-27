package org.daylight.numismaticscalculator.ui.overlays;

import org.daylight.numismaticscalculator.replacements.IGuiGraphics;
import org.daylight.numismaticscalculator.replacements.IKeyPressEvent;
import org.daylight.numismaticscalculator.replacements.IScreen;
import org.jetbrains.annotations.NotNull;

public interface IOverlay {
    void render(@NotNull IGuiGraphics guiGraphics, float partialTick, Integer mouseX, Integer mouseY);

//    void init(AbstractContainerScreen<?> screen);

//    Quartet<Integer, Integer, Integer, Integer> getOverlayBoundsForScreen(AbstractContainerScreen<?> screen);

    boolean onMouseClick(double mouseX, double mouseY, int button, IScreen screenOrig);
    boolean onMouseDrag(double mouseX, double mouseY, int button, IScreen screenOrig);
    void onMouseRelease(double mouseX, double mouseY, int button, IScreen screenOrig);

    void updateOverlayPosition(IScreen screen);
    void onKeyPressed(IKeyPressEvent event);
}
