package smashdudes.gameplay;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class AttackBox extends CombatBox
{
    public float angle;
    public float power;

    public AttackBox()
    {
        this(new Rectangle(), new Vector2());
    }

    public AttackBox(Rectangle rect, Vector2 knockback)
    {
        super(rect);
        angle = knockback.angleRad();
        power = knockback.len();
    }

    public AttackBox(Rectangle rect, float angleRad, float magnitude)
    {
        super(rect);
        this.angle = angleRad;
        this.power = magnitude;
    }

    @Override
    public CombatBox clone()
    {
        return new AttackBox(this, angle, power);
    }

    public Vector2 getLaunchVector(boolean facingLeft)
    {
        Vector2 vector = new Vector2();
        vector.x = MathUtils.cos(angle);
        vector.y = MathUtils.sin(angle);
        vector.scl(power);
        if(facingLeft)
        {
            vector.x *= -1;
        }

        return vector;
    }
}
