package smashdudes.core.state.playerstate;

import smashdudes.core.state.State;
import smashdudes.ecs.Entity;
import smashdudes.ecs.components.*;
import smashdudes.ecs.events.JumpEvent;

public class GroundIdleState extends State
{
    public GroundIdleState(Entity entity)
    {
        super(entity);
    }

    @Override
    public void onEnter(float dt)
    {
        AnimationContainerComponent container = entity.getComponent(AnimationContainerComponent.class);
        entity.removeComponent(AnimationComponent.class);
        AnimationComponent anim = container.get(this.getClass());
        anim.reset();
        entity.addComponent(anim);
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
            return new GroundRunningState(entity);
        }
        else if(i.currentState.up)
        {
            v.velocity.y = j.jumpStrength;
            throwEvent(new JumpEvent(entity));
            return new JumpState(entity);
        }
        else if(v.velocity.y < 0)
        {
            return new FallingState(entity);
        }

        return this;
    }
}
