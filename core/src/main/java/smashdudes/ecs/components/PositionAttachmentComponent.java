package smashdudes.ecs.components;

import com.badlogic.gdx.math.Vector2;
import smashdudes.ecs.Component;
import smashdudes.ecs.Entity;

public class PositionAttachmentComponent extends Component
{
    public final Entity parent;
    public final Vector2 offset;

    public PositionAttachmentComponent(Entity parent, Vector2 offset)
    {
        this.parent = parent;
        this.offset = new Vector2(offset);
    }
}
