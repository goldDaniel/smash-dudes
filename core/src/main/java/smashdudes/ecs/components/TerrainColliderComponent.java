package smashdudes.ecs.components;

import com.badlogic.gdx.math.Rectangle;
import smashdudes.ecs.Component;

public class TerrainColliderComponent extends Component
{
    private final Rectangle collider;

    public TerrainColliderComponent(Rectangle collider)
    {
        this.collider = new Rectangle(collider);
    }

    public Rectangle getCollider(boolean flip)
    {
        int dir = flip ? -1 : 1;

        Rectangle result = new Rectangle(collider);
        result.x = dir * result.x;

        return  result;
    }
}
