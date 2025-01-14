package se.mickelus.tetra.items.journal.gui.craft;

import se.mickelus.tetra.blocks.workbench.gui.GuiModuleGlyph;
import se.mickelus.tetra.gui.*;
import se.mickelus.tetra.module.schema.OutcomePreview;

import java.util.function.Consumer;

public class GuiJournalVariantMajorItem extends GuiJournalVariantItem {

    public GuiJournalVariantMajorItem(int x, int y, OutcomePreview outcome, Consumer<OutcomePreview> onHover, Consumer<OutcomePreview> onBlur,
            Consumer<OutcomePreview> onSelect) {
        super(x, y, 16, 16, outcome, onHover, onBlur, onSelect);

        backdrop = new GuiTexture(1, 0, 15, 15, 52,0, texture);
        addChild(backdrop);

        addChild(new GuiModuleGlyph(0, 0, 16, 16, outcome.glyph).setShift(false));
    }

    @Override
    protected void calculateFocusState(int refX, int refY, int mouseX, int mouseY) {
        mouseX -= refX + x;
        mouseY -= refY + y;
        boolean gainFocus = true;

        if (mouseX + mouseY < 8) {
            gainFocus = false;
        }

        if (mouseX + mouseY > 24) {
            gainFocus = false;
        }

        if (mouseX - mouseY > 8) {
            gainFocus = false;
        }

        if (mouseY - mouseX > 8) {
            gainFocus = false;
        }

        if (gainFocus != hasFocus) {
            hasFocus = gainFocus;
            if (hasFocus) {
                onFocus();
            } else {
                onBlur();
            }
        }
    }
}
