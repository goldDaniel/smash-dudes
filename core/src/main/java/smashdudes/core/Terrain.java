package smashdudes.core;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;

public class Terrain extends Entity implements ICollision
{
    private Rectangle rect;

    public Terrain(float x, float y, float w, float h)
    {
        position.x = x;
        position.y = y;
        rect = new Rectangle(x - w / 2, y - h / 2, w, h);
    }

    @Override
    public Entity getEntity()
    {
        return this;
    }

    public Rectangle getCollisionRect()
    {
        rect.x = position.x - rect.width / 2;
        rect.y = position.y - rect.height / 2;
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
        sh.setColor(Color.BLUE);
    }
}
