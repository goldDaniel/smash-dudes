package smashdudes.ecs.events;

import smashdudes.ecs.Entity;

public class StateEvent extends Event
{
    public final Event event;

    public StateEvent(Event event)
    {
        super(event.entity);
        this.event = event;
    }

    @Override
    public boolean isImmediate()
    {
        return event.isImmediate();
    }
}
