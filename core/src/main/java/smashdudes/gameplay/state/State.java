package smashdudes.gameplay.state;

import com.badlogic.gdx.utils.Queue;
import smashdudes.ecs.Entity;
import smashdudes.ecs.events.Event;

public abstract class State
{
    protected final Entity entity;
    private boolean onFirstRun = true;
    private final Queue<Event> eventQueue = new Queue<>();

    public State(Entity entity)
    {
        this.entity = entity;
    }

    public final void update(float dt)
    {
        if(onFirstRun)
        {
            onEnter(dt);
            onFirstRun = false;
        }
        innerUpdate(dt);
    }

    public abstract void onEnter(float dt);

    public abstract void innerUpdate(float dt);

    public abstract void onExit();

    public State getNextState()
    {
        return this;
    }

    protected final void throwEvent(Event event)
    {
        eventQueue.addLast(event);
    }

    public final Event popEvent()
    {
        if(eventQueue.notEmpty())
        {
            return eventQueue.removeFirst();
        }
        else
        {
            return null;
        }
    }

    public State handleEvent(Event event)
    {
        return this;
    }
}
