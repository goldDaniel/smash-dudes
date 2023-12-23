package smashdudes.gameplay.state.playerstate.ground;

import smashdudes.ecs.Entity;
import smashdudes.ecs.components.CharacterInputComponent;
import smashdudes.ecs.components.JumpComponent;
import smashdudes.ecs.components.VelocityComponent;
import smashdudes.gameplay.state.State;

public class GroundIdleState extends PlayerGroundState
{
    public GroundIdleState(Entity entity)
    {
        super(entity);
    }

    @Override
    public void innerUpdate(float dt)
    {
    }

    @Override
    public void onExit()
    {

    }

    @Override
    public State getNextState()
    {
        CharacterInputComponent i = entity.getComponent(CharacterInputComponent.class);
        VelocityComponent v = entity.getComponent(VelocityComponent.class);
        JumpComponent j = entity.getComponent(JumpComponent.class);

        if(i.currentState.punch)
        {
            return new GroundAttackState(entity);
        }
        else if( !(i.currentState.left && i.currentState.right) && (i.currentState.left || i.currentState.right) )
        {
            return new GroundRunningState(entity);
        }
        else if(i.currentState.up)
        {
            v.velocity.y = j.jumpStrength;
            return new JumpState(entity);
        }
        else if(v.velocity.y < 0)
        {
            return new FallingState(entity);
        }

        return this;
    }
}
