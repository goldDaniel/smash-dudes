package smashdudes.audio;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.utils.Queue;

public class AudioEventReceiver
{
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

    public void playSounds()
    {
        while(sounds.notEmpty())
        {
            Audio a = sounds.removeFirst();
            a.sound.play(a.volume);
        }
    }

    public void addSound(Sound sound, float volume)
    {
        sounds.addLast(new Audio(sound, volume));
    }
}
