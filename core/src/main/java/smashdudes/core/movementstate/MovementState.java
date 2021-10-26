package smashdudes.core.movementstate;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
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

    public void draw(ShapeRenderer sh)
    {
        sh.setColor(player.colour);
        sh.rect(player.position.x - player.width / 2, player.position.y - player.height / 2, player.width, player.height);
    }
}
