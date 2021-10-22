package smashdudes.core.playerstate;

import smashdudes.core.ICollision;
import smashdudes.core.Player;

public abstract class PlayerState
{
    protected Player player;

    public PlayerState(Player player)
    {
        this.player = player;
    }

    public abstract void update(float dt);

    public abstract void resolve(ICollision.Side side, ICollision collidedWith);
}
