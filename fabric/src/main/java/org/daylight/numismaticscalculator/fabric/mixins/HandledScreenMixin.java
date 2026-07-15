package org.daylight.numismaticscalculator.fabric.mixins;

import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import org.daylight.numismaticscalculator.replacements.SingletonInstances;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractContainerScreen.class)
public class HandledScreenMixin { // doesn't need a mixin in forge
    @Inject(method = "onClose", at=@At("HEAD"))
    public void close(CallbackInfo ci) {
        SingletonInstances.CALCULATOR_OVERLAY.disableSelection();
    }
}
