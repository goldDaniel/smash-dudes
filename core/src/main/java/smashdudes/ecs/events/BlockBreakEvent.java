package smashdudes.ecs.events;

import smashdudes.ecs.Entity;

public class BlockBreakEvent extends Event
{
    public BlockBreakEvent(Entity entity)
    {
        super(entity);
    }
}
