package smashdudes.ecs.events;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import smashdudes.ecs.Entity;


public class AttackEvent extends Event
{
    public final Entity attacked;

    public final Vector2 launchVector;
    public final Rectangle collisionArea;

    public AttackEvent(Entity entity, Entity attacked, Vector2 launchVector, Rectangle collisionArea)
    {
        super(entity);
        this.attacked = attacked;
        this.launchVector = new Vector2(launchVector);
        this.collisionArea = new Rectangle(collisionArea);
    }
}
