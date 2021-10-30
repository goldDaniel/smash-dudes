package smashdudes.ecs.systems;

import com.badlogic.gdx.Gdx;
import smashdudes.core.Collisions;
import smashdudes.ecs.Engine;
import smashdudes.ecs.Entity;
import smashdudes.ecs.components.*;
import smashdudes.ecs.events.Event;
import smashdudes.ecs.events.TerrainCollisionEvent;

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
        InAirComponent a = entity.getComponent(InAirComponent.class);
        OnGroundComponent g = entity.getComponent(OnGroundComponent.class);

        if(i.currentState.up && !i.previousState.up)
        {
            if (j.remainingJumps == j.maxJumps && g.isEnabled())
            {
                v.velocity.y = j.jumpStrength;
                a.enable();
                g.disable();
            }
            else if (j.remainingJumps == j.maxJumps)
            {
                j.remainingJumps--;
                v.velocity.y = j.extraJumpStrength;
            }
            else if (j.remainingJumps < j.maxJumps)
            {
                v.velocity.y = j.extraJumpStrength;
            }

            j.remainingJumps--;
            if (j.remainingJumps == 0)
            {
                j.disable();
            }
        }
    }

    @Override
    protected void handleEvent(Event event)
    {
        if(event instanceof TerrainCollisionEvent)
        {
            TerrainCollisionEvent e = (TerrainCollisionEvent)event;

            JumpComponent j = e.entity.getComponent(JumpComponent.class);
            InAirComponent a = e.entity.getComponent(InAirComponent.class);
            OnGroundComponent g = e.entity.getComponent(OnGroundComponent.class);
            if(e.collisionSide == Collisions.CollisionSide.Top)
            {
                j.enable();
                a.disable();
                g.enable();
                j.remainingJumps = j.maxJumps;
            }
        }
    }
}
