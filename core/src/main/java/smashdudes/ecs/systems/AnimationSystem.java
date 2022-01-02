package smashdudes.ecs.systems;

import smashdudes.ecs.Engine;
import smashdudes.ecs.Entity;
import smashdudes.ecs.components.AnimationComponent;
import smashdudes.ecs.components.DrawComponent;

public class AnimationSystem extends GameSystem
{
    public AnimationSystem(Engine engine)
    {
        super(engine);
        registerComponentType(AnimationComponent.class);
        registerComponentType(DrawComponent.class);
    }

    @Override
    protected void updateEntity(Entity entity, float dt)
    {
        AnimationComponent anim = entity.getComponent(AnimationComponent.class);
        DrawComponent draw = entity.getComponent(DrawComponent.class);

        anim.update(dt);

        draw.texture = anim.getCurrentFrame().texture;
    }
}
