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

                float speed = 16;
                res.launchDirection.y += 0.3f;

                v.velocity = res.launchDirection.nor().scl(speed);
            }
        }

        engine.destroyEntity(entity);
    }

}
