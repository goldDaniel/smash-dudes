package smashdudes.ecs.systems;

import smashdudes.ecs.Engine;
import smashdudes.ecs.Entity;
import smashdudes.ecs.components.DynamicTerrainComponent;
import smashdudes.ecs.components.PositionComponent;
import smashdudes.ecs.components.VelocityComponent;

public class DynamicTerrainMovementSystem extends GameSystem
{
    public DynamicTerrainMovementSystem(Engine engine)
    {
        super(engine);
        registerComponentType(PositionComponent.class);
        registerComponentType(DynamicTerrainComponent.class);
        registerComponentType(VelocityComponent.class);
    }

    @Override
    protected void updateEntity(Entity entity, float dt)
    {
        DynamicTerrainComponent d = entity.getComponent(DynamicTerrainComponent.class);
        PositionComponent p = entity.getComponent(PositionComponent.class);
        VelocityComponent v = entity.getComponent(VelocityComponent.class);

        d.prevPos.set(p.position);
    }
}
