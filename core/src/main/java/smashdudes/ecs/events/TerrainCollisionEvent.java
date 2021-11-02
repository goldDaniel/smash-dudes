package smashdudes.ecs.events;

import smashdudes.core.Collisions;
import smashdudes.ecs.Entity;

public class TerrainCollisionEvent extends Event
{
    public final Collisions.CollisionSide collisionSide;

    public TerrainCollisionEvent(Entity entity, Collisions.CollisionSide side)
    {
        super(entity);
        this.collisionSide = side;
    }
}
