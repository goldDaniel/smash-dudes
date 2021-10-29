package smashdudes.ecs.systems;

import com.badlogic.gdx.Gdx;
import smashdudes.ecs.Engine;
import smashdudes.ecs.Entity;
import smashdudes.ecs.components.InputConfigComponent;
import smashdudes.ecs.components.JumpComponent;
import smashdudes.ecs.components.OnGroundComponent;
import smashdudes.ecs.components.VelocityComponent;
import smashdudes.ecs.events.Event;
import smashdudes.ecs.events.TerrainCollisionEvent;

public class JumpInputSystem extends GameSystem
{
    public JumpInputSystem(Engine engine)
    {
        super(engine);
        registerComponentType(VelocityComponent.class);
        registerComponentType(InputConfigComponent.class);
        registerComponentType(JumpComponent.class);

        registerEventType(TerrainCollisionEvent.class);
    }

    @Override
    protected void updateEntity(Entity entity, float dt)
    {
        InputConfigComponent i = entity.getComponent(InputConfigComponent.class);
        VelocityComponent v = entity.getComponent(VelocityComponent.class);
        JumpComponent j = entity.getComponent(JumpComponent.class);
        if (Gdx.input.isKeyJustPressed(i.config.up)) // jump input removed upon single jump
        {
            if (j.remainingJumps == j.maxJumps)
            {
                v.velocity.y = j.jumpStrength;
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
            if(e.collisionSide == TerrainCollisionSystem.CollisionSide.Top)
            {
                j.enable();
                j.remainingJumps = j.maxJumps;
            }
        }
    }
}
