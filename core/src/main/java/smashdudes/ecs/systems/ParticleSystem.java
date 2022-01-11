package smashdudes.ecs.systems;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import smashdudes.core.WorldUtils;
import smashdudes.ecs.Engine;
import smashdudes.ecs.Entity;
import smashdudes.ecs.components.DrawComponent;
import smashdudes.ecs.components.ParticleComponent;
import smashdudes.ecs.components.ParticleEmitterComponent;
import smashdudes.ecs.components.PositionComponent;
import smashdudes.ecs.events.AttackEvent;
import smashdudes.ecs.events.Event;
import smashdudes.ecs.events.LandingEvent;

public class ParticleSystem extends GameSystem
{
    private WorldUtils utils;

    public ParticleSystem(Engine engine, WorldUtils utils)
    {
        super(engine);
        this.utils = utils;

        registerComponentType(PositionComponent.class);
        registerComponentType(ParticleComponent.class);
        registerComponentType(DrawComponent.class);

        registerEventType(LandingEvent.class);
        registerEventType(AttackEvent.class);
    }

    @Override
    protected void preUpdate()
    {
        
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
            LandingEvent e = (LandingEvent)event;

            Entity emitter = engine.createEntity();

            ParticleEmitterComponent comp = new ParticleEmitterComponent();

            comp.emissionPoint = e.landingPoint;

            comp.lifetime = 0.05f;
            comp.emissionRate = 2048;

            comp.startColor = new Color(0.2f, 0.2f, 0.2f, 1);
            comp.endColor = new Color(0.8f, 0.8f, 0.8f, 0);

            comp.lifespanStartRange = 0.2f;
            comp.lifespanEndRange = 0.4f;

            comp.sizeStartRange = new Vector2(0.1f, 0.2f);
            comp.sizeEndRange = new Vector2(0.0f, 0.1f);

            comp.velocityMin = new Vector2(-5.f, 0.1f);
            comp.velocityMax = new Vector2(5.f, 3.f);

            comp.zIndex = 20;

            emitter.addComponent(comp);
        }

        if(event instanceof AttackEvent)
        {
            AttackEvent e = (AttackEvent)event;

            Entity emitter = engine.createEntity();
            ParticleEmitterComponent comp = new ParticleEmitterComponent();

            comp.emissionPoint = e.collisionArea.getCenter(new Vector2());

            comp.lifetime = 0.05f;
            comp.emissionRate = 512;

            comp.startColor = Color.RED.cpy();
            comp.endColor = Color.ORANGE.cpy();
            comp.endColor.a = 0.0f;

            comp.lifespanStartRange = 0.1f;
            comp.lifespanEndRange = 0.3f;

            comp.sizeStartRange = new Vector2(0.2f, 0.4f);
            comp.sizeEndRange = new Vector2(0.0f, 0.2f);

            comp.velocityMin = new Vector2(2.f, -8f);
            comp.velocityMax = new Vector2(8.f,   8f);

            comp.zIndex = 50;

            emitter.addComponent(comp);
        }
    }
}
