package smashdudes.ecs.systems;

import smashdudes.ecs.Engine;
import smashdudes.ecs.Entity;
import smashdudes.ecs.components.*;
import smashdudes.gameplay.PlayerState;

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

        PlayerComponent attackedPlayer = res.attacked.getComponent(PlayerComponent.class);
        if(attackedPlayer.currentState != PlayerState.Ground_Stunned)
        {
            attackedPlayer.currentState = PlayerState.Ground_Stunned;

            if(res.attacked.hasComponent(VelocityComponent.class))
            {
                VelocityComponent v = res.attacked.getComponent(VelocityComponent.class);

                v.velocity.set(res.launchDirection.nor().scl(res.knockback));
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
