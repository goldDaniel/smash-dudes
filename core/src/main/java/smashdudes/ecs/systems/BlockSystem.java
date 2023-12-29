package smashdudes.ecs.systems;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import smashdudes.ecs.Engine;
import smashdudes.ecs.Entity;
import smashdudes.ecs.components.BlockComponent;
import smashdudes.ecs.components.DebugDrawComponent;
import smashdudes.ecs.components.PositionComponent;
import smashdudes.ecs.events.BlockBreakEvent;
import smashdudes.gameplay.GameplayUtils;

public class BlockSystem extends GameSystem
{
    public BlockSystem(Engine engine)
    {
        super(engine);
        registerComponentType(BlockComponent.class);
    }

    @Override
    public void updateEntity(Entity entity, float dt)
    {
        BlockComponent block = entity.getComponent(BlockComponent.class);
        DebugDrawComponent debug = entity.getComponent(DebugDrawComponent.class);
        PositionComponent pos = entity.getComponent(PositionComponent.class);

        Rectangle r = new Rectangle();
        r.x = pos.position.x - block.blockBox.width / 2;
        r.y = pos.position.y - block.blockBox.height / 2;
        r.width = block.blockBox.width;
        r.height = block.blockBox.height;

        block.shieldDuration += block.isEnabled ? -dt : dt;
        block.shieldDuration = Math.max(Math.min(block.shieldDuration, GameplayUtils.MAX_SHIELD_DURATION), 0);

        if(block.isEnabled)
        {
            Color color = new Color(0, 1, 0, 0.5f);
            color.lerp(1, 0, 0, 0.5f, 1 - block.shieldDuration / GameplayUtils.MAX_SHIELD_DURATION);
            debug.pushShape(ShapeRenderer.ShapeType.Filled, r, color);
        }

        if(block.shieldDuration <= 0)
        {
            engine.addEvent(new BlockBreakEvent(entity));
        }
    }
}
