package smashdudes.core.state.playerstate;

import smashdudes.core.state.State;
import smashdudes.ecs.Entity;
import smashdudes.ecs.components.*;
import smashdudes.ecs.events.JumpEvent;
import smashdudes.gameplay.PlayerState;

public class GroundIdleState extends State
{
    public GroundIdleState(Entity entity)
    {
        super(entity);
    }

    @Override
    public void innerOnEnter(float dt)
    {
        PlayerAnimationContainerComponent container = entity.getComponent(PlayerAnimationContainerComponent.class);
        if(entity.getComponent(AnimationComponent.class) != container.idle)
        {
            entity.removeComponent(AnimationComponent.class);
            entity.addComponent(container.idle);
        }
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
        else if( !(i.currentState.left && i.currentState.right) &&
                (i.currentState.left || i.currentState.right) )
        {
            //player.currentState = PlayerState.Ground_Running;
        }
        else if(i.currentState.up)
        {
            v.velocity.y = j.jumpStrength;
            return new AirIdleState(entity);
        }

        return this;
    }
}
