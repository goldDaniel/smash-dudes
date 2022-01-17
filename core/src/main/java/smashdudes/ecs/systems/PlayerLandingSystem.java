package smashdudes.ecs.systems;

import com.badlogic.gdx.math.MathUtils;
import smashdudes.core.Collisions;
import smashdudes.ecs.Engine;
import smashdudes.ecs.Entity;
import smashdudes.ecs.components.*;
import smashdudes.ecs.events.JumpEvent;
import smashdudes.ecs.events.TerrainCollisionEvent;
import smashdudes.ecs.events.Event;

public class PlayerLandingSystem extends GameSystem
{
    public PlayerLandingSystem(Engine engine)
    {
        super(engine);
        registerEventType(TerrainCollisionEvent.class);
    }

    @Override
    protected void handleEvent(Event event)
    {
        Entity entity = event.entity;

        if(event instanceof TerrainCollisionEvent)
        {
            TerrainCollisionEvent e = (TerrainCollisionEvent)event;
            JumpComponent j = entity.getComponent(JumpComponent.class);

            if(e.collisionSide == Collisions.CollisionSide.Top)
            {
                entity.removeComponent(PlayerInAirComponent.class);
                if(!entity.hasComponent(PlayerIdleComponent.class) &&
                   !entity.hasComponent(PlayerRunningComponent.class) &&
                   !entity.hasComponent(PlayerOnGroundAttackStateComponent.class) &&
                   !entity.hasComponent(PlayerStunnedComponent.class))
                {
                    entity.addComponent(new PlayerIdleComponent());
                }
            }
        }
    }
}
