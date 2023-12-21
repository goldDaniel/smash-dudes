package smashdudes.core.state.playerstate;

import smashdudes.core.AnimationSequence;
import smashdudes.core.state.State;
import smashdudes.ecs.Entity;
import smashdudes.ecs.components.AnimationComponent;
import smashdudes.ecs.components.AnimationContainerComponent;
import smashdudes.ecs.components.VelocityComponent;

public class GroundAttackState extends State
{
    public GroundAttackState(Entity entity)
    {
        super(entity);
    }

    @Override
    public void onEnter(float dt)
    {
        VelocityComponent v = entity.getComponent(VelocityComponent.class);
        v.velocity.x = 0;

        AnimationContainerComponent container = entity.getComponent(AnimationContainerComponent.class);
        entity.removeComponent(AnimationComponent.class);
        AnimationSequence seq = container.get(this.getClass());
        seq.reset();
        AnimationComponent anim = seq.getAnimation(0);
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
        AnimationComponent anim = super.entity.getComponent(AnimationComponent.class);
        if(anim.isFinished())
        {
            return new GroundIdleState(super.entity);
        }

        return this;
    }
}
