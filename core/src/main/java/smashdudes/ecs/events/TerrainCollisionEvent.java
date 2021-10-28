package smashdudes.ecs.events;

import smashdudes.ecs.Entity;
import smashdudes.ecs.systems.TerrainCollisionSystem;

public class TerrainCollisionEvent extends Event
{
    public final Entity entity;
    public final TerrainCollisionSystem.CollisionSide collisionSide;

    public TerrainCollisionEvent(Entity entity, TerrainCollisionSystem.CollisionSide side)
    {
        this.entity = entity;
        this.collisionSide = side;
    }
}
