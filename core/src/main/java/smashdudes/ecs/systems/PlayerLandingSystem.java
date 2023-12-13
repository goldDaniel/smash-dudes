package smashdudes.ecs.systems;

import com.badlogic.gdx.math.MathUtils;
import smashdudes.core.Collisions;
import smashdudes.ecs.Engine;
import smashdudes.ecs.Entity;
import smashdudes.ecs.components.*;
import smashdudes.ecs.events.JumpEvent;
import smashdudes.ecs.events.TerrainCollisionEvent;
import smashdudes.ecs.events.Event;
import smashdudes.gameplay.PlayerState;

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
            PlayerComponent player = entity.getComponent(PlayerComponent.class);
            VelocityComponent v = entity.getComponent(VelocityComponent.class);

            if(e.collisionSide == Collisions.CollisionSide.Top)
            {
                if(Math.abs(v.velocity.x) > 0)
                {
                    player.currentState = PlayerState.Ground_Running;
                }
                else
                {
                    player.currentState = PlayerState.Ground_Idle;
                }
            }
        }
    }
}
