package smashdudes.core.logic.selectable;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;


public class SelectableMovable extends Selectable<Vector2>
{
    private boolean selected = false;
    private final float moveSelectionRadius = 0.1f;

    public SelectableMovable(Vector2 p, OnApply apply)
    {
        super(apply);
        this.original = p;
        this.clone = p.cpy();
    }

    @Override
    public boolean select(Vector2 mouseWorldPos)
    {
        selected = false;
        if(getGrabCircle(original).contains(mouseWorldPos))
        {
            selected = true;
            clone.set(original);
        }

        return selected;
    }

    @Override
    public void release()
    {
        super.release();
        selected = false;
    }

    @Override
    public void drag(Vector2 mouseWorldPos, Vector2 mouseDelta)
    {
        clone.x = mouseWorldPos.x;
        clone.y = mouseWorldPos.y;
    }

    @Override
    public void draw(Vector2 mouseWorldPos, ShapeRenderer sh)
    {
        if (sh.isDrawing())
        {
            sh.end();
        }

        Vector2 p = original;
        Color color = Color.LIGHT_GRAY;

        if(selected || getGrabCircle(clone).contains(mouseWorldPos))
        {
            p = clone;
            color = Color.WHITE;
        }

        sh.setColor(color);
        sh.begin(ShapeRenderer.ShapeType.Filled);
        sh.circle(p.x, p.y, moveSelectionRadius, 16);
        sh.end();
    }

    private Circle getGrabCircle(Vector2 p)
    {
        return new Circle(p.x, p.y, moveSelectionRadius);
    }
}
