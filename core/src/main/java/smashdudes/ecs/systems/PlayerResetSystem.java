package smashdudes.ecs.systems;

import smashdudes.ecs.Engine;
import smashdudes.ecs.Entity;
import smashdudes.ecs.components.PlayerComponent;
import smashdudes.ecs.components.PlayerControllerComponent;
import smashdudes.ecs.components.PlayerInAirComponent;
import smashdudes.ecs.events.Event;
import smashdudes.ecs.events.RespawnEvent;

public class PlayerResetSystem extends GameSystem
{
    public PlayerResetSystem(Engine engine)
    {
        super(engine);
        registerComponentType(PlayerComponent.class);
        registerComponentType(PlayerControllerComponent.class);

        registerEventType(RespawnEvent.class);
    }

    @Override
    public void handleEvent(Event event)
    {
        if(event instanceof RespawnEvent)
        {
            
        }
    }
}
