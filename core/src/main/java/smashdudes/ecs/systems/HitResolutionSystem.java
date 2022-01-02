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
            res.attacked.addComponent(new PlayerStunnedComponent(0.5f));

            if(res.attacked.hasComponent(VelocityComponent.class))
            {
                VelocityComponent v = res.attacked.getComponent(VelocityComponent.class);

                v.velocity.y = 32;
            }
        }

        engine.destroyEntity(entity);
    }

}
