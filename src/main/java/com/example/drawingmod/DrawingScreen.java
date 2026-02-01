package com.example.drawingmod;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.client.gui.GuiGraphics;
import java.util.ArrayList;
import java.util.List;

public class DrawingScreen extends Screen {
    private static class Point {
        float x, y;
        int color;
        Point(float x, float y, int color) {
            this.x = x;
            this.y = y;
            this.color = color;
        }
    }

    private static final List<List<Point>> lines = new ArrayList<>();
    private List<Point> currentLine = null;
    private int currentColor = 0xFFFFFFFF; // White

    protected DrawingScreen() {
        super(Component.literal("Drawing Menu"));
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(guiGraphics);
        super.render(guiGraphics, mouseX, mouseY, partialTick);

        // Draw instructions
        guiGraphics.drawString(this.font, "Hold Left Click to Draw", 10, 10, 0xFFFFFF);
        guiGraphics.drawString(this.font, "Press C to Clear", 10, 25, 0xFFFFFF);
        guiGraphics.drawString(this.font, "Press ESC to Close", 10, 40, 0xFFFFFF);

        // Render all lines
        for (List<Point> line : lines) {
            for (int i = 0; i < line.size() - 1; i++) {
                Point p1 = line.get(i);
                Point p2 = line.get(i + 1);
                guiGraphics.fill((int)p1.x, (int)p1.y, (int)p2.x + 2, (int)p2.y + 2, p1.color);
            }
        }
        
        // Render current line
        if (currentLine != null) {
            for (int i = 0; i < currentLine.size() - 1; i++) {
                Point p1 = currentLine.get(i);
                Point p2 = currentLine.get(i + 1);
                guiGraphics.fill((int)p1.x, (int)p1.y, (int)p2.x + 2, (int)p2.y + 2, p1.color);
            }
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button == 0) { // Left Click
            currentLine = new ArrayList<>();
            currentLine.add(new Point((float)mouseX, (float)mouseY, currentColor));
            return true;
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        if (button == 0 && currentLine != null) {
            currentLine.add(new Point((float)mouseX, (float)mouseY, currentColor));
            return true;
        }
        return super.mouseDragged(mouseX, mouseY, button, dragX, dragY);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (button == 0 && currentLine != null) {
            lines.add(currentLine);
            currentLine = null;
            return true;
        }
        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == 67) { // 'C' key
            lines.clear();
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
