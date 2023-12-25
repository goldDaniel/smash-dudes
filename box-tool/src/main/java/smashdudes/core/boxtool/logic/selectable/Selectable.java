package smashdudes.core.boxtool.logic.selectable;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

public abstract class Selectable<T>
{
    public interface OnApply
    {
        void execute(Selectable selectable);
    }

    protected final OnApply onApply;

    protected T original;
    protected T clone;

    public Selectable(OnApply apply)
    {
        this.onApply = apply;
    }

    public abstract boolean select(Vector2 mouseWorldPos);

    public void release()
    {
        onApply.execute(this);
    }

    public abstract void drag(Vector2 mouseWorldPos, Vector2 mouseDelta);

    public abstract void draw(Vector2 mouseWorldPos, ShapeRenderer sh);

    public final T getOriginal()
    {
        return original;
    }

    public final T getClone()
    {
        return clone;
    }
}
