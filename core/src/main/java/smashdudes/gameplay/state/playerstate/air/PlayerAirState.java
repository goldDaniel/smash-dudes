package smashdudes.gameplay.state.playerstate.air;

import smashdudes.ecs.Entity;
import smashdudes.gameplay.state.playerstate.PlayerState;

public abstract class PlayerAirState extends PlayerState
{
    public PlayerAirState(Entity entity)
    {
        super(entity);
    }

    @Override
    public boolean onGround()
    {
        return false;
    }
}
