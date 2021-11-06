package smashdudes.ecs.systems;

import smashdudes.core.AudioResources;
import smashdudes.ecs.Engine;
import smashdudes.ecs.events.Event;
import smashdudes.ecs.events.JumpEvent;

public class AudioSystem extends GameSystem
{
    public AudioSystem(Engine engine)
    {
        super(engine);

        registerEventType(JumpEvent.class);
    }

    @Override
    protected void handleEvent(Event event)
    {
        if (event instanceof JumpEvent)
        {
            AudioResources.getSoundEffect("audio/Jump.wav").play(0.1f);
        }
    }
}
