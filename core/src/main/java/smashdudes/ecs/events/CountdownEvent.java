package smashdudes.ecs.events;

import smashdudes.ecs.Entity;

public class CountdownEvent extends Event
{
    public final int currTime;

    public CountdownEvent(Entity entity, int currTime)
    {
        super(entity);
        this.currTime = currTime;
    }
}
