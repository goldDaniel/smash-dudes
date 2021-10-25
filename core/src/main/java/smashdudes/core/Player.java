package smashdudes.core;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import smashdudes.core.movementstate.*;

public class Player implements ICollision
{
    Rectangle collisionRectangle = new Rectangle();

    public Vector2 position = new Vector2();
    public float width = 2;
    public float height = 2;

    public Vector2 velocity = new Vector2();

    public InputConfig inputConfig;
    public Color colour;

    public float ySpeed = 11;
    public float speed = 8;

    private MovementState currState = new InAirState(this);

    public Player(InputConfig inputConfig, Color colour)
    {
        this.inputConfig = inputConfig;
        this.colour = colour;
    }

    void update(float dt)
    {
        currState.update(dt);
    }

    void draw(ShapeRenderer sh)
    {
        sh.setColor(colour);
        sh.rect(position.x - width / 2, position.y - height / 2, width, height);
    }

    @Override
    public Rectangle getCollisionRect()
    {
        collisionRectangle.set(position.x - width / 2, position.y - height / 2, width, height);
        return collisionRectangle;
    }

    @Override
    public void resolve(Side side, ICollision collidedWith)
    {
        currState.resolve(side, collidedWith);
    }

    public void setNextState(MovementState nextState)
    {
        currState = nextState;
    }
}
