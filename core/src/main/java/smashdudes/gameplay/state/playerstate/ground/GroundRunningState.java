package smashdudes.gameplay.state.playerstate.ground;

import smashdudes.ecs.Entity;
import smashdudes.ecs.components.CharacterInputComponent;
import smashdudes.ecs.components.JumpComponent;
import smashdudes.ecs.components.VelocityComponent;
import smashdudes.ecs.events.JumpEvent;
import smashdudes.gameplay.state.State;
import smashdudes.gameplay.state.playerstate.air.JumpState;

public class GroundRunningState extends PlayerGroundState
{
    public GroundRunningState(Entity entity)
    {
        super(entity);
    }

    @Override
    public void innerUpdate(float dt)
    {
        CharacterInputComponent ci = entity.getComponent(CharacterInputComponent.class);
        VelocityComponent v  = entity.getComponent(VelocityComponent.class);

        float speed = 0;
        if(ci.currentState.left)
        {
            speed -= v.runSpeed;
        }
        if(ci.currentState.right)
        {
            speed += v.runSpeed;
        }
        if(Math.abs(speed) > 0)
        {
            v.velocity.x = speed;
        }
    }

    @Override
    public void onExit()
    {
        
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
            return new JumpState(entity);
        }
        else if( (ci.currentState.left && ci.currentState.right) ||
                !(ci.currentState.left || ci.currentState.right) ||
                  Math.abs(v.velocity.y) > 0)
        {
            if(Math.abs(v.velocity.y) > 0)
            {
                return new JumpState(entity);
            }
            else
            {
                return new GroundIdleState(entity);
            }
        }
        else if (v.velocity.y < 0)
        {
            return new JumpState(entity);
        }

        return this;
    }
}
