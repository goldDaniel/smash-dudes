package smashdudes.ecs.events;

public class AudioEvent extends Event
{
    public final String audioFile;
    public final boolean isSound; // true for fx, false for music

    public AudioEvent(String audioFile, boolean sound)
    {
        super(null);
        this.audioFile = audioFile;
        this.isSound = sound;
    }
}
