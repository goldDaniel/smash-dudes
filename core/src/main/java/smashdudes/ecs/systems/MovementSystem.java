package smashdudes.ecs.systems;

import smashdudes.ecs.Engine;
import smashdudes.ecs.Entity;
import smashdudes.ecs.components.PositionComponent;
import smashdudes.ecs.components.VelocityComponent;

public class MovementSystem extends GameSystem
{
    public MovementSystem(Engine engine)
    {
        super(engine);
        registerComponentType(PositionComponent.class);
        registerComponentType(VelocityComponent.class);
    }

    @Override
    public void updateEntity(Entity entity, float dt)
    {
        PositionComponent p = entity.getComponent(PositionComponent.class);
        VelocityComponent v = entity.getComponent(VelocityComponent.class);

        p.position.x += v.velocity.x * dt;
        p.position.y += v.velocity.y * dt;

        float decelerationThisFrame = v.deceleration * dt;
        if (Math.abs(v.velocity.x) > decelerationThisFrame) {
            v.velocity.x -= Math.signum(v.velocity.x) * decelerationThisFrame;
        } else {
            v.velocity.x = 0;
        }
    }
}