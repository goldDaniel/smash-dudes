package smashdudes.gameplay.state;

import smashdudes.ecs.Entity;
import smashdudes.ecs.events.Event;

public abstract class State
{
    public interface FireEvent
    {
        void execute(Event event);
    }
    protected static FireEvent fireEvent;

    protected final Entity entity;
    private boolean onFirstRun = true;
    public State(Entity entity)
    {
        this.entity = entity;
    }

    public static final void setFireEventCallback(FireEvent fire)
    {
        fireEvent = fire;
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

    public State handleEvent(Event event)
    {
        return this;
    }
}
