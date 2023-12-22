package smashdudes.ecs.events;

import smashdudes.ecs.Entity;

public class StunnedEvent extends Event
{
    public final float stunTimer;

    public StunnedEvent(Entity entity, float stunTimer)
    {
        super(entity);
        this.stunTimer = stunTimer;
    }
}
