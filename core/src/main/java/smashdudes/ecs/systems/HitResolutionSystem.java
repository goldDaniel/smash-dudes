package smashdudes.ecs.systems;

import smashdudes.ecs.Engine;
import smashdudes.ecs.Entity;
import smashdudes.ecs.components.*;

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

        if(!res.attacked.hasComponent(PlayerStunnedComponent.class))
        {
            res.attacked.addComponent(new PlayerStunnedComponent(res.stunTime));

            if(res.attacked.hasComponent(VelocityComponent.class))
            {
                VelocityComponent v = res.attacked.getComponent(VelocityComponent.class);

                v.velocity = res.launchDirection.nor().scl(res.knockback);
            }
        }

        if(res.attacked.hasComponent(HealthComponent.class))
        {
            HealthComponent health = res.attacked.getComponent(HealthComponent.class);
            health.health += res.damage;
        }

        engine.destroyEntity(entity);
    }
}
