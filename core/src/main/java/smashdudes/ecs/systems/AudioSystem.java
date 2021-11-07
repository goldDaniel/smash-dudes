package smashdudes.ecs.systems;

import smashdudes.core.AudioResources;
import smashdudes.ecs.Engine;
import smashdudes.ecs.events.Event;
import smashdudes.ecs.events.JumpEvent;
import smashdudes.ecs.events.LandingEvent;
import smashdudes.ecs.events.TerrainCollisionEvent;

public class AudioSystem extends GameSystem
{
    public AudioSystem(Engine engine)
    {
        super(engine);

        registerEventType(JumpEvent.class);
        registerEventType(LandingEvent.class);
    }

    @Override
    protected void handleEvent(Event event)
    {
        if (event instanceof JumpEvent)
        {
            AudioResources.getSoundEffect("audio/Jump.wav").play(0.1f);
        }
        else if (event instanceof LandingEvent)
        {
            AudioResources.getSoundEffect("audio/Land.wav").play(0.1f); // will machine gun audio
        }
    }
}
