package smashdudes.ecs.events;

import smashdudes.core.Collisions;
import smashdudes.ecs.Entity;

public class TerrainCollisionEvent extends Event
{
    public final Entity entity;
    public final Collisions.CollisionSide collisionSide;

    public TerrainCollisionEvent(Entity entity, Collisions.CollisionSide side)
    {
        this.entity = entity;
        this.collisionSide = side;
    }
}
