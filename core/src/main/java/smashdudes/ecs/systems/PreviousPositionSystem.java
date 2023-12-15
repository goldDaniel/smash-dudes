package smashdudes.ecs.systems;

import smashdudes.ecs.Engine;
import smashdudes.ecs.Entity;
import smashdudes.ecs.components.PositionComponent;

public class PreviousPositionSystem extends GameSystem
{
    public PreviousPositionSystem(Engine engine)
    {
        super(engine);
        registerComponentType(PositionComponent.class);
    }

    @Override
    protected void updateEntity(Entity entity, float dt)
    {
        PositionComponent p = entity.getComponent(PositionComponent.class);
        p.prevPosition.set(p.position);
    }
}
