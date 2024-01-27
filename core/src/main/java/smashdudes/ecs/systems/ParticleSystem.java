package smashdudes.ecs.systems;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import smashdudes.core.ArrayUtils;
import smashdudes.ecs.Engine;
import smashdudes.ecs.Entity;
import smashdudes.ecs.components.*;
import smashdudes.ecs.events.AttackEvent;
import smashdudes.ecs.events.Event;
import smashdudes.ecs.events.LandingEvent;
import smashdudes.graphics.effects.Effect;
import smashdudes.graphics.effects.ParticleEmitterConfig;

public class ParticleSystem extends GameSystem
{
    public ParticleSystem(Engine engine)
    {
        super(engine);

        registerComponentType(PositionComponent.class);
        registerComponentType(ParticleComponent.class);
        registerComponentType(DrawComponent.class);

        registerEventType(LandingEvent.class);
    }

    @Override
    protected void updateEntity(Entity entity, float dt)
    {
        PositionComponent pos = entity.getComponent(PositionComponent.class);
        ParticleComponent particle = entity.getComponent(ParticleComponent.class);
        DrawComponent draw = entity.getComponent(DrawComponent.class);

        particle.update(dt);
        if(!particle.isAlive())
        {
            engine.destroyEntity(entity);
            return;
        }

        pos.position.add(particle.getVelocity().cpy().scl(dt));

        draw.getColor().set(particle.getColor());
        draw.scale.set(particle.getSize(),particle.getSize());
    }

    @Override
    protected void handleEvent(Event event)
    {
    }
}
