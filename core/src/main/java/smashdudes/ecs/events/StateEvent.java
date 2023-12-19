package smashdudes.ecs.events;

import smashdudes.ecs.Entity;

public class StateEvent extends Event
{
    public StateEvent(Entity entity)
    {
        super(entity);
    }
}
