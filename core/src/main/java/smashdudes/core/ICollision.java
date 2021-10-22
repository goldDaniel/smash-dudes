package smashdudes.core;

import com.badlogic.gdx.math.Rectangle;

public interface ICollision
{
    enum Side
    {
        Left,
        Right,
        Top,
        Bottom
    }

    default boolean overlaps(ICollision other)
    {
        return other.getCollisionRect().overlaps(this.getCollisionRect());
    }

    Rectangle getCollisionRect();

    void resolve(Side side, ICollision collidedWith);
}
