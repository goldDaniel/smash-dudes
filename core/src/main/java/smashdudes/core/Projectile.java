package smashdudes.core;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

public class Projectile
{
    public Vector2 speed;
    public Vector2 dim;
    public Vector2 pos;

    public float knockback;
    public float damage;
    public float lifeTime;

    public Texture texture;

    public Projectile(Vector2 speed, Vector2 dim, Vector2 pos, float knockback, float damage, float lifeTime, Texture texture)
    {
        this.speed = speed.cpy();
        this.dim = dim.cpy();
        this.pos = pos.cpy();

        this.knockback = knockback;
        this.damage = damage;
        this.lifeTime = lifeTime;

        this.texture = texture;
    }

    public Projectile()
    {
        this(new Vector2(), new Vector2(), new Vector2(), 0, 0, 0, null);
    }

    public Projectile copy()
    {
        return new Projectile(speed, dim, pos, knockback, damage, lifeTime, texture);
    }
}
