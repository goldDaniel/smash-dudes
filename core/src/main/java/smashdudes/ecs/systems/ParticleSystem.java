package smashdudes.ecs.systems;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import smashdudes.core.WorldUtils;
import smashdudes.ecs.Engine;
import smashdudes.ecs.Entity;
import smashdudes.ecs.components.DrawComponent;
import smashdudes.ecs.components.ParticleComponent;
import smashdudes.ecs.components.ParticleEmitterComponent;
import smashdudes.ecs.components.PositionComponent;
import smashdudes.ecs.events.Event;
import smashdudes.ecs.events.LandingEvent;

public class ParticleSystem extends GameSystem
{
    WorldUtils utils;

    public ParticleSystem(Engine engine, WorldUtils utils)
    {
        super(engine);
        this.utils = utils;

        registerComponentType(PositionComponent.class);
        registerComponentType(ParticleComponent.class);
        registerComponentType(DrawComponent.class);

        registerEventType(LandingEvent.class);
    }

    @Override
    protected void preUpdate()
    {
        if(Gdx.input.isButtonJustPressed(Input.Buttons.LEFT))
        {
            Entity emitter = engine.createEntity();

            ParticleEmitterComponent comp = new ParticleEmitterComponent();
            comp.emissionRate = 128;
            comp.emissionPoint = utils.getWorldFromScreen(new Vector2(Gdx.input.getX(), Gdx.input.getY()));

            comp.startColor = new Color(MathUtils.random(),MathUtils.random(),MathUtils.random(), 1);
            comp.endColor = new Color(MathUtils.random(),MathUtils.random(),MathUtils.random(), 0);

            comp.lifespanStartRange = 0.1f;
            comp.lifespanEndRange = 0.6f;

            comp.sizeStartRange = new Vector2(0.2f, 0.4f);
            comp.sizeEndRange = new Vector2(0.4f, 0.6f);

            comp.velocityMin = new Vector2(-8.f, -8.f);
            comp.velocityMax = new Vector2(8.f, 8.f);

            emitter.addComponent(comp);
        }
    }

    @Override
    protected void updateEntity(Entity entity, float dt)
    {
        PositionComponent pos = entity.getComponent(PositionComponent.class);
        ParticleComponent particle = entity.getComponent(ParticleComponent.class);
        DrawComponent draw = entity.getComponent(DrawComponent.class);

        particle.update(dt);

        pos.position.add(particle.getVelocity().cpy().scl(dt));

        draw.getColor().set(particle.getColor());
        draw.scale = particle.getSize();

        if(!particle.isAlive())
        {
            engine.destroyEntity(entity);
        }
    }

    @Override
    protected void handleEvent(Event event)
    {
        if(event instanceof LandingEvent)
        {
            System.out.println("land");
            LandingEvent e = (LandingEvent)event;

            Entity emitter = engine.createEntity();

            ParticleEmitterComponent comp = new ParticleEmitterComponent();
            comp.emissionPoint = e.landingPoint;

            comp.lifetime = 0.2f;
            comp.emissionRate = 128;

            comp.startColor = new Color(0.2f, 0.2f, 0.2f, 1);
            comp.endColor = new Color(0.8f, 0.8f, 0.8f, 0);

            comp.lifespanStartRange = 0.2f;
            comp.lifespanEndRange = 0.4f;

            comp.sizeStartRange = new Vector2(0.4f, 0.6f);
            comp.sizeEndRange = new Vector2(0.0f, 0.4f);

            comp.velocityMin = new Vector2(-5.f, 0.1f);
            comp.velocityMax = new Vector2(5.f, 3.f);

            comp.zIndex = 20;

            emitter.addComponent(comp);
        }
    }
}
