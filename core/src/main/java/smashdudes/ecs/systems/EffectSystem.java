package smashdudes.ecs.systems;

import smashdudes.ecs.Engine;
import smashdudes.ecs.Entity;
import smashdudes.ecs.components.EffectComponent;
import smashdudes.ecs.components.PositionComponent;

public class EffectSystem extends RenderSystem
{
    public EffectSystem(Engine engine)
    {
        super(engine);
        registerComponentType(EffectComponent.class);
    }

    @Override
    protected void renderEntity(Entity entity, float dt, float alpha)
    {
        EffectComponent e = entity.getComponent(EffectComponent.class);

        e.effect.update(dt);
        e.effect.render();
    }
}
