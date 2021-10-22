package smashdudes.core;

import com.badlogic.gdx.math.Rectangle;

public class CollisionResolver
{
    public static void resolve(ICollision c0, ICollision c1)
    {
        if (!c0.overlaps(c1)) return;

        Rectangle r0 = c0.getCollisionRect();
        Rectangle r1 = c1.getCollisionRect();

        ICollision.Side side0 = getCollisionSide(r0, r1);
        ICollision.Side side1 = getCollisionSide(r1, r0);

        c0.resolve(side0, c1);
        c1.resolve(side1, c0);
    }

    /**
     * Returns side Rectangle is colliding with relative to r0.
     */
    private static ICollision.Side getCollisionSide(Rectangle r0, Rectangle r1)
    {
        ICollision.Side result = null;
        //horizontal side
        boolean left = r0.x + r0.width / 2 < r1.x + r1.width / 2;
        //vertical side
        boolean above = r0.y + r0.height / 2 > r1.y + r1.height / 2;

        //holds how deep the r1ect is inside the tile on each axis
        float horizontalDif;
        float verticalDif;

        //determine the differences for depth
        if (left)
        {
            horizontalDif = r0.x + r0.width - r1.x;
        }
        else
        {
            horizontalDif = r1.x + r1.width - r0.x;
        }

        if (above)
        {
            verticalDif = r1.y + r1.height - r0.y;
        }
        else
        {
            verticalDif = r0.y + r0.height - r1.y;
        }

        if (horizontalDif < verticalDif)
        {
            if (left)
            {
                result = ICollision.Side.Left;
            }
            else
            {
                result = ICollision.Side.Right;
            }
        }
        else if (above)
        {
            result = ICollision.Side.Top;
        }
        else
        {
            result = ICollision.Side.Bottom;
        }

        return result;
    }
}
