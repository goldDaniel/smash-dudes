package smashdudes.core.state;

import smashdudes.ecs.Entity;

public abstract class State
{
    protected final Entity entity;
    private boolean onFirstRun = true;

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

    public abstract State getNextState();
}
