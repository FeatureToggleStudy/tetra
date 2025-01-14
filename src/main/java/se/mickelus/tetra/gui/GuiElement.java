package se.mickelus.tetra.gui;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import se.mickelus.tetra.gui.animation.KeyframeAnimation;

import java.util.*;
import java.util.stream.Collectors;

public class GuiElement extends Gui {

    protected int x;
    protected int y;
    protected GuiAttachment attachmentPoint = GuiAttachment.topLeft;
    protected GuiAttachment attachmentAnchor = GuiAttachment.topLeft;

    protected int width;
    protected int height;

    protected float opacity = 1;

    protected boolean hasFocus = false;

    protected boolean isVisible = true;

    protected boolean shouldRemove = false;

    protected ArrayList<GuiElement> elements;

    protected Set<KeyframeAnimation> activeAnimations;

    public GuiElement(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;

        elements = new ArrayList<>();

        activeAnimations = new HashSet<>();
    }

    public void draw(int refX, int refY, int screenWidth, int screenHeight, int mouseX, int mouseY, float opacity) {
        calculateFocusState(refX, refY, mouseX, mouseY);
        drawChildren(refX + x, refY + y, screenWidth, screenHeight, mouseX, mouseY, opacity * this.opacity);
    }

    public void updateAnimations() {
        activeAnimations.removeIf(keyframeAnimation -> !keyframeAnimation.isActive());
        activeAnimations.forEach(KeyframeAnimation::preDraw);
    }

    protected void drawChildren(int refX, int refY, int screenWidth, int screenHeight, int mouseX, int mouseY, float opacity) {
        elements.removeIf(GuiElement::shouldRemove);
        elements.stream()
                .filter(GuiElement::isVisible)
                .forEach((element -> {
                    element.updateAnimations();
                    element.draw(
                            refX + getXOffset(this, element.attachmentAnchor) - getXOffset(element, element.attachmentPoint),
                            refY + getYOffset(this, element.attachmentAnchor) - getYOffset(element, element.attachmentPoint),
                            screenWidth, screenHeight, mouseX, mouseY, opacity);
                }));
    }

    protected static int getXOffset(GuiElement element, GuiAttachment attachment) {
        switch (attachment) {
            case topLeft:
            case middleLeft:
            case bottomLeft:
                return 0;
            case topCenter:
            case middleCenter:
            case bottomCenter:
                return element.getWidth() / 2;
            case topRight:
            case middleRight:
            case bottomRight:
                return element.getWidth();
        }
        return 0;
    }

    protected static int getYOffset(GuiElement element, GuiAttachment attachment) {
        switch (attachment) {
            case topLeft:
            case topCenter:
            case topRight:
                return 0;
            case middleLeft:
            case middleCenter:
            case middleRight:
                return element.getHeight() / 2;
            case bottomCenter:
            case bottomLeft:
            case bottomRight:
                return element.getHeight();
        }
        return 0;
    }

    public boolean onClick(int x, int y) {
        // iterate reverse, elements rendered last (=topmost) should intercept clicks first
        for (int i = elements.size() - 1; i >= 0; i--) {
            if (elements.get(i).isVisible()) {
                if (elements.get(i).onClick(x, y)) {
                    return true;
                }
            }
        }

        return false;
    }

    public void mouseReleased(int x, int y) {
        elements.forEach(element -> element.mouseReleased(x, y));
    }

    protected void calculateFocusState(int refX, int refY, int mouseX, int mouseY) {
        boolean gainFocus = mouseX >= getX() + refX
                && mouseX < getX() + refX + getWidth()
                && mouseY >= getY() + refY
                && mouseY < getY() + refY + getHeight();

        if (gainFocus != hasFocus) {
            hasFocus = gainFocus;
            if (hasFocus) {
                onFocus();
            } else {
                onBlur();
            }
        }
    }

    protected void onFocus() {

    }

    protected void onBlur() {

    }

    public boolean hasFocus() {
        return hasFocus;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public GuiElement setAttachmentPoint(GuiAttachment attachment) {
        attachmentPoint = attachment;

        return this;
    }

    public GuiElement setAttachmentAnchor(GuiAttachment attachment) {
        attachmentAnchor = attachment;

        return this;
    }

    public GuiElement setAttachment(GuiAttachment attachment) {
        attachmentPoint = attachment;
        attachmentAnchor = attachment;

        return this;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setVisible(boolean visible) {
        if (isVisible != visible) {
            if (visible) {
                onShow();
            } else {
                if (!onHide()) {
                    return;
                }
            }
            isVisible = visible;
        }
    }

    public boolean isVisible() {
        return isVisible;
    }

    protected void onShow() {}

    /**
     * Can be overridden to do something when the element is hidden. Returning false indicates that the handler will
     * take care of setting isVisible to false.
     * @return
     */
    protected boolean onHide() {
        this.hasFocus = false;
        return true;
    }

    public GuiElement setOpacity(float opacity) {
        this.opacity = opacity;
        return this;
    }

    public float getOpacity() {
        return opacity;
    }

    public void addAnimation(KeyframeAnimation animation) {
        activeAnimations.add(animation);
    }

    public void removeAnimation(KeyframeAnimation animation) {
        activeAnimations.remove(animation);
    }

    public void remove() {
        shouldRemove = true;
    }

    public boolean shouldRemove() {
        return shouldRemove;
    }

    public void addChild(GuiElement child) {
        this.elements.add(child);
    }

    public void clearChildren() {
        this.elements.clear();
    }

    public int getNumChildren() {
        return elements.size();
    }

    public GuiElement getChild(int index) {
        if (index >= 0 && index < elements.size()) {
            return elements.get(index);
        }
        return null;
    }

    public List<GuiElement> getChildren() {
        return Collections.unmodifiableList(elements);
    }

    public <T> List<T> getChildrenOfType(Class<T> type) {
        return elements.stream()
                .filter(type::isInstance)
                .map(type::cast)
                .collect(Collectors.toList());
    }

    public List<String> getTooltipLines() {
        if (isVisible()) {
            return elements.stream()
                    .map(GuiElement::getTooltipLines)
                    .filter(Objects::nonNull)
                    .findFirst()
                    .orElse(null);
        }
        return null;
    }


    protected void drawRect(int left, int top, int right, int bottom, int color, float opacity) {
        if (left < right) {
            int i = left;
            left = right;
            right = i;
        }

        if (top < bottom) {
            int j = top;
            top = bottom;
            bottom = j;
        }

        float red = (float)(color >> 16 & 255) / 255.0F;
        float green = (float)(color >> 8 & 255) / 255.0F;
        float blue = (float)(color & 255) / 255.0F;
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tessellator.getBuffer();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.color(red, green, blue, opacity);
        bufferBuilder.begin(7, DefaultVertexFormats.POSITION);
        bufferBuilder.pos((double)left, (double)bottom, zLevel).endVertex();
        bufferBuilder.pos((double)right, (double)bottom, zLevel).endVertex();
        bufferBuilder.pos((double)right, (double)top, zLevel).endVertex();
        bufferBuilder.pos((double)left, (double)top, zLevel).endVertex();
        tessellator.draw();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }

    protected static int colorWithOpacity(int color, float opacity) {
        return colorWithOpacity(color, Math.round(opacity * 255));
    }

    protected static int colorWithOpacity(int color, int opacity) {
        // replace alpha bits with passed opacity value, multiples opacity with current alpha bits if they are present
        return color & 0xffffff | (opacity * (color >> 24 == 0 ? 255 : color >> 24 & 255) / 255 << 24);
    }
}
