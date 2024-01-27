package smashdudes.particletool.logic;

import com.badlogic.gdx.utils.Pool;
import smashdudes.content.DTO;
import smashdudes.core.logic.commands.Command;
import smashdudes.core.logic.commands.CommandList;
import smashdudes.graphics.effects.Effect;
import smashdudes.graphics.effects.Particle;
import smashdudes.graphics.effects.ParticleEmitter;
import smashdudes.graphics.effects.ParticleEmitterConfig;

public class ParticleEditorContext
{
    private final CommandList cl;

    private DTO.EffectDescription effectDesc;

    private Effect effect;

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


    public ParticleEditorContext(CommandList cl)
    {
        this.cl = cl;
        setEffect(new DTO.EffectDescription());
    }

    public void setEffect(DTO.EffectDescription effectDesc)
    {
        this.cl.clear();
        this.particlePool.clear();
        this.selectedConfig = null;

        this.playing = true;

        this.effectDesc = effectDesc;
        this.effect = new Effect(effectDesc, particlePool);
        if(effect.emitterCount() > 0)
        {
            selectedConfig = effect.getConfig(0);
        }
    }


    public DTO.EffectDescription getEffect()
    {
        return this.effectDesc;
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
        effect.addEmitter(config);
    }

    public ParticleEmitter getEmitter(ParticleEmitterConfig config)
    {
        return effect.getEmitter(config);
    }

    public void removeEmitter(ParticleEmitterConfig config)
    {
        effect.removeEmitter(config);
    }

    public void play()
    {
        playing = true;
    }

    public void pause()
    {
        playing = false;
    }

    public boolean isPlaying()
    {
        if(effect.isFinished())
        {
            playing = false;
        }
        return playing;
    }

    public void execute(Command c)
    {
        cl.execute(c);
    }

    public void reset()
    {
        effect.reset();
    }
}
