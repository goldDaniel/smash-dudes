package smashdudes.core;

import com.badlogic.gdx.math.Rectangle;

public class Collisions
{
    public enum CollisionSide
    {
        Left,
        Right,
        Top,
        Bottom,
    }

    /**
     * Returns side Rectangle is colliding with relative to r0.
     */
    public static CollisionSide getCollisionSide(Rectangle r0, Rectangle r1)
    {
        CollisionSide result = null;
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
                result = CollisionSide.Left;
            }
            else
            {
                result = CollisionSide.Right;
            }
        }
        else if (above)
        {
            result = CollisionSide.Top;
        }
        else
        {
            result = CollisionSide.Bottom;
        }

        return result;
    }
}