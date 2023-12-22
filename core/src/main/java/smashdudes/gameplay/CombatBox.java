package smashdudes.gameplay;

import com.badlogic.gdx.math.Rectangle;

public abstract class CombatBox extends Rectangle
{
    public CombatBox()
    {
        super(new Rectangle());
    }

    public CombatBox(Rectangle rect)
    {
        super(rect);
    }

    public abstract CombatBox clone();
}
