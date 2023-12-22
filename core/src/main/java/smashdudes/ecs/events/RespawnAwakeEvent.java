package smashdudes.ecs.events;

import smashdudes.ecs.Entity;

public class RespawnAwakeEvent extends Event
{
    public RespawnAwakeEvent(Entity entity)
    {
        super(entity);
    }
}
