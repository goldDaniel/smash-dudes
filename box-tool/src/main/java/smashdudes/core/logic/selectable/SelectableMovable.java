package smashdudes.core.logic.selectable;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

public class SelectableMovable extends Selectable<Vector2>
{
    private final float moveSelectionRadius = 0.04f;

    public SelectableMovable(OnApply apply)
    {
        super(apply);
    }

    @Override
    public boolean select(Vector2 mouseWorldPos)
    {
        return false;
    }

    @Override
    public void drag(Vector2 mouseWorldPos, Vector2 mouseDelta)
    {

    }

    @Override
    public void draw(Vector2 mouseWorldPos, ShapeRenderer sh)
    {

    }
}
