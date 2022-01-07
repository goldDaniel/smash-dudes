package smashdudes.ecs.systems;

import smashdudes.ecs.Engine;
import smashdudes.ecs.Entity;
import smashdudes.ecs.components.*;
import smashdudes.ecs.events.JumpEvent;

public class PlayerRunningSystem extends GameSystem
{
    public PlayerRunningSystem(Engine engine)
    {
        super(engine);

        registerComponentType(PlayerRunningComponent.class);
        registerComponentType(PlayerAnimationContainerComponent.class);
        registerComponentType(CharacterInputComponent.class);
        registerComponentType(VelocityComponent.class);
        registerComponentType(JumpComponent.class);
    }

    @Override
    public void updateEntity(Entity entity, float dt)
    {
        CharacterInputComponent ci = entity.getComponent(CharacterInputComponent.class);
        VelocityComponent v  = entity.getComponent(VelocityComponent.class);
        JumpComponent j = entity.getComponent(JumpComponent.class);

        PlayerAnimationContainerComponent container = entity.getComponent(PlayerAnimationContainerComponent.class);
        if(entity.getComponent(AnimationComponent.class) != container.running)
        {
            entity.removeComponent(AnimationComponent.class);
            entity.addComponent(container.running);
        }

        if(ci.currentState.punch)
        {
            entity.removeComponent(PlayerRunningComponent.class);
            entity.addComponent(new PlayerOnGroundAttackStateComponent());
        }
        else if(ci.currentState.up && v.velocity.y == 0)
        {
            v.velocity.y = j.jumpStrength;
            j.disable();
            engine.addEvent(new JumpEvent(entity));

            entity.addComponent(new PlayerInAirComponent());

            entity.removeComponent(PlayerRunningComponent.class);
            entity.removeComponent(PlayerIdleComponent.class);
        }
        else if( (ci.currentState.left && ci.currentState.right) ||
                !(ci.currentState.left || ci.currentState.right))
        {
            entity.removeComponent(PlayerRunningComponent.class);
            entity.addComponent(new PlayerIdleComponent());
        }
        else
        {
            float speed = v.runSpeed;
            v.velocity.x *= v.deceleration * dt;
            if(ci.currentState.left)
            {
                v.velocity.x -= speed;
            }
            if(ci.currentState.right)
            {
                v.velocity.x += speed;
            }
        }
    }
}
