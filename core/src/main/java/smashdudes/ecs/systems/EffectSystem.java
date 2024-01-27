package smashdudes.ecs.systems;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.Viewport;
import smashdudes.core.ArrayUtils;
import smashdudes.ecs.Engine;
import smashdudes.ecs.Entity;
import smashdudes.ecs.components.EffectComponent;
import smashdudes.ecs.components.ParticleEmitterComponent;
import smashdudes.ecs.events.AttackEvent;
import smashdudes.ecs.events.Event;
import smashdudes.ecs.events.LandingEvent;
import smashdudes.graphics.effects.Effect;
import smashdudes.graphics.effects.ParticleEmitterConfig;

public class EffectSystem extends DrawSystem
{
    public EffectSystem(Engine engine, Camera camera, Viewport viewport)
    {
        super(engine, camera, viewport);
        registerComponentType(EffectComponent.class);

        // TODO (nathanp): StateEvent 2.0, possibly worth looking into in the future
        registerEventType(LandingEvent.class);
        registerEventType(AttackEvent.class);
    }

    @Override
    protected void preRender()
    {
        super.preRender();
        sb.begin();
    }

    @Override
    protected void renderEntity(Entity entity, float dt, float alpha)
    {
        EffectComponent e = entity.getComponent(EffectComponent.class);

        e.effect.update(dt);
        e.effect.render(sb);
    }

    @Override
    protected void postRender()
    {
        super.postRender();
        sb.end();
    }

    @Override
    protected void handleEvent(Event event)
    {
        if(event instanceof LandingEvent)
        {
            LandingEvent e = (LandingEvent)event;
            Entity effect = engine.createEntity();

            EffectComponent eff = new EffectComponent();

            ParticleEmitterConfig pec = new ParticleEmitterConfig();
            Array<ParticleEmitterConfig> carper = ArrayUtils.toArray(pec);

            eff.effect = new Effect(e.landingPoint, carper);

            effect.addComponent(eff);
        }
    }
}
