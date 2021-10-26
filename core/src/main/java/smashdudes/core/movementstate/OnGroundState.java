package smashdudes.core.movementstate;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import smashdudes.core.ICollision;
import smashdudes.core.Player;

public class OnGroundState extends MovementState
{
    private boolean onGround = false;

    private boolean isAttacking = false;

    private boolean attackingLeft = false;
    private float attackSpeed = 15f;
    private float attackDist = 1.75f;
    private float attackX = 0;

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

        player.velocity.y = -0.001f;
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

        if(!isAttacking)
        {
            if(Gdx.input.isKeyJustPressed(Input.Keys.E))
            {
                isAttacking = true;
                attackingLeft = false;
            }
            else if(Gdx.input.isKeyJustPressed(Input.Keys.Q))
            {
                isAttacking = true;
                attackingLeft = true;
            }
        }

        if(isAttacking)
        {
            if(attackingLeft)
            {
                attackX -= attackSpeed * dt;
            }
            else
            {
                attackX += attackSpeed * dt;
            }

            if(Math.abs(attackX) >= attackDist)
            {
                isAttacking = false;
                attackX = 0;
            }
        }
    }

    @Override
    public void draw(ShapeRenderer sh)
    {
        super.draw(sh);

        if(isAttacking)
        {
            float attackSize = 0.75f;
            sh.setColor(Color.WHITE);

            if(attackingLeft)
            {
                sh.rect(player.position.x - attackSize +  attackX, player.position.y + attackSize / 2, attackSize, attackSize);
            }
            else
            {
                sh.rect(player.position.x + attackX, player.position.y + attackSize / 2, attackSize, attackSize);
            }
        }
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
