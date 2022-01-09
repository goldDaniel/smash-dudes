package smashdudes.ecs.systems;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import smashdudes.core.WorldUtils;
import smashdudes.ecs.Engine;
import smashdudes.ecs.Entity;
import smashdudes.ecs.components.DrawComponent;
import smashdudes.ecs.components.ParticleComponent;
import smashdudes.ecs.components.ParticleEmitterComponent;
import smashdudes.ecs.components.PositionComponent;

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
}
