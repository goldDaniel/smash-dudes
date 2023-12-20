package smashdudes.ecs.systems;

import smashdudes.ecs.Engine;
import smashdudes.ecs.Entity;
import smashdudes.ecs.components.VelocityComponent;
import smashdudes.ecs.events.CollisionEvent;
import smashdudes.ecs.events.Event;
import smashdudes.ecs.events.LandingEvent;

public class LandingSystem extends GameSystem
{
    public LandingSystem(Engine engine)
    {
        super(engine);

        registerComponentType(VelocityComponent.class);
        registerEventType(CollisionEvent.class);
    }

    @Override
    public void handleEvent(Event event)
    {
        Entity entity = event.entity;

        if(event instanceof CollisionEvent)
        {
            VelocityComponent v = entity.getComponent(VelocityComponent.class);
            if(v.velocity.y <= 0)
            {
                engine.addEvent(new LandingEvent(entity, ((CollisionEvent) event).collisionPoint));
            }
        }
    }
}
