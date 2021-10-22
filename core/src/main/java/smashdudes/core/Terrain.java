package smashdudes.core;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;

public class Terrain
{
    private Rectangle rect;

    private boolean hasTouched;

    public Terrain(float x, float y, float w, float h)
    {
        rect = new Rectangle(x, y, w, h);
    }

    public boolean isTouching(Rectangle rect)
    {
        hasTouched = this.rect.overlaps(rect);
        return hasTouched;
    }

    public void draw(ShapeRenderer sh)
    {
        sh.setColor(Color.GREEN);
        if (hasTouched)
        {
            sh.setColor(Color.CYAN);
        }

        sh.rect(rect.x, rect.y, rect.width, rect.height);
    }
}
