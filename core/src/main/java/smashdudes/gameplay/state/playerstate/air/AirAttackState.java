package smashdudes.gameplay.state.playerstate.air;

import smashdudes.ecs.Entity;
import smashdudes.ecs.components.AnimationComponent;
import smashdudes.gameplay.state.State;
import smashdudes.gameplay.state.playerstate.ground.GroundIdleState;

public class AirAttackState extends PlayerAirState
{
    public AirAttackState(Entity entity)
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
        AnimationComponent anim = entity.getComponent(AnimationComponent.class);
        if(anim.isFinished())
        {
            return new FallingState(entity);
        }

        return this;
    }
}
