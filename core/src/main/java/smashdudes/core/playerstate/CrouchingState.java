package smashdudes.core.playerstate;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;
import smashdudes.core.ICollision;
import smashdudes.core.Player;

public class CrouchingState extends PlayerState
{
    private boolean onGround = true;

    private float crouchingSpeed = 0.5f;
    private float crouchHeight = 0.5f;

    public CrouchingState(Player player)
    {
        super(player);

        player.height *= crouchHeight;
        player.position.y -= player.height / 2;
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
        player.velocity.x *= crouchingSpeed * player.speed;

        if (!onGround)
        {
            resetPlayer();
            player.setNextState(new InAirState(player));
        }
        else if (Gdx.input.isKeyPressed(player.inputConfig.up))
        {
            resetPlayer();
            player.velocity.y = player.ySpeed;
            player.setNextState(new InAirState(player));
        }
        else if (!Gdx.input.isKeyPressed(player.inputConfig.down))
        {
            resetPlayer();
            player.setNextState(new OnGroundState(player));
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

    private void resetPlayer()
    {
        player.position.y += player.height / 2;
        player.height /= crouchHeight;
    }
}
