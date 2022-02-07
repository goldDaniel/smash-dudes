package smashdudes.ecs.events;

import smashdudes.ecs.Entity;

public class WinEvent extends Event
{
    public WinEvent(Entity entity)
    {
        super(entity);
    }
}
