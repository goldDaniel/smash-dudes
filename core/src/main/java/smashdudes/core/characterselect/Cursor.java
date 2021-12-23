package smashdudes.core.characterselect;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;

public class Cursor
{
    public final Color color;
    public final Circle circle;

    public Cursor(Circle circle, Color color)
    {
        this.color = color;
        this.circle = circle;
    }

    public void updatePosition(Vector2 direction, float dt)
    {
        float speed = 256;
        circle.x += direction.x * speed * dt;
        circle.y += direction.y * speed * dt;
    }
}