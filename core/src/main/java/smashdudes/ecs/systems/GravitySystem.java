package smashdudes.ecs.systems;

import smashdudes.ecs.Engine;
import smashdudes.ecs.Entity;
import smashdudes.ecs.GameSystem;
import smashdudes.ecs.components.GravityComponent;
import smashdudes.ecs.components.VelocityComponent;

public class GravitySystem extends GameSystem
{
    public GravitySystem(Engine engine)
    {
        super(engine);
        registerComponentType(VelocityComponent.class);
        registerComponentType(GravityComponent.class);
    }

    @Override
    public void updateEntity(Entity entity, float dt)
    {
        VelocityComponent v = entity.getComponent(VelocityComponent.class);
        GravityComponent g = entity.getComponent(GravityComponent.class);

        v.velocity.y -= g.gravityStrength * dt;
    }
}
