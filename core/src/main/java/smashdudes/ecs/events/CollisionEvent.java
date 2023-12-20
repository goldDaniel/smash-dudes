package smashdudes.ecs.events;

import com.badlogic.gdx.math.Vector2;
import smashdudes.ecs.Entity;

public class CollisionEvent extends Event
{
    public final Vector2 collisionPoint;
    public CollisionEvent(Entity entity, Vector2 collisionPoint)
    {
        super(entity);
        this.collisionPoint = collisionPoint;
    }
}
