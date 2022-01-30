package smashdudes.ecs.systems;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.utils.Queue;
import smashdudes.core.AudioResources;
import smashdudes.ecs.Engine;
import smashdudes.ecs.events.Event;
import smashdudes.ecs.events.JumpEvent;
import smashdudes.ecs.events.LandingEvent;
import smashdudes.ecs.events.*;

public class AudioSystem extends GameSystem
{
    public static float masterVolume = 1f;

    private class Audio
    {
        private final Sound sound;
        private final float volume;

        private Audio(Sound sound, float volume)
        {
            this.sound = sound;
            this.volume = volume;
        }
    }

    private Queue<Audio> sounds = new Queue<>();

    public AudioSystem(Engine engine)
    {
        super(engine);

        registerEventType(JumpEvent.class);
        registerEventType(LandingEvent.class);
        registerEventType(CountdownEvent.class);
    }

    @Override
    protected void handleEvent(Event event)
    {
        if (event instanceof JumpEvent)
        {
            Sound s = AudioResources.getSoundEffect("audio/effects/Jump.wav");
            sounds.addLast(new Audio(s, 0.1f * masterVolume));
        }
        else if (event instanceof LandingEvent)
        {
            Sound s = AudioResources.getSoundEffect("audio/effects/Land.wav");
            sounds.addLast(new Audio(s, 0.1f * masterVolume));
        }

        if(event instanceof CountdownEvent)
        {
            Sound s = AudioResources.getSoundEffect("audio/effects/Jump.wav");
            sounds.addLast(new Audio(s, 0.5f));
        }
    }

    @Override
    protected void postUpdate()
    {
        while(sounds.notEmpty())
        {
            Audio a = sounds.removeFirst();
            a.sound.play(a.volume);
        }
    }
}
