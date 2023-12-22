package smashdudes.gameplay.state.playerstate;

import smashdudes.ecs.Entity;
import smashdudes.ecs.events.Event;
import smashdudes.ecs.events.RespawnAwakeEvent;
import smashdudes.gameplay.state.State;

public class RespawnState extends PlayerState
{
    public RespawnState(Entity entity)
    {
        super(entity);
    }

    @Override
    public void innerUpdate(float dt)
    {

    }

    @Override
    public void onExit()
    {

    }

    @Override
    public State handleEvent(Event event)
    {
        State result = super.handleEvent(event);
        if (result == this && event instanceof RespawnAwakeEvent)
        {
            result = new GroundIdleState(entity);
        }

        return result;
    }
}
