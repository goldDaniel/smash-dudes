package smashdudes.graphics.effects;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import smashdudes.content.DTO;

public class Effect
{
    private final Pool<Particle> particlePool;
    private final DTO.EffectDescription desc;
    private final Array<ParticleEmitter> emitters;

    public Effect(DTO.EffectDescription desc, Pool<Particle> particlePool)
    {
        this.desc = desc;
        this.particlePool = particlePool;
        this.emitters = new Array<>();

        for(ParticleEmitterConfig config : desc.emitterConfigs)
        {
            config.name = "Emitter " + (emitterCount());
            emitters.add(new ParticleEmitter(config, particlePool));
        }
    }

    public boolean isFinished()
    {
        for(ParticleEmitter emitter : emitters)
        {
            if(!emitter.depleted())
            {
                return false;
            }
        }

        return true;
    }

    public void addEmitter(ParticleEmitterConfig config)
    {
        if(desc.emitterConfigs.contains(config, true))
        {
            throw new IllegalArgumentException("Context already contains this config");
        }

        desc.emitterConfigs.add(config);
        config.name = "Emitter " + (emitterCount());
        emitters.add(new ParticleEmitter(config, particlePool));
    }

    public void removeEmitter(ParticleEmitterConfig config)
    {
        if(!desc.emitterConfigs.contains(config, true))
        {
            throw new IllegalArgumentException("Context must already contain config");
        }

        ParticleEmitter emitter = null;
        for(ParticleEmitter e : emitters)
        {
            if(config == e.getConfig())  emitter = e;
        }

        desc.emitterConfigs.removeValue(config, true);
        emitters.removeValue(emitter, true);

        for(int i = 0; i < desc.emitterConfigs.size; i++)
        {
            desc.emitterConfigs.get(i).name = "Emitter " + i;
        }

        emitter.release();
    }

    public int emitterCount()
    {
        return emitters.size;
    }

    public ParticleEmitterConfig getConfig(int idx)
    {
        return desc.emitterConfigs.get(idx);
    }

    public ParticleEmitter getEmitter(int idx)
    {
        return emitters.get(idx);
    }

    public ParticleEmitter getEmitter(ParticleEmitterConfig config)
    {
        for(ParticleEmitter emitter : emitters)
        {
            if(config == emitter.getConfig())
            {
                return emitter;
            }
        }

        throw new IllegalArgumentException("Emitter with this config not found!");
    }

    public void reset()
    {
        for(ParticleEmitter emitter : emitters)
        {
            emitter.reset();
        }
    }
}
