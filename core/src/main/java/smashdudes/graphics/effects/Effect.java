package smashdudes.graphics.effects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pools;

import java.util.stream.StreamSupport;

public class Effect
{
    private final Array<ParticleEmitter> emitters = new Array<>();

    public Effect(Vector2 position, Array<ParticleEmitterConfig> configs)
    {
        for(ParticleEmitterConfig config : configs)
        {
            config.effectPosition = position;
            emitters.add(new ParticleEmitter(config, Pools.get(Particle.class)));
        }
    }

    public void update(float dt)
    {
        StreamSupport.stream(emitters.spliterator(), true).forEach(e ->  e.update(dt));
    }

    public void render(SpriteBatch sb)
    {
        for(ParticleEmitter emitter : emitters)
        {
            emitter.render(sb);
        }
    }
}
