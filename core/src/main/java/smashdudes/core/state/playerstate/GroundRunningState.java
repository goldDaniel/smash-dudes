package smashdudes.core.state.playerstate;

import smashdudes.core.state.State;
import smashdudes.ecs.Entity;
import smashdudes.ecs.components.*;
import smashdudes.ecs.events.JumpEvent;

public class GroundRunningState extends State
{
    public GroundRunningState(Entity entity)
    {
        super(entity);
    }

    @Override
    public void onEnter(float dt)
    {
        PlayerAnimationContainerComponent container = entity.getComponent(PlayerAnimationContainerComponent.class);
        entity.removeComponent(AnimationComponent.class);
        entity.addComponent(container.running);
    }

    @Override
    public void innerUpdate(float dt)
    {
        CharacterInputComponent ci = entity.getComponent(CharacterInputComponent.class);
        VelocityComponent v  = entity.getComponent(VelocityComponent.class);

        float speed = v.runSpeed;
        if(ci.currentState.left)
        {
            v.velocity.x -= speed;
        }
        if(ci.currentState.right)
        {
            v.velocity.x += speed;
        }
    }

    @Override
    public void onExit()
    {
        PlayerAnimationContainerComponent container = entity.getComponent(PlayerAnimationContainerComponent.class);
        container.running.reset();
    }

    @Override
    public State getNextState()
    {
        JumpComponent j = entity.getComponent(JumpComponent.class);
        CharacterInputComponent ci = entity.getComponent(CharacterInputComponent.class);
        VelocityComponent v  = entity.getComponent(VelocityComponent.class);

        if(ci.currentState.punch)
        {
            return new GroundAttackState(entity);
        }
        else if(ci.currentState.up && v.velocity.y == 0)
        {
            v.velocity.y = j.jumpStrength;
            throwEvent(new JumpEvent(entity));
            return new AirIdleState(entity);
        }
        else if( (ci.currentState.left && ci.currentState.right) ||
                !(ci.currentState.left || ci.currentState.right) ||
                  Math.abs(v.velocity.y) > 0)
        {
            if(Math.abs(v.velocity.y) > 0)
            {
                return new AirIdleState(entity);
            }
            else
            {
                return new GroundIdleState(entity);
            }
        }
        else if (v.velocity.y < 0)
        {
            return new AirIdleState(entity);
        }

        return this;
    }
}
