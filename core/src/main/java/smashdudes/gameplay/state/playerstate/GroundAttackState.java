package smashdudes.gameplay.state.playerstate;

import smashdudes.gameplay.state.State;
import smashdudes.ecs.Entity;
import smashdudes.ecs.components.AnimationComponent;
import smashdudes.ecs.components.AnimationContainerComponent;
import smashdudes.ecs.components.VelocityComponent;

public class GroundAttackState extends PlayerState
{
    public GroundAttackState(Entity entity)
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
        AnimationComponent anim = super.entity.getComponent(AnimationComponent.class);
        if(anim.isFinished())
        {
            return new GroundIdleState(super.entity);
        }

        return this;
    }
}
