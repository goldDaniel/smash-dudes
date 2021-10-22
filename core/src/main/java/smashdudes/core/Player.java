package smashdudes.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import smashdudes.core.playerstate.*;

public class Player implements ICollision
{
    Rectangle collisionRectangle = new Rectangle();

    public Vector2 position = new Vector2();
    public float width = 2;
    public float height = 2;

    public Vector2 velocity = new Vector2();

    public InputConfig inputConfig;

    public float speed = 8;

    private PlayerState currState = new InAirState(this);

    public Player(InputConfig inputConfig)
    {
        this.inputConfig = inputConfig;
    }

    void update(float dt)
    {
        currState.update(dt);
    }

    void draw(ShapeRenderer sh)
    {
        sh.setColor(Color.RED);
        sh.rect(position.x - width / 2, position.y - height / 2, 2, 2);
    }

    @Override
    public Rectangle getCollisionRect()
    {
        collisionRectangle.set(position.x - width / 2, position.y - height / 2, 2, 2);
        return collisionRectangle;
    }

    @Override
    public void resolve(Side side, ICollision collidedWith)
    {
        currState.resolve(side, collidedWith);
    }

    public void setNextState(PlayerState nextState)
    {
        currState = nextState;
    }
}
