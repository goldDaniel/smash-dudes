package smashdudes.particletool.logic;

import smashdudes.core.logic.commands.Command;
import smashdudes.core.logic.commands.CommandList;
import smashdudes.graphics.effects.ParticleEmitterConfig;

public class ParticleEditorContext
{
    private final CommandList cl;
    private ParticleEmitterConfig emitterConfig;

    private boolean playing = false;

    public ParticleEditorContext(CommandList cl)
    {
        this(cl, new ParticleEmitterConfig());
    }

    public ParticleEditorContext(CommandList cl, ParticleEmitterConfig config)
    {
        this.emitterConfig = config;
        this.cl = cl;
    }

    public void setParticleEmitterConfig(ParticleEmitterConfig config)
    {
        this.emitterConfig = config;
    }

    public ParticleEmitterConfig getParticleEmitterConfig()
    {
        return emitterConfig;
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
