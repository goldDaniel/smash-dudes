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

        for(int i = 0; i < 3; i++)
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
        }

        boolean finishedCountdown = true;
        for(boolean fired : eventsHaveFired)
        {
            if(!fired)
            {
                finishedCountdown = false;
                break;
            }
        }

        if(cc.currDuration > 2 && !eventsHaveFired.get(0))
        {
            engine.addEvent(new CountdownEvent(entity, 3));
            eventsHaveFired.set(0, true);
        }
        else if(cc.currDuration <= 2 && cc.currDuration > 1 && !eventsHaveFired.get(1))
        {
            engine.addEvent(new CountdownEvent(entity, 2));
            eventsHaveFired.set(1, true);

        }
        else if(cc.currDuration <= 1 && cc.currDuration > 0 && !eventsHaveFired.get(2))
        {
            engine.addEvent(new CountdownEvent(entity, 1));
            eventsHaveFired.set(2, true);
        }
        else if (cc.currDuration <= 0 && finishedCountdown)
        {
            engine.addEvent(new CountdownEvent(entity, 0));
        }
    }
}
