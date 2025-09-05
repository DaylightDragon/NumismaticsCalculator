package org.daylight.coinscalculator.ui.overlays;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;

public class CoinsOverlay implements IGuiOverlay {
    @Override
    public void render(ForgeGui gui, GuiGraphics guiGraphics, float partialTick, int screenWidth, int screenHeight) {
        Minecraft mc = Minecraft.getInstance();

//        if (mc.screen == null || !(mc.screen instanceof InventoryScreen || mc.screen instanceof CreativeModeInventoryScreen)) {
//            return;
//        }

        String coinText = "Overlay"; // Здесь должна быть логика подсчёта монет
        Component text = Component.keybind(coinText).setStyle(Style.EMPTY.withColor(TextColor.fromRgb(0xFFFF00)));

        int x = 10; // screenWidth / 2;
        int y = screenHeight - 20; // / 2;

        PoseStack poseStack = guiGraphics.pose();
        poseStack.pushPose();
        poseStack.translate(0, 0, 200); // Z поверх всего

        mc.font.drawInBatch(text, x, y, 0xFFFF00,
                true,
                poseStack.last().pose(),
                mc.renderBuffers().bufferSource(),
                Font.DisplayMode.NORMAL,
                0,
                0xF000F0
        );
    }
}
