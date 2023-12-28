package smashdudes.ecs.events;

import com.badlogic.gdx.utils.Array;
import smashdudes.content.AnimationEvent;

public class AnimationEventBuilder
{
    public static Array<Event> getFrameEvents(Array<AnimationEvent> events)
    {
        Array<Event> result = new Array<>();

        for(AnimationEvent event : events)
        {
            if(!event.eventFired)
            {
                event.eventFired = true;
                switch(event.type)
                {
                    case Audio: result.add(buildAudioEvent(event)); break;
                    case Particle: throw new IllegalStateException("UNIMPLEMENTED");
                }
            }
        }

        return result;
    }

    private static AudioEvent buildAudioEvent(AnimationEvent event)
    {
        // HACK (danielg): remove before merge
        return new AudioEvent(event.data, true);
    }
}
