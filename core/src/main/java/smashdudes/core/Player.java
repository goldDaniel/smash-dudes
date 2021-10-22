package smashdudes.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;


public class Player implements ICollision
{
    Rectangle collisionRectangle = new Rectangle();

    Vector2 position = new Vector2();
    float width = 2;
    float height = 2;

    float speed = 4;

    void update(float dt)
    {
        Vector2 velocity = new Vector2();
        if (Gdx.input.isKeyPressed(Input.Keys.A))
        {
            velocity.x--;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D))
        {
            velocity.x++;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.W))
        {
            velocity.y++;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S))
        {
            velocity.y--;
        }
        velocity.nor().scl(speed * dt);
        position.add(velocity);
    }

    void draw(ShapeRenderer sh)
    {
        sh.setColor(Color.RED);
        sh.rect(position.x - width / 2, position.y - height / 2, 2, 2);
    }

    public Rectangle getCollisionRect()
    {
        collisionRectangle.set(position.x - width / 2, position.y - height / 2, 2, 2);
        return collisionRectangle;
    }

    @Override
    public void resolve(Side side, ICollision collidedWith)
    {
        Rectangle r = collidedWith.getCollisionRect();

        if (side == Side.Left)
        {
            position.x = r.x - width / 2;
        }
        else if (side == Side.Right)
        {
            position.x = r.x + r.width + width / 2;
        }

        if (side == Side.Top)
        {
            position.y = r.y + r.height + height / 2;
        }
        else if (side == Side.Bottom)
        {
            position.y = r.y - height / 2;
        }
    }
}
