package smashdudes.gameplay.state.playerstate.air;

import smashdudes.ecs.Entity;
import smashdudes.ecs.events.Event;
import smashdudes.ecs.events.LandingEvent;
import smashdudes.gameplay.state.State;
import smashdudes.gameplay.state.playerstate.ground.GroundStunnedState;

public class AirStunnedState extends PlayerAirState
{
    private float stunTimer;
    public AirStunnedState(Entity entity, float stunTimer)
    {
        super(entity);
        this.stunTimer = stunTimer;
    }

    @Override
    public void innerUpdate(float dt)
    {
        stunTimer -= dt;
    }

    @Override
    public void onExit()
    {

    }

    @Override
    public State getNextState()
    {
        if (stunTimer <= 0)
        {
            return new FallingState(entity);
        }

        return this;
    }

    @Override
    public State handleEvent(Event event)
    {
        State state = super.handleEvent(event);
        if(state == this && event instanceof LandingEvent)
        {
            state = new GroundStunnedState(this.entity, stunTimer);
        }

        return state;
    }
}
