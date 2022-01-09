package smashdudes.ecs.systems;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import smashdudes.ecs.Engine;
import smashdudes.ecs.Entity;
import smashdudes.ecs.components.DrawComponent;
import smashdudes.ecs.components.ParticleComponent;
import smashdudes.ecs.components.ParticleEmitterComponent;
import smashdudes.ecs.components.PositionComponent;
import smashdudes.graphics.RenderResources;

public class ParticleEmitterSystem extends GameSystem
{
    public ParticleEmitterSystem(Engine engine)
    {
        super(engine);

        registerComponentType(ParticleEmitterComponent.class);
    }

    @Override
    protected void updateEntity(Entity entity, float dt)
    {
        ParticleEmitterComponent emitter = entity.getComponent(ParticleEmitterComponent.class);

        emitter.update(dt);

        while(emitter.canSpawn())
        {
            Entity particle = engine.createEntity();

            Color startColor = emitter.startColor;
            Color endColor = emitter.endColor;
            float startSize = MathUtils.random(emitter.sizeStartRange.x, emitter.sizeStartRange.y);
            float endSize = MathUtils.random(emitter.sizeEndRange.x, emitter.sizeEndRange.y);
            Vector2 vel = new Vector2(MathUtils.random(emitter.velocityMin.x, emitter.velocityMax.x),
                                      MathUtils.random(emitter.velocityMin.y, emitter.velocityMax.y));
            float lifespan = MathUtils.random(emitter.lifespanStartRange, emitter.lifespanEndRange);


            PositionComponent position = new PositionComponent(emitter.emissionPoint);
            if(emitter.position != null)
            {
                position.position.add(emitter.position.position);
            }
            ParticleComponent par = new ParticleComponent(lifespan, startColor, endColor, vel, startSize, endSize);
            DrawComponent draw = new DrawComponent();
            draw.texture = RenderResources.getTexture("textures/circle.png");
            draw.zIndex = 5;

            particle.addComponent(position);
            particle.addComponent(par);
            particle.addComponent(draw);
        }

        if(!emitter.isAlive())
        {
            engine.destroyEntity(entity);
        }
    }
}
