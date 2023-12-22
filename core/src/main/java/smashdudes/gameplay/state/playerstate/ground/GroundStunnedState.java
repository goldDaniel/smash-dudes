package smashdudes.gameplay.state.playerstate.ground;

import smashdudes.ecs.Entity;
import smashdudes.gameplay.state.State;

public class GroundStunnedState extends PlayerGroundState
{
    private float stunTimer;
    public GroundStunnedState(Entity entity, float stunTimer)
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
            return new GroundIdleState(entity);
        }

        return this;
    }
}
