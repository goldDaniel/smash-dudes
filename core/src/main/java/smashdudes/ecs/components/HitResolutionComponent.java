package smashdudes.ecs.components;

import com.badlogic.gdx.math.Vector2;
import smashdudes.ecs.Component;
import smashdudes.ecs.Entity;

public class HitResolutionComponent extends Component
{
    public final Entity attacker;
    public final Entity attacked;

    public final float stunTime;

    public final Vector2 launchDirection;

    public HitResolutionComponent(Entity attacker, Entity attacked, Vector2 launchDirection, float stunTime)
    {
        this.attacker = attacker;
        this.attacked = attacked;
        this.launchDirection = launchDirection;
        this.stunTime = stunTime;
    }
}