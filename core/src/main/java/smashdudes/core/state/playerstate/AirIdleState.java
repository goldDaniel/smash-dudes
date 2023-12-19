package smashdudes.core.state.playerstate;

import smashdudes.core.state.State;
import smashdudes.ecs.Entity;
import smashdudes.ecs.components.AnimationComponent;
import smashdudes.ecs.components.CharacterInputComponent;
import smashdudes.ecs.components.PlayerAnimationContainerComponent;
import smashdudes.ecs.components.VelocityComponent;

public class AirIdleState extends State
{
    public AirIdleState(Entity entity)
    {
        super(entity);
    }

    @Override
    public void innerOnEnter(float dt)
    {

    }

    @Override
    public void innerUpdate(float dt)
    {
        CharacterInputComponent ci = entity.getComponent(CharacterInputComponent.class);
        VelocityComponent v = entity.getComponent(VelocityComponent.class);
        PlayerAnimationContainerComponent container = entity.getComponent(PlayerAnimationContainerComponent.class);
        AnimationComponent current = entity.getComponent(AnimationComponent.class);

        if(current != container.jumping || current != container.falling)
        {
            entity.removeComponent(AnimationComponent.class);
            if(v.velocity.y > 0)
            {
                entity.addComponent(container.jumping);
            }
            else
            {
                entity.addComponent(container.falling);
            }
        }

        if(ci.currentState.left || ci.currentState.right)
        {
            float speed = v.airSpeed;
            if(ci.currentState.left)
            {
                v.velocity.x -= speed;
            }
            if(ci.currentState.right)
            {
                v.velocity.x += speed;
            }
        }
    }

    @Override
    public void onExit()
    {

    }

    @Override
    public State getNextState()
    {
        return this;
    }
}
