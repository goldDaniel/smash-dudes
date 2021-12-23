package smashdudes.core.characterselect;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Circle;
import smashdudes.core.input.MenuInputRetriever;

public class Cursor
{
    public final Color color;
    public final Circle circle;

    public Cursor(Circle circle, Color color)
    {
        this.color = color;
        this.circle = circle;
    }

    public void updatePosition(MenuInputRetriever r, float dt)
    {
        float speed = 256;
        circle.x += r.getDirection().x * speed * dt;
        circle.y += r.getDirection().y * speed * dt;
    }
}