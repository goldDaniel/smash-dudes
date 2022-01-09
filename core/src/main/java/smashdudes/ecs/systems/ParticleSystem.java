package smashdudes.ecs.systems;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import smashdudes.ecs.Engine;
import smashdudes.ecs.Entity;
import smashdudes.ecs.components.DrawComponent;
import smashdudes.ecs.components.ParticleComponent;
import smashdudes.ecs.components.PositionComponent;
import smashdudes.graphics.RenderResources;

public class ParticleSystem extends GameSystem
{
    public ParticleSystem(Engine engine)
    {
        super(engine);

        registerComponentType(PositionComponent.class);
        registerComponentType(ParticleComponent.class);
        registerComponentType(DrawComponent.class);
    }

    @Override
    protected void preUpdate()
    {
        if(Gdx.input.isButtonJustPressed(Input.Buttons.LEFT))
        {
            int amount = 1000;
            for(int i = 0; i < amount; i++)
            {
                Entity particle = engine.createEntity();

                Color startColor = new Color(MathUtils.random(),MathUtils.random(),MathUtils.random(), 1);
                Color endColor = new Color(MathUtils.random(),MathUtils.random(),MathUtils.random(), 1);
                float startSize = MathUtils.random(2.0f);
                float endSize = MathUtils.random(startSize);
                Vector2 vel = new Vector2(MathUtils.random(-1.0f, 1.0f), MathUtils.random(-1.0f, 1.0f)).nor().scl(MathUtils.random(5.0f));
                float lifespan = MathUtils.random(0.2f, 1.0f);

                PositionComponent position = new PositionComponent();
                ParticleComponent par = new ParticleComponent(lifespan, startColor, endColor, vel, startSize, endSize);
                DrawComponent draw = new DrawComponent();
                draw.texture = RenderResources.getTexture("textures/circle.png");
                draw.zIndex = 5;

                particle.addComponent(position);
                particle.addComponent(par);
                particle.addComponent(draw);
            }
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
