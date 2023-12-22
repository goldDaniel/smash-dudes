package smashdudes.gameplay.state.playerstate.ground;

import smashdudes.ecs.Entity;
import smashdudes.ecs.components.AnimationComponent;
import smashdudes.gameplay.state.State;

public class GroundAttackState extends PlayerGroundState
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
