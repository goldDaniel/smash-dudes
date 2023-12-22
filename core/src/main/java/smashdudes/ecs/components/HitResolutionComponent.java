package smashdudes.ecs.components;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import smashdudes.ecs.Component;
import smashdudes.ecs.Entity;

public class HitResolutionComponent extends Component
{
    public final Entity attacker;
    public final Entity attacked;

    public final Rectangle collisionArea;
    public final Vector2 launchVector;

    public final float stunTime;
    public final float damage;
    public final float knockback;

    public HitResolutionComponent(Entity attacker, Entity attacked, Vector2 launchVector, Rectangle collisionArea, float stunTime, float damage, float knockback)
    {
        this.attacker = attacker;
        this.attacked = attacked;

        this.collisionArea = collisionArea;
        this.launchVector = launchVector;

        this.stunTime = stunTime;
        this.damage = damage;
        this.knockback = knockback;
    }
}
