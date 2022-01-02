package smashdudes.ecs.systems;

import smashdudes.ecs.Engine;
import smashdudes.ecs.Entity;
import smashdudes.ecs.components.HitResolutionComponent;

public class HitResolutionSystem extends GameSystem
{
    public HitResolutionSystem(Engine engine)
    {
        super(engine);

        registerComponentType(HitResolutionComponent.class);
    }

    public void updateEntity(Entity entity, float dt)
    {
        HitResolutionComponent resolution = entity.getComponent(HitResolutionComponent.class);

        Entity attacker = resolution.attacker;
        Entity attacked = resolution.attacked;


        engine.destroyEntity(entity);
    }
}
