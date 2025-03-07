package se.mickelus.tetra.gui.impl.statbar;

import se.mickelus.tetra.gui.*;
import se.mickelus.tetra.gui.impl.GuiColors;

public class GuiBarSplit extends GuiBar {

    private GuiBar negativeBar;
    private GuiBar positiveBar;

    public GuiBarSplit(int x, int y, int barLength, double range) {
        super(x, y, barLength, -range, range);

        negativeBar = new GuiBar(0, 0, (barLength - 5) / 2, 0, range, true);
        negativeBar.setAlignment(GuiAlignment.right);
        addChild(negativeBar);

        positiveBar = new GuiBar(0, 0, (barLength - 5) / 2, 0, range);
        positiveBar.setAttachment(GuiAttachment.topRight);
        addChild(positiveBar);

        GuiElement separator = new GuiRect(0, 5, 1, 3, GuiColors.muted);
        separator.setAttachment(GuiAttachment.topCenter);
        addChild(separator);
    }

    protected void calculateBarLengths() {
        negativeBar.setValue(value > 0 ? 0 : -value, diffValue > 0 ? 0 : -diffValue);
        positiveBar.setValue(value < 0 ? 0 : value, diffValue < 0 ? 0 : diffValue);
    }

    @Override
    public void draw(int refX, int refY, int screenWidth, int screenHeight, int mouseX, int mouseY, float opacity) {
        drawChildren(refX, refY, screenWidth, screenHeight, mouseX, mouseY, opacity);
    }
}
