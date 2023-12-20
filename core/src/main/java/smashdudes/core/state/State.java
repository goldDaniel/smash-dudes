package smashdudes.core.state;

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

    public void onEnter(float dt)
    {
        innerOnEnter(dt);
        onFirstRun = false;
    }

    public void update(float dt)
    {
        if(onFirstRun)
        {
            onEnter(dt);
        }
        innerUpdate(dt);
    }

    public abstract void innerOnEnter(float dt);

    public abstract void innerUpdate(float dt);

    public abstract void onExit();

    public State getNextState()
    {
        return this;
    }

    protected void throwEvent(Event event)
    {
        eventQueue.addLast(event);
    }

    public Event popEvent()
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
