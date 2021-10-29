package smashdudes.ecs.systems;

import smashdudes.ecs.Engine;
import smashdudes.ecs.Entity;
import smashdudes.ecs.components.InAirComponent;
import smashdudes.ecs.components.OnGroundComponent;
import smashdudes.ecs.components.PlayerComponent;
import smashdudes.ecs.components.PositionComponent;

public class InAirSystem extends GameSystem
{
    public InAirSystem(Engine engine)
    {
        super(engine);
        registerComponentType(PositionComponent.class);
        registerComponentType(PlayerComponent.class);
        registerComponentType(OnGroundComponent.class);
    }

    @Override
    protected void updateEntity(Entity entity, float dt)
    {
        if (entity.getComponent(OnGroundComponent.class) != null && entity.getComponent(InAirComponent.class) == null)
        {
            entity.addComponent(new InAirComponent());
            entity.removeComponent(OnGroundComponent.class);
        }

    }
}
