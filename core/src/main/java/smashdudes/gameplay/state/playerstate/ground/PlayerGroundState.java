package smashdudes.gameplay.state.playerstate.ground;

import smashdudes.ecs.Entity;
import smashdudes.gameplay.state.playerstate.PlayerState;

public abstract class PlayerGroundState extends PlayerState
{
    public PlayerGroundState(Entity entity)
    {
        super(entity);
    }

    @Override
    public final boolean onGround()
    {
        return true;
    }
}
