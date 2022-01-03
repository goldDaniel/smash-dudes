package smashdudes.graphics;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class AnimationFrame
{
    public final Texture texture;

    public final Array<Rectangle> hitboxes;
    public final Array<Rectangle> hurtboxes;

    public AnimationFrame(Texture texture, Array<Rectangle> hitboxes, Array<Rectangle> hurtboxes)
    {
        this.texture = texture;
        this.hitboxes = hitboxes;
        this.hurtboxes = hurtboxes;
    }

    public Array<Rectangle> getHitboxesRelativeTo(Vector2 pos, boolean mirror)
    {
        return getRelativeTo(hitboxes, pos, mirror);
    }

    public Array<Rectangle> getHurtboxesRelativeTo(Vector2 pos, boolean mirror)
    {
        return getRelativeTo(hurtboxes, pos, mirror);
    }

    private Array<Rectangle> getRelativeTo(Array<Rectangle> boxes, Vector2 pos, boolean mirror)
    {
        Array<Rectangle> result = new Array<>();

        for(Rectangle relative : boxes)
        {
            Rectangle absolute = new Rectangle();
            absolute.width = relative.width;
            absolute.height = relative.height;

            if(mirror)
            {
                absolute.x = pos.x - relative.x - relative.width / 2;
            }
            else
            {
                absolute.x = + pos.x + relative.x - relative.width / 2;
            }

            absolute.y = (relative.y - relative.height / 2) + pos.y;

            result.add(absolute);
        }

        return result;
    }
}
