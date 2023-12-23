package smashdudes.ecs.systems;

import com.badlogic.gdx.math.Vector2;
import smashdudes.ecs.Engine;
import smashdudes.ecs.Entity;
import smashdudes.ecs.components.PositionAttachmentComponent;
import smashdudes.ecs.components.PositionComponent;

public class PositionAttachmentSystem extends GameSystem
{
    public PositionAttachmentSystem(Engine engine)
    {
        super(engine);

        registerComponentType(PositionAttachmentComponent.class);
        registerComponentType(PositionComponent.class);
    }

    protected void updateEntity(Entity entity, float dt)
    {
        PositionComponent pos = entity.getComponent(PositionComponent.class);
        PositionAttachmentComponent attachment = entity.getComponent(PositionAttachmentComponent.class);

        Vector2 relative = attachment.parent.getComponent(PositionComponent.class).position;
        pos.position.set(relative).add(attachment.offset);
    }
}
