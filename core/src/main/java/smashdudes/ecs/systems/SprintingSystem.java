package smashdudes.ecs.systems;

import smashdudes.ecs.Engine;
import smashdudes.ecs.Entity;
import smashdudes.ecs.components.*;

public class SprintingSystem extends GameSystem
{
    public SprintingSystem(Engine engine)
    {
        super(engine);
        registerComponentType(PlayerComponent.class);
        registerComponentType(OnGroundComponent.class);
        registerComponentType(VelocityComponent.class);
        registerComponentType(PositionComponent.class);
        registerComponentType(SprintingComponent.class);
    }

    @Override
    protected void updateEntity(Entity entity, float dt)
    {
        VelocityComponent v = entity.getComponent(VelocityComponent.class);
        SprintingComponent s = entity.getComponent(SprintingComponent.class);

        v.velocity.x = s.percentFaster * v.velocity.x;
    }
}
