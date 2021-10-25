package smashdudes.core.movementstate;

import smashdudes.core.ICollision;
import smashdudes.core.Player;

public abstract class MovementState
{
    protected Player player;

    public MovementState(Player player)
    {
        this.player = player;
    }

    public abstract void update(float dt);

    public abstract void resolve(ICollision.Side side, ICollision collidedWith);
}
