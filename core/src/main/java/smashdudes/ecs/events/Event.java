package smashdudes.ecs.events;

import smashdudes.ecs.Entity;

public abstract class Event
{
    public final Entity entity;

    private boolean immediate = false;

    public Event(Entity entity)
    {
        this.entity = entity;
    }

    public Event setImmediate()
    {
        this.immediate = true;
        return this;
    }


    public boolean isImmediate()
    {
        return immediate;
    }
}
