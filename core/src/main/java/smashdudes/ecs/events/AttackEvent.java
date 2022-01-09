package smashdudes.ecs.events;

import com.badlogic.gdx.math.Rectangle;
import smashdudes.ecs.Entity;


public class AttackEvent extends Event
{
    public final Entity attacked;

    public final Rectangle collisionArea;

    public AttackEvent(Entity entity, Entity attacked, Rectangle collisionArea)
    {
        super(entity);
        this.attacked = attacked;
        this.collisionArea = new Rectangle(collisionArea);
    }
}
