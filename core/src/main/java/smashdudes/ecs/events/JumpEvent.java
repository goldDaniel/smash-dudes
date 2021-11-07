package smashdudes.ecs.events;

import smashdudes.ecs.Entity;

public class JumpEvent extends Event
{
    public JumpEvent(Entity entity)
    {
        super(entity);
    }
}
