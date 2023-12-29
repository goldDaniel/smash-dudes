package smashdudes.core.boxtool.logic.selectable;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class SelectableRectangle extends Selectable<Rectangle>
{
    private enum SelectionType
    {
        Move,
        ScaleX,
        ScaleY,
        None,
    }

    private SelectionType selectionType;


    private final float moveSelectionRadius = 0.04f;
    private final float scaleSelectionSmall = 0.04f;
    private final float scaleSelectionLarge = 0.2f;


    private final Color rectColor;

    public SelectableRectangle(Rectangle rect, Color color, OnApply onApply)
    {
        super(onApply);
        this.original = rect;
        this.clone = new Rectangle(rect);
        this.rectColor = color.cpy();

        selectionType = SelectionType.None;
    }

    @Override
    public boolean select(Vector2 mouseWorldPos)
    {
        if(selectionType != SelectionType.None) return true;

        if(getGrabCircle(original).contains(mouseWorldPos))
        {
            selectionType = SelectionType.Move;
        }
        else if(getHorizontalRect(original).contains(mouseWorldPos))
        {
            selectionType = SelectionType.ScaleX;
        }
        else if(getVerticalRect(original).contains(mouseWorldPos))
        {
            selectionType = SelectionType.ScaleY;
        }
        else
        {
            selectionType = SelectionType.None;
        }

        boolean result = selectionType != SelectionType.None;

        if(result)
        {
            clone.set(original);
        }

        return result;
    }

    @Override
    public void release()
    {
        super.release();
        selectionType = SelectionType.None;
    }

    @Override
    public void drag(Vector2 mouseWorldPos, Vector2 mouseDelta)
    {
        if(selectionType == SelectionType.Move)
        {
            clone.x = mouseWorldPos.x;
            clone.y = mouseWorldPos.y;
        }
        else if(selectionType == SelectionType.ScaleX)
        {
            clone.width += mouseDelta.x;
        }
        else if(selectionType == SelectionType.ScaleY)
        {
            clone.height += mouseDelta.y;
        }
    }

    public void draw(Vector2 mouseWorldPos, ShapeRenderer sh)
    {
        if (sh.isDrawing())
        {
            sh.end();
        }

        Rectangle rect = original;
        Color moveColor = Color.LIGHT_GRAY;
        Color scaleXColor = Color.FOREST;
        Color scaleYColor = Color.RED;
        if (selectionType != SelectionType.None)
        {
            rect = clone;
        }
        if(selectionType == SelectionType.Move || getGrabCircle(rect).contains(mouseWorldPos))
        {
            moveColor = Color.WHITE;
        }
        else if(selectionType == SelectionType.ScaleX || getHorizontalRect(rect).contains(mouseWorldPos))
        {
            scaleXColor = Color.GREEN;
        }
        else if(selectionType == SelectionType.ScaleY || getVerticalRect(rect).contains(mouseWorldPos))
        {
            scaleYColor = Color.SALMON;
        }

        sh.begin(ShapeRenderer.ShapeType.Line);
        // draw rect
        {
            sh.setColor(rectColor);
            float w = rect.width;
            float h = rect.height;
            float x = (rect.x - w / 2);
            float y = (rect.y - h / 2);

            sh.setColor(rectColor);
            sh.rect(x, y, w, h);
            sh.line(x, y, x + w, y + h);
            sh.line(x, y + h, x + w, y);
        }
        sh.end();

        sh.begin(ShapeRenderer.ShapeType.Filled);
        sh.setColor(scaleXColor);
        Rectangle horizontal = getHorizontalRect(rect);
        sh.rect(horizontal.x, horizontal.y, horizontal.width, horizontal.height);

        sh.setColor(scaleYColor);
        Rectangle vertical = getVerticalRect(rect);
        sh.rect(vertical.x, vertical.y, vertical.width, vertical.height);

        sh.setColor(moveColor);
        sh.circle(rect.x, rect.y, moveSelectionRadius, 16);

        sh.end();
    }

    private Circle getGrabCircle(Rectangle rect)
    {
        return new Circle(rect.x, rect.y, moveSelectionRadius);
    }

    private Rectangle getHorizontalRect(Rectangle rect)
    {
        Rectangle scaleRect = new Rectangle();
        scaleRect.x = rect.x;
        scaleRect.y = rect.y - scaleSelectionSmall / 2.0f;
        scaleRect.width = scaleSelectionLarge;
        scaleRect.height = scaleSelectionSmall;

        return scaleRect;
    }

    private Rectangle getVerticalRect(Rectangle rect)
    {
        Rectangle scaleRect = new Rectangle();
        scaleRect.x = rect.x - scaleSelectionSmall / 2.0f;
        scaleRect.y = rect.y;
        scaleRect.width = scaleSelectionSmall;
        scaleRect.height = scaleSelectionLarge;

        return scaleRect;
    }
}
