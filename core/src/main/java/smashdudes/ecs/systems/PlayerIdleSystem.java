package smashdudes.ecs.systems;

import smashdudes.ecs.Engine;
import smashdudes.ecs.Entity;
import smashdudes.ecs.components.*;
import smashdudes.ecs.events.JumpEvent;

public class PlayerIdleSystem extends GameSystem
{
    public PlayerIdleSystem(Engine engine)
    {
        super(engine);
        registerComponentType(CharacterInputComponent.class);
        registerComponentType(PlayerAnimationContainerComponent.class);
        registerComponentType(PlayerIdleComponent.class);
        registerComponentType(VelocityComponent.class);
    }

    @Override
    public void updateEntity(Entity entity, float dt)
    {
        CharacterInputComponent i = entity.getComponent(CharacterInputComponent.class);
        VelocityComponent v = entity.getComponent(VelocityComponent.class);
        JumpComponent j = entity.getComponent(JumpComponent.class);

        PlayerAnimationContainerComponent container = entity.getComponent(PlayerAnimationContainerComponent.class);
        if(entity.getComponent(AnimationComponent.class) != container.idle)
        {
            entity.removeComponent(AnimationComponent.class);
            entity.addComponent(container.idle);
        }

        v.velocity.x *= v.deceleration * dt;

        if(i.currentState.punch)
        {
            entity.removeComponent(PlayerIdleComponent.class);
            entity.addComponent(new PlayerOnGroundAttackStateComponent());
        }
        else if(i.currentState.up && v.velocity.y == 0)
        {
            v.velocity.y = j.jumpStrength;
            j.disable();
            engine.addEvent(new JumpEvent(entity));

            entity.addComponent(new PlayerInAirComponent());

            entity.removeComponent(PlayerRunningComponent.class);
            entity.removeComponent(PlayerIdleComponent.class);
        }
        else if( !(i.currentState.left && i.currentState.right) &&
                  (i.currentState.left || i.currentState.right) )
        {
            entity.removeComponent(PlayerIdleComponent.class);
            entity.addComponent(new PlayerRunningComponent());
        }
    }
}
