package smashdudes.core;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;

public class Terrain implements ICollision
{
    private Rectangle rect;

    public Terrain(float x, float y, float w, float h)
    {
        rect = new Rectangle(x, y, w, h);
    }

    public Rectangle getCollisionRect()
    {
        return rect;
    }

    @Override
    public void resolve(Side side, ICollision collidedWith)
    {

    }

    public void draw(ShapeRenderer sh)
    {
        sh.setColor(Color.GREEN);
        sh.rect(rect.x, rect.y, rect.width, rect.height);
    }
}
