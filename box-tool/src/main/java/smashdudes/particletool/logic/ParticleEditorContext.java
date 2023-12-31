package smashdudes.particletool.logic;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ArrayMap;
import com.badlogic.gdx.utils.Pool;
import smashdudes.content.DTO;
import smashdudes.core.logic.commands.Command;
import smashdudes.core.logic.commands.CommandList;
import smashdudes.graphics.effects.Particle;
import smashdudes.graphics.effects.ParticleEmitter;
import smashdudes.graphics.effects.ParticleEmitterConfig;

public class ParticleEditorContext
{
    private final CommandList cl;

    private DTO.EffectDescription effect;

    private final ArrayMap<ParticleEmitterConfig, ParticleEmitter> emitters = new ArrayMap<>();

    private ParticleEmitterConfig selectedConfig;

    private final Pool<Particle> particlePool = new Pool<Particle>(8192)
    {
        @Override
        protected Particle newObject()
        {
            return new Particle();
        }
    };

    private boolean playing = false;


    public ParticleEditorContext(CommandList cl, DTO.EffectDescription effect)
    {
        this.cl = cl;
        this.effect = effect;
    }

    public void setEffect(DTO.EffectDescription effect)
    {
        this.effect = effect;
        this.cl.clear();
        this.particlePool.clear();
        this.emitters.clear();
        this.selectedConfig = null;

        this.playing = true;

        for (ParticleEmitterConfig config : effect.emitterConfigs)
        {
            emitters.put(config, new ParticleEmitter(config, particlePool));
        }
        if(effect.emitterConfigs.size > 0)
        {
            selectedConfig = effect.emitterConfigs.get(0);
        }
    }


    public DTO.EffectDescription getEffect()
    {
        return this.effect;
    }


    public ParticleEmitterConfig getSelectedConfig()
    {
        return selectedConfig;
    }

    public void setSelectedConfig(ParticleEmitterConfig config)
    {
        this.selectedConfig = config;
    }

    public void addEmitter(ParticleEmitterConfig config)
    {
        if(emitters.containsKey(config))
        {
            throw new IllegalArgumentException("Context already contains this config");
        }

        effect.emitterConfigs.add(config);
        config.name = "Emitter " + (effect.emitterConfigs.size - 1);

        emitters.put(config, new ParticleEmitter(config, particlePool));
    }

    public ParticleEmitter getEmitter(ParticleEmitterConfig config)
    {
        return emitters.get(config);
    }

    public void removeEmitter(ParticleEmitterConfig config)
    {
        if(!emitters.containsKey(config))
        {
            throw new IllegalArgumentException("Context must already contain config");
        }

        ParticleEmitter emitter = emitters.removeKey(config);
        effect.emitterConfigs.removeValue(config, true);
        for(int i = 0; i < effect.emitterConfigs.size; i++)
        {
             effect.emitterConfigs.get(i).name = "Emitter " + i;
        }

        emitter.release();
    }

    public Iterable<ParticleEmitter> getEmitters()
    {
        // draw order is the order inside the effect
        Array<ParticleEmitter> result = new Array<>();
        for(ParticleEmitterConfig config : effect.emitterConfigs)
        {
            result.add(emitters.get(config));
        }

        return result;
    }

    public void play()
    {
        playing = true;
    }

    public void stop()
    {
        playing = false;
    }

    public boolean isPlaying()
    {
        return playing;
    }

    public void execute(Command c)
    {
        cl.execute(c);
    }
}
