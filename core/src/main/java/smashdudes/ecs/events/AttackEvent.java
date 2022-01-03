package smashdudes.ecs.events;

import smashdudes.ecs.Entity;

public class AttackEvent extends Event
{
    public final Entity attacked;

    public AttackEvent(Entity entity, Entity attacked)
    {
        super(entity);
        this.attacked = attacked;
    }
}
