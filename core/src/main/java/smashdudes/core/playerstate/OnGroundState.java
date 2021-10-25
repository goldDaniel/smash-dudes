package smashdudes.core.playerstate;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;
import smashdudes.core.ICollision;
import smashdudes.core.Player;

public class OnGroundState extends PlayerState
{
    protected boolean onGround = false;

    public OnGroundState(Player player)
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

        if (!onGround)
        {
            player.setNextState(new InAirState(player));
        }
        else if (Gdx.input.isKeyPressed(player.inputConfig.up))
        {
            player.velocity.y = player.ySpeed;
            player.setNextState(new InAirState(player));
        }
        else if (Gdx.input.isKeyPressed(player.inputConfig.down))
        {
            player.setNextState(new CrouchingState(player));
        }

        onGround = false;

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
            onGround = true;
        }
    }
}
