package smashdudes.core.boxtool.logic;

import com.badlogic.gdx.math.Rectangle;


public class AnimationSelectionContext
{
    private static class SelectedRectangle
    {
        public Rectangle original = null;
        public Rectangle clone = null;
        SelectionType type = SelectionType.None;
        public boolean horizontal = false;
    }

    public static final float boxCenterRadius = 0.04f;
    public static final float scaleSelectionLength = 0.2f;
    public static final float scaleSelectionWidth = 0.06f;

    private final SelectedRectangle selectedRectangle;

    public AnimationSelectionContext()
    {
        selectedRectangle = new SelectedRectangle();
    }

    public boolean isSelectionType(SelectionType type)
    {
        return selectedRectangle.type == type;
    }

    public void setRectangle(Rectangle rect, SelectionType type)
    {
        if(rect == null) type = SelectionType.None;
        setRectangle(rect, type, true);
    }

    public void setRectangle(Rectangle rect, SelectionType type, boolean horizontal)
    {
        selectedRectangle.original = rect;
        selectedRectangle.clone = rect != null ? new Rectangle(rect) : null;
        selectedRectangle.type = type;
        selectedRectangle.horizontal = horizontal;
    }

    public boolean isHorizontalScaling()
    {
        return selectedRectangle.horizontal;
    }


    public SelectionType getSelectionType()
    {
        return selectedRectangle.type;
    }

    public Rectangle getRectangleForEdit()
    {
        return selectedRectangle.clone;
    }

    public Rectangle getRectangleForSave()
    {
        return selectedRectangle.original;
    }
}
