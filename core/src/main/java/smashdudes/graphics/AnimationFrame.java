package smashdudes.graphics;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class AnimationFrame
{
    public final Texture texture;

    public final Array<Rectangle> attackboxes;
    public final Array<Rectangle> bodyboxes;

    public AnimationFrame(Texture texture, Array<Rectangle> attackboxes, Array<Rectangle> bodyboxes)
    {
        this.texture = texture;
        this.attackboxes = attackboxes;
        this.bodyboxes = bodyboxes;
    }

    public Array<Rectangle> getAttackboxesRelativeTo(Vector2 pos, boolean mirrorX)
    {
        return getRelativeTo(attackboxes, pos, mirrorX);
    }

    public Array<Rectangle> getBodyboxesRelativeTo(Vector2 pos, boolean mirrorX)
    {
        return getRelativeTo(bodyboxes, pos, mirrorX);
    }

    private Array<Rectangle> getRelativeTo(Array<Rectangle> boxes, Vector2 pos, boolean mirrorX)
    {
        Array<Rectangle> result = new Array<>();

        for(Rectangle relative : boxes)
        {
            Rectangle absolute = new Rectangle();
            absolute.width = relative.width;
            absolute.height = relative.height;

            int dir = mirrorX ? -1 : 1;

            absolute.x = pos.x + (dir * relative.x) - relative.width / 2;
            absolute.y = pos.y + relative.y - relative.height / 2;

            result.add(absolute);
        }

        return result;
    }
}
