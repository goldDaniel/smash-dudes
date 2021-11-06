package smashdudes.ecs.systems;

import smashdudes.ecs.Engine;
import smashdudes.ecs.Entity;
import smashdudes.ecs.components.AnimationComponent;
import smashdudes.ecs.components.SpriteDrawComponent;

public class AnimationSystem extends GameSystem
{
    public AnimationSystem(Engine engine)
    {
        super(engine);
        registerComponentType(AnimationComponent.class);
        registerComponentType(SpriteDrawComponent.class);
    }

    @Override
    protected void updateEntity(Entity entity, float dt)
    {
        AnimationComponent anim = entity.getComponent(AnimationComponent.class);
        SpriteDrawComponent draw = entity.getComponent(SpriteDrawComponent.class);

        anim.currentTime += dt;

        draw.sprite = anim.currentAnimation.getKeyFrame(anim.currentTime).texture;
    }
}
