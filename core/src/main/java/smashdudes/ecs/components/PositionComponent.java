package smashdudes.ecs.components;

import com.badlogic.gdx.math.Vector2;
import smashdudes.ecs.Component;

public class PositionComponent extends Component
{
    public final Vector2 prevPosition;
    public final Vector2 position;

    public PositionComponent()
    {
        position = new Vector2();
        prevPosition = new Vector2();
    }

    public PositionComponent(Vector2 pos)
    {
        position = pos.cpy();
        prevPosition = position.cpy();
    }
}
