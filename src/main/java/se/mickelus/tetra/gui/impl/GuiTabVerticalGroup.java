package se.mickelus.tetra.gui.impl;

import se.mickelus.tetra.gui.GuiElement;
import se.mickelus.tetra.gui.GuiRect;
import se.mickelus.tetra.gui.impl.GuiTabVerticalButton;

import java.util.function.Consumer;

public class GuiTabVerticalGroup extends GuiElement {

    private static final char[] keybindings = new char[] {'a', 's', 'd', 'f', 'g'};

    private GuiTabVerticalButton[] buttons;

    private Consumer<Integer> clickHandler;

    public GuiTabVerticalGroup(int x, int y, Consumer<Integer> clickHandler, String ... labels) {
        super(x, y, 3, labels.length * 16 + 1);

        buttons = new GuiTabVerticalButton[labels.length];

        for (int i = 0; i < labels.length; i++) {
            final int index = i;
            buttons[i] = new GuiTabVerticalButton(1, 1 + 16 * i, labels[i],
                    i < keybindings.length ? keybindings[i] + "" : null,
                    () -> {
                        clickHandler.accept(index);
                        setActive(index);
                    },
                    i == 0);
            addChild(buttons[i]);
        }

        this.clickHandler = clickHandler;
    }

    public void setActive(int index) {
        for (int i = 0; i < buttons.length; i++) {
            buttons[i].setActive(i == index);
        }
    }

    public void setHasContent(int index, boolean hasContent) {
        buttons[index].setHasContent(hasContent);
    }

    public void keyTyped(char typedChar) {
        for (int i = 0; i < buttons.length; i++) {
            if (i < keybindings.length && keybindings[i] == typedChar) {
                setActive(i);
                clickHandler.accept(i);
            }
        }
    }
}
