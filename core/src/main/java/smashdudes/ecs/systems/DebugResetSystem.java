package smashdudes.ecs.systems;

import smashdudes.ecs.Engine;
import smashdudes.ecs.Entity;
import smashdudes.ecs.components.DebugDrawComponent;

public class DebugResetSystem extends GameSystem
{
    public DebugResetSystem(Engine engine)
    {
        super(engine);

        registerComponentType(DebugDrawComponent.class);
    }

    @Override
    protected void updateEntity(Entity entity, float dt)
    {
        entity.getComponent(DebugDrawComponent.class).reset();
    }
}
