package smashdudes.core;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class AttackResult
{
    public Vector2 direction = new Vector2();
    public Rectangle collisionArea = new Rectangle();

    public float stunTime = 0;
}