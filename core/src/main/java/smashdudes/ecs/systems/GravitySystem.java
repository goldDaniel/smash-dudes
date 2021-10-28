package smashdudes.ecs.systems;

import smashdudes.ecs.Engine;
import smashdudes.ecs.Entity;
import smashdudes.ecs.components.GravityComponent;
import smashdudes.ecs.components.JumpComponent;
import smashdudes.ecs.components.VelocityComponent;
import smashdudes.ecs.events.Event;
import smashdudes.ecs.events.TerrainCollisionEvent;

public class GravitySystem extends GameSystem
{
    public GravitySystem(Engine engine)
    {
        super(engine);
        registerComponentType(VelocityComponent.class);
        registerComponentType(GravityComponent.class);

        registerEventType(TerrainCollisionEvent.class);
    }

    @Override
    public void updateEntity(Entity entity, float dt)
    {
        VelocityComponent v = entity.getComponent(VelocityComponent.class);
        GravityComponent g = entity.getComponent(GravityComponent.class);

        v.velocity.y -= g.gravityStrength * dt;
    }

    @Override
    protected void handleEvent(Event event)
    {
        if(event instanceof TerrainCollisionEvent)
        {
            TerrainCollisionEvent e = (TerrainCollisionEvent)event;
            if(e.collisionSide == TerrainCollisionSystem.CollisionSide.Top)
            {
                VelocityComponent v = e.entity.getComponent(VelocityComponent.class);
                v.velocity.y = 0;
            }
        }
    }
}