package smashdudes.ecs.systems;

import smashdudes.ecs.Engine;
import smashdudes.ecs.Entity;
import smashdudes.ecs.components.PositionComponent;
import smashdudes.ecs.components.VelocityComponent;
import smashdudes.ecs.events.Event;
import smashdudes.ecs.events.HitEvent;

public class MovementSystem extends GameSystem
{
    public MovementSystem(Engine engine)
    {
        super(engine);
        registerComponentType(PositionComponent.class);
        registerComponentType(VelocityComponent.class);

        registerEventType(HitEvent.class);
    }

    @Override
    public void updateEntity(Entity entity, float dt)
    {
        PositionComponent p = entity.getComponent(PositionComponent.class);
        VelocityComponent v = entity.getComponent(VelocityComponent.class);

        p.position.x += v.velocity.x * dt;
        p.position.y += v.velocity.y * dt;
    }

    @Override
    public void handleEvent(Event event)
    {
        if (event instanceof HitEvent)
        {
            HitEvent e = (HitEvent)event;

            VelocityComponent v = e.entity.getComponent(VelocityComponent.class);
            v.velocity.add(e.attackLine.cpy().scl(2));
        }
    }
}