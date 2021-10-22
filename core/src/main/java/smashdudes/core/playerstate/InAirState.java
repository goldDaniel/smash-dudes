package smashdudes.core.playerstate;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;
import smashdudes.core.ICollision;
import smashdudes.core.Player;

public class InAirState extends PlayerState
{
    private boolean hasLanded = false;

    public InAirState(Player player)
    {
        super(player);
    }

    @Override
    public void update(float dt)
    {
        player.velocity.x = 0;
        if (Gdx.input.isKeyPressed(player.inputConfig.left))
        {
            player.velocity.x--;
        }
        if (Gdx.input.isKeyPressed(player.inputConfig.right))
        {
            player.velocity.x++;
        }
        player.velocity.x *= player.speed;

        player.velocity.y -= 10 * dt;
        if (hasLanded)
        {
            player.setNextState(new OnGroundState(player));
        }

        player.position.add(player.velocity.x * dt, player.velocity.y * dt);
    }

    @Override
    public void resolve(ICollision.Side side, ICollision collidedWith)
    {
        Rectangle r = collidedWith.getCollisionRect();

        if (side == ICollision.Side.Left)
        {
            player.position.x = r.x - player.width / 2;
        }
        else if (side == ICollision.Side.Right)
        {
            player.position.x = r.x + r.width + player.width / 2;
        }

        if (side == ICollision.Side.Top)
        {
            player.position.y = r.y + r.height + player.height / 2;
            hasLanded = true;
        }
        else if (side == ICollision.Side.Bottom)
        {
            player.position.y = r.y - player.height / 2;
            player.velocity.y = 0;
        }
    }
}
