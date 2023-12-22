package smashdudes.ecs.systems;

import com.badlogic.gdx.math.Vector2;
import smashdudes.ecs.Engine;
import smashdudes.ecs.Entity;
import smashdudes.ecs.components.*;
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

        boolean facingLeft = res.attacker.getComponent(PlayerComponent.class).facingLeft;

        engine.addEvent(new StunnedEvent(res.attacked, 0.5f));

        if(res.attacked.hasComponent(VelocityComponent.class))
        {
            VelocityComponent v = res.attacked.getComponent(VelocityComponent.class);
            v.velocity.set(new Vector2((facingLeft ? -1 : 1), 1).nor().scl(20)); // res.knockback
        }

        if(res.attacked.hasComponent(HealthComponent.class))
        {
            HealthComponent health = res.attacked.getComponent(HealthComponent.class);
            health.health += res.damage;
        }

        engine.destroyEntity(entity);
    }
}
