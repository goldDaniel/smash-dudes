package smashdudes.ecs.systems;

import smashdudes.ecs.Engine;
import smashdudes.ecs.Entity;
import smashdudes.ecs.components.AnimationComponent;
import smashdudes.ecs.components.AnimationDebugComponent;
import smashdudes.ecs.components.DebugDrawComponent;

public class AnimationDebugSystem extends GameSystem
{
    public AnimationDebugSystem(Engine engine)
    {
        super(engine);

        registerComponentType(AnimationComponent.class);
        registerComponentType(AnimationDebugComponent.class);
        registerComponentType(DebugDrawComponent.class);
    }

    @Override
    protected void updateEntity(Entity entity, float dt)
    {
        AnimationComponent anim = entity.getComponent(AnimationComponent.class);
        DebugDrawComponent ddraw = entity.getComponent(DebugDrawComponent.class);

        AnimationComponent.AnimationFrame frame = anim.currentAnimation.getKeyFrame(anim.currentTime);
        ddraw.hurtboxes = frame.hurtboxes;
        ddraw.hitboxes = frame.hitboxes;
    }
}
