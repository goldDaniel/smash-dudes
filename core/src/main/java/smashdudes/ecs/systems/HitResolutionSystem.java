package smashdudes.ecs.systems;

import smashdudes.ecs.Engine;
import smashdudes.ecs.Entity;
import smashdudes.ecs.components.HealthComponent;
import smashdudes.ecs.components.HitResolutionComponent;
import smashdudes.ecs.components.VelocityComponent;
import smashdudes.ecs.events.StunnedEvent;

public class HitResolutionSystem extends GameSystem
{

    public HitResolutionSystem(Engine engine)
    {
        super(engine);

        registerComponentType(HitResolutionComponent.class);
    }

    @Override
    public void updateEntity(Entity entity, float dt)
    {
        HitResolutionComponent res = entity.getComponent(HitResolutionComponent.class);

        engine.addEvent(new StunnedEvent(res.attacked, 0.5f));

        if(res.attacked.hasComponent(VelocityComponent.class))
        {
            VelocityComponent v = res.attacked.getComponent(VelocityComponent.class);


            v.velocity.set(res.launchVector);
        }

        if(res.attacked.hasComponent(HealthComponent.class))
        {
            HealthComponent health = res.attacked.getComponent(HealthComponent.class);
            health.health += res.damage;
        }

        engine.destroyEntity(entity);
    }
}
