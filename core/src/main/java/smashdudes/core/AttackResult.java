package smashdudes.core;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class AttackResult
{
    public Vector2 launchVector = new Vector2();
    public Rectangle collisionArea = new Rectangle();

    public float stunTime = 0;

    public float damage = 0;

    public boolean hitBlock = false;
    public float knockback = 0;
}