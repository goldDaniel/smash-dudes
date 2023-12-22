package smashdudes.gameplay;

import com.badlogic.gdx.math.Rectangle;

public class BodyBox extends CombatBox
{
    public BodyBox()
    {
        this(new Rectangle());
    }

    public BodyBox(Rectangle rect)
    {
        super(rect);
    }

    @Override
    public BodyBox clone()
    {
        return new BodyBox(this);
    }
}
