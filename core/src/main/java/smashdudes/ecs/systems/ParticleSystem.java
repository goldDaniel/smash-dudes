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
        if(Gdx.input.isButtonJustPressed(Input.Buttons.LEFT))
        {

            Entity emitter = engine.createEntity();
            ParticleEmitterComponent comp = new ParticleEmitterComponent();
            comp.lifetime = 0.1f;

            comp.emissionRate = 2048;
            comp.emissionPoint = utils.getWorldFromScreen(new Vector2(Gdx.input.getX(), Gdx.input.getY()));

            Color startColor =Color.GREEN.cpy();
            comp.colors.add(startColor);

            Color red = Color.YELLOW.cpy().add(-0.2f, -0.2f, -0.2f, 0);
            red.a = 0.8f;
            comp.colors.add(red);

            Color end = Color.WHITE.cpy();
            end.a = 0.5f;
            comp.colors.add(end);

            comp.lifespanStartRange = 0.1f;
            comp.lifespanEndRange = 0.3f;

            comp.sizeStartRange = new Vector2(0.2f, 0.4f);
            comp.sizeEndRange = new Vector2(0.0f, 0.1f);

            comp.velocityMin = new Vector2(-1f, -1).nor().scl(15);
            comp.velocityMax = new Vector2( 1, 1).nor().scl(15);
            comp.zIndex = 25;
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
        if(!particle.isAlive())
        {
            engine.destroyEntity(entity);
            return;
        }

        pos.position.add(particle.getVelocity().cpy().scl(dt));

        draw.getColor().set(particle.getColor());
        draw.scale = particle.getSize();
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
            comp.emissionRate = 1024;

            comp.colors.add(new Color(0.2f, 0.2f, 0.2f, 1));
            comp.colors.add(new Color(0.8f, 0.8f, 0.8f, 0));

            comp.lifespanStartRange = 0.2f;
            comp.lifespanEndRange = 0.4f;

            comp.sizeStartRange = new Vector2(0.2f, 0.3f);
            comp.sizeEndRange = new Vector2(0.0f, 0.2f);

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

            comp.colors.add(Color.RED.cpy());
            Color end = Color.ORANGE.cpy();
            end.a = 0;
            comp.colors.add(end);

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
