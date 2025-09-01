package org.daylight.coinscalculator.ui;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import org.daylight.coinscalculator.CoinValues;
import org.joml.Matrix4f;

public class MainWidget {
    public static void drawWidget(Minecraft mc, RenderGuiOverlayEvent.Post event, int x, int y) {
        ItemStack stack = mc.player.getMainHandItem();
        int value = CoinValues.ITEM_TO_VALUE.getOrDefault(stack.getItem(), 0) * stack.getCount();

        String text = value + "¤";

        PoseStack poseStack = event.getGuiGraphics().pose(); // это MatrixStack, из которого можно получить Matrix4f
        Matrix4f matrix = poseStack.last().pose();
        MultiBufferSource.BufferSource buffer = mc.renderBuffers().bufferSource();

        mc.font.drawInBatch(
                text,
                10, // X
                10, // Y
                0xFFFF00, // цвет
                true,     // тень
                matrix,
                buffer,
                Font.DisplayMode.NORMAL,
                0,        // фон
                0xF000F0  // свет
        );

        buffer.endBatch(); // обязательно завершить батч
    }
}
