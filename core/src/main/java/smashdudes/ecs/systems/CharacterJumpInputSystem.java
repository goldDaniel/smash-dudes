package smashdudes.ecs.systems;

import com.badlogic.gdx.math.MathUtils;
import smashdudes.core.Collisions;
import smashdudes.ecs.Engine;
import smashdudes.ecs.Entity;
import smashdudes.ecs.components.*;
import smashdudes.ecs.events.JumpEvent;
import smashdudes.ecs.events.TerrainCollisionEvent;
import smashdudes.ecs.events.Event;

public class CharacterJumpInputSystem extends GameSystem
{
    public CharacterJumpInputSystem(Engine engine)
    {
        super(engine);
        registerComponentType(VelocityComponent.class);
        registerComponentType(CharacterInputComponent.class);
        registerComponentType(JumpComponent.class);

        registerEventType(TerrainCollisionEvent.class);
    }

    @Override
    protected void updateEntity(Entity entity, float dt)
    {
        CharacterInputComponent i = entity.getComponent(CharacterInputComponent.class);
        VelocityComponent v = entity.getComponent(VelocityComponent.class);
        JumpComponent j = entity.getComponent(JumpComponent.class);

        if(i.currentState.up && v.velocity.y == 0)
        {
            v.velocity.y = j.jumpStrength;
            j.disable();
            engine.addEvent(new JumpEvent(entity));

            entity.addComponent(new PlayerInAirComponent());

            entity.removeComponent(PlayerRunningComponent.class);
            entity.removeComponent(PlayerIdleComponent.class);
        }
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
                j.enable();
                entity.removeComponent(PlayerInAirComponent.class);
                if(!entity.hasComponent(PlayerIdleComponent.class) &&
                   !entity.hasComponent(PlayerRunningComponent.class) &&
                   !entity.hasComponent(PlayerOnGroundAttackStateComponent.class))
                {
                    entity.addComponent(new PlayerIdleComponent());
                }
            }
        }
    }
}
