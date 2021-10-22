package smashdudes.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;


public class Player
{
    Rectangle collisionRectangle = new Rectangle();

    Vector2 position = new Vector2();

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
        sh.rect(position.x, position.y, 2, 2);
    }

    public Rectangle getCollisionRect()
    {
        collisionRectangle.set(position.x, position.y, 2, 2);
        return collisionRectangle;
    }
}
