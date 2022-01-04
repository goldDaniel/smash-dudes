package smashdudes.ecs.systems;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import smashdudes.ecs.Engine;
import smashdudes.ecs.Entity;
import smashdudes.ecs.components.AnimationComponent;
import smashdudes.ecs.components.DebugDrawComponent;
import smashdudes.ecs.components.PlayerComponent;
import smashdudes.ecs.components.PositionComponent;
import smashdudes.graphics.AnimationFrame;

public class AnimationDebugSystem extends GameSystem
{
    public AnimationDebugSystem(Engine engine)
    {
        super(engine);

        registerComponentType(PositionComponent.class);
        registerComponentType(AnimationComponent.class);
        registerComponentType(DebugDrawComponent.class);
    }

    @Override
    protected void updateEntity(Entity entity, float dt)
    {
        PositionComponent pos = entity.getComponent(PositionComponent.class);
        AnimationComponent anim = entity.getComponent(AnimationComponent.class);
        DebugDrawComponent ddraw = entity.getComponent(DebugDrawComponent.class);

        AnimationFrame frame = anim.getCurrentFrame();

        boolean mirror = false;
        if(entity.hasComponent(PlayerComponent.class))
        {
            mirror = entity.getComponent(PlayerComponent.class).facingLeft;
        }

        Array<Rectangle> attackboxes = frame.getAttackboxesRelativeTo(pos.position, mirror);
        for(Rectangle r : attackboxes)
        {
            ddraw.pushShape(ShapeRenderer.ShapeType.Line, r, Color.BLUE);
        }

        Array<Rectangle> bodyboxes = frame.getBodyboxesRelativeTo(pos.position, mirror);
        for(Rectangle r : bodyboxes)
        {
            ddraw.pushShape(ShapeRenderer.ShapeType.Line, r, Color.RED);
        }
    }
}
