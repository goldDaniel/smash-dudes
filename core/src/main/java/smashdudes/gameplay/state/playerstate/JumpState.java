package smashdudes.gameplay.state.playerstate;

import smashdudes.gameplay.state.State;
import smashdudes.ecs.Entity;
import smashdudes.ecs.components.AnimationComponent;
import smashdudes.ecs.components.CharacterInputComponent;
import smashdudes.ecs.components.AnimationContainerComponent;
import smashdudes.ecs.components.VelocityComponent;

public class JumpState extends PlayerState
{
    public JumpState(Entity entity)
    {
        super(entity);
    }

    @Override
    public void innerUpdate(float dt)
    {
        CharacterInputComponent ci = entity.getComponent(CharacterInputComponent.class);
        VelocityComponent v = entity.getComponent(VelocityComponent.class);

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
        VelocityComponent v = entity.getComponent(VelocityComponent.class);
        if(v.velocity.y <= 0)
        {
           return new FallingState(entity);
        }

        return this;
    }
}
