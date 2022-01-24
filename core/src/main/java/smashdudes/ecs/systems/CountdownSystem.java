package smashdudes.ecs.systems;

import smashdudes.ecs.Engine;
import smashdudes.ecs.Entity;
import smashdudes.ecs.components.CountdownComponent;

public class CountdownSystem extends GameSystem
{
    public CountdownSystem(Engine engine)
    {
        super(engine);
        registerComponentType(CountdownComponent.class);
    }

    @Override
    public void updateEntity(Entity entity, float dt)
    {
        CountdownComponent cc = entity.getComponent(CountdownComponent.class);

        if(cc.currDuration <= 0)
        {
            engine.enableSystem(PlayerControllerSystem.class);
            engine.destroyEntity(entity);
            return;
        }

        cc.currDuration -= dt;
    }
}
