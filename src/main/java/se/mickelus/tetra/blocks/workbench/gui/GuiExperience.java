package se.mickelus.tetra.blocks.workbench.gui;

import net.minecraft.client.resources.I18n;
import se.mickelus.tetra.gui.GuiElement;
import se.mickelus.tetra.gui.GuiString;
import se.mickelus.tetra.gui.GuiStringOutline;
import se.mickelus.tetra.gui.GuiTexture;

import java.util.Collections;
import java.util.List;

public class GuiExperience extends GuiElement {
    private static final String texture = "textures/gui/workbench.png";

    private static final int positiveColor = 0xc8ff8f;
    private static final int negativeColor = 0x8c605d;

    private GuiTexture indicator;
    private GuiString levelString;

    private String unlocalizedTooltip;
    private List<String> formattedTooltip;

    public GuiExperience(int x, int y) {
        this(x, y, null);
    }

    public GuiExperience(int x, int y, String unlocalizedTooltip) {
        super(x, y, 16, 16);

        indicator = new GuiTexture(0, 0, 16, 16, 0, 0, texture);
        addChild(indicator);

        levelString = new GuiStringOutline(8, 2, "");
        addChild(levelString);

        this.unlocalizedTooltip = unlocalizedTooltip;
    }

    public void update(int level, boolean positive) {
        indicator.setTextureCoordinates(Math.min(level, 3) * 16 + 112, positive ? 0 : 16);

        levelString.setString(level + "");
        levelString.setColor(positive ? positiveColor : negativeColor);

        if (unlocalizedTooltip != null) {
            formattedTooltip = Collections.singletonList(I18n.format(unlocalizedTooltip, level));
        }
    }

    @Override
    public List<String> getTooltipLines() {
        if (formattedTooltip != null && hasFocus()) {
            return formattedTooltip;
        }

        return super.getTooltipLines();
    }
}
