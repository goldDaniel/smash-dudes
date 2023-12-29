package smashdudes.ecs.systems;

import smashdudes.ecs.Engine;
import smashdudes.ecs.Entity;
import smashdudes.ecs.components.BlockComponent;
import smashdudes.ecs.components.HealthComponent;
import smashdudes.ecs.components.HitResolutionComponent;
import smashdudes.ecs.components.VelocityComponent;
import smashdudes.ecs.events.StunnedEvent;
import smashdudes.gameplay.GameplayUtils;

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

        if(res.attacked.hasComponent(VelocityComponent.class))
        {
            VelocityComponent v = res.attacked.getComponent(VelocityComponent.class);
            v.velocity.set(res.launchVector);
        }

        if(res.attacked.hasComponent(HealthComponent.class))
        {
            HealthComponent health = res.attacked.getComponent(HealthComponent.class);
            if(res.hitBlock)
            {
                BlockComponent block = res.attacked.getComponent(BlockComponent.class);
                block.shieldDuration -= GameplayUtils.DamageToShieldDamage(res.damage);
            }
            else
            {
                health.health += res.damage;
                engine.addEvent(new StunnedEvent(res.attacked, 0.5f));
            }
        }

        engine.destroyEntity(entity);
    }
}
