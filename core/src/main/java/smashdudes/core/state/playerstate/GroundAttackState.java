package smashdudes.core.state.playerstate;

import smashdudes.core.state.State;
import smashdudes.ecs.Entity;
import smashdudes.ecs.components.AnimationComponent;
import smashdudes.ecs.components.PlayerAnimationContainerComponent;
import smashdudes.ecs.components.VelocityComponent;
import smashdudes.gameplay.PlayerState;

public class GroundAttackState extends State
{
    public GroundAttackState(Entity entity)
    {
        super(entity);
    }

    @Override
    public void innerOnEnter(float dt)
    {
        VelocityComponent v = entity.getComponent(VelocityComponent.class);
        v.velocity.x = 0;

        PlayerAnimationContainerComponent container = entity.getComponent(PlayerAnimationContainerComponent.class);
        entity.removeComponent(AnimationComponent.class).reset();
        entity.addComponent(container.attack_1);
        container.attack_1.reset();
    }

    @Override
    public void innerUpdate(float dt)
    {

    }

    @Override
    public void onExit()
    {
        PlayerAnimationContainerComponent container = entity.getComponent(PlayerAnimationContainerComponent.class);
        container.attack_1.reset();
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
