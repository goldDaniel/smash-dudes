package smashdudes.ecs.systems;

import com.badlogic.gdx.math.Vector2;
import smashdudes.ecs.Engine;
import smashdudes.ecs.Entity;
import smashdudes.ecs.components.PlayerComponent;
import smashdudes.ecs.components.PlayerControllerComponent;
import smashdudes.ecs.components.PlayerInAirComponent;
import smashdudes.ecs.components.VelocityComponent;
import smashdudes.ecs.events.Event;
import smashdudes.ecs.events.RespawnEvent;

public class PlayerResetSystem extends GameSystem
{
    public PlayerResetSystem(Engine engine)
    {
        super(engine);
        registerEventType(RespawnEvent.class);
    }

    @Override
    public void handleEvent(Event event)
    {
        if(event instanceof RespawnEvent)
        {
            RespawnEvent e = (RespawnEvent)event;
            VelocityComponent vel = e.entity.getComponent(VelocityComponent.class);
            vel.velocity.set(new Vector2());
        }
    }
}
