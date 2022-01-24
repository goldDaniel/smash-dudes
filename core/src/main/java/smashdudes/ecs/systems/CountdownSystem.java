package smashdudes.ecs.systems;

import com.badlogic.gdx.utils.Array;
import smashdudes.ecs.Engine;
import smashdudes.ecs.Entity;
import smashdudes.ecs.components.CountdownComponent;
import smashdudes.ecs.events.CountdownEvent;

public class CountdownSystem extends GameSystem
{
    private Array<Boolean> eventsHaveFired = new Array<>();

    public CountdownSystem(Engine engine)
    {
        super(engine);
        registerComponentType(CountdownComponent.class);

        for(int i = 0; i < 4; i++)
        {
            eventsHaveFired.add(false);
        }
    }

    @Override
    public void updateEntity(Entity entity, float dt)
    {
        CountdownComponent cc = entity.getComponent(CountdownComponent.class);

        cc.currDuration -= dt;

        if (cc.currDuration <= 0)
        {
            engine.enableSystem(PlayerControllerSystem.class);
            engine.destroyEntity(entity);
        }

        if(cc.currDuration > 2)
        {
            engine.addEvent(new CountdownEvent(entity, 3));
        }
        else if(cc.currDuration > 1)
        {
            engine.addEvent(new CountdownEvent(entity, 2));
        }
        else if(cc.currDuration > 0)
        {
            engine.addEvent(new CountdownEvent(entity, 1));
        }
        else
        {
            engine.addEvent(new CountdownEvent(entity, 0));
        }
    }
}
