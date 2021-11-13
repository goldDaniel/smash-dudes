package smashdudes.ecs.events;

import com.badlogic.gdx.math.Vector2;
import smashdudes.ecs.Entity;

public class HitEvent extends Event
{
    public final Entity other;
    public final Vector2 attackLine;

    public HitEvent(Entity entity, Entity other, Vector2 attackLine)
    {
        super(entity);

        this.other = other;
        this.attackLine = attackLine;
    }
}
