package smashdudes.ecs.systems;

import smashdudes.ecs.Engine;
import smashdudes.ecs.Entity;
import smashdudes.ecs.components.AnimationDebugComponent;
import smashdudes.ecs.components.DebugDrawComponent;

public class AnimationDebugSystem extends GameSystem
{
    public AnimationDebugSystem(Engine engine)
    {
        super(engine);

        registerComponentType(AnimationDebugComponent.class);
        registerComponentType(DebugDrawComponent.class);
    }

    @Override
    protected void updateEntity(Entity entity, float dt)
    {
        AnimationDebugComponent danim = entity.getComponent(AnimationDebugComponent.class);
        DebugDrawComponent ddraw = entity.getComponent(DebugDrawComponent.class);

        danim.currentTime += dt;

        for (int i = 0; i < danim.currentHitboxes.size; i++)
        {
            ddraw.hitboxes.add(danim.currentHitboxes.get(i).getKeyFrame(danim.currentTime));
        }

        for (int i = 0; i < danim.currentHurtboxes.size; i++)
        {
            //ddraw.hurtboxes.add(danim.currentHurtboxes.get(i).getKeyFrame(danim.currentTime));
        }
    }
}
