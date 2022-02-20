package smashdudes.core;

import com.badlogic.gdx.math.Vector2;

public class Projectile
{
    public Vector2 speed;
    public Vector2 dim;
    public Vector2 pos;

    public Projectile(Vector2 speed, Vector2 dim, Vector2 pos)
    {
        this.speed = speed.cpy();
        this.dim = dim.cpy();
        this.pos = pos.cpy();
    }

    public Projectile()
    {
        this(new Vector2(), new Vector2(), new Vector2());
    }

    public Projectile copy()
    {
        return new Projectile(speed, dim, pos);
    }
}
