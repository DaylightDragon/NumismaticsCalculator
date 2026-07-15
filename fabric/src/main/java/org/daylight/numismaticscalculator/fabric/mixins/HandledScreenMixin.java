package org.daylight.numismaticscalculator.fabric.mixins;

import net.minecraft.client.gui.screen.ingame.HandledScreen;
import org.daylight.numismaticscalculator.UiState;
import org.daylight.numismaticscalculator.fabric.event.FabricScreenEvents;
import org.daylight.numismaticscalculator.replacements.SingletonInstances;
import org.daylight.numismaticscalculator.ui.overlays.ICalculatorOverlay;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HandledScreen.class)
public class HandledScreenMixin { // doesn't need a mixin in forge
    @Inject(method = "close", at=@At("HEAD"))
    public void close(CallbackInfo ci) {
        SingletonInstances.CALCULATOR_OVERLAY.disableSelection();
    }
}
