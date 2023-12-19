package smashdudes.ecs.systems;

import smashdudes.core.Collisions;
import smashdudes.core.state.playerstate.AirIdleState;
import smashdudes.core.state.playerstate.GroundIdleState;
import smashdudes.ecs.Engine;
import smashdudes.ecs.Entity;
import smashdudes.ecs.components.*;
import smashdudes.ecs.events.*;
import smashdudes.gameplay.PlayerState;

public class StateSystem extends GameSystem
{
    public StateSystem(Engine engine)
    {
        super(engine);
        registerComponentType(StateComponent.class);
    }
    @Override
    public void updateEntity(Entity entity, float dt)
    {
        StateComponent s = entity.getComponent(StateComponent.class);
        s.state.update(dt);
        s.setNextState(s.state.getNextState());
    }

    private void GroundRunning(Entity entity, PlayerComponent player, float dt)
    {
        CharacterInputComponent ci = entity.getComponent(CharacterInputComponent.class);
        VelocityComponent v  = entity.getComponent(VelocityComponent.class);
        JumpComponent j = entity.getComponent(JumpComponent.class);

        PlayerAnimationContainerComponent container = entity.getComponent(PlayerAnimationContainerComponent.class);
        if(entity.getComponent(AnimationComponent.class) != container.running)
        {
            entity.removeComponent(AnimationComponent.class);
            entity.addComponent(container.running);
        }

        if(ci.currentState.punch)
        {
            player.currentState = PlayerState.Ground_Attack;
        }
        else if(ci.currentState.up && v.velocity.y == 0)
        {
            v.velocity.y = j.jumpStrength;
            engine.addEvent(new JumpEvent(entity));

            player.currentState = PlayerState.Air_Idle;
        }
        else if( (ci.currentState.left && ci.currentState.right) ||
                !(ci.currentState.left || ci.currentState.right))
        {
            if(Math.abs(v.velocity.y) > 0)
            {
                player.currentState = PlayerState.Air_Idle;
            }
            else
            {
                player.currentState = PlayerState.Ground_Idle;
            }
        }
        else
        {
            float speed = v.runSpeed;
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
    protected void handleEvent(Event event)
    {
        Entity entity = event.entity;

        if(event instanceof LandingEvent)
        {
            StateComponent s = entity.getComponent(StateComponent.class);
            if (s.state instanceof AirIdleState)
            {
                s.setNextState(new GroundIdleState(entity));
            }
        }
    }
}
