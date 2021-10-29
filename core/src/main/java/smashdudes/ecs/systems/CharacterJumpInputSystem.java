package smashdudes.ecs.systems;

import com.badlogic.gdx.Gdx;
import smashdudes.ecs.Engine;
import smashdudes.ecs.Entity;
import smashdudes.ecs.components.CharacterInputComponent;
import smashdudes.ecs.components.JumpComponent;
import smashdudes.ecs.components.OnGroundComponent;
import smashdudes.ecs.components.VelocityComponent;
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
        registerComponentType(OnGroundComponent.class);


        registerEventType(TerrainCollisionEvent.class);
    }

    @Override
    protected void updateEntity(Entity entity, float dt)
    {
        CharacterInputComponent i = entity.getComponent(CharacterInputComponent.class);
        VelocityComponent v = entity.getComponent(VelocityComponent.class);
        JumpComponent j = entity.getComponent(JumpComponent.class);

        if(i.currentState.up)
        {
            v.velocity.y = j.jumpStrength;
            j.disable();

            entity.removeComponent(OnGroundComponent.class);
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
            }
        }
    }
}
