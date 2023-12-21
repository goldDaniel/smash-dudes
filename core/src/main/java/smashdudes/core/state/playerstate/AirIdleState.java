package smashdudes.core.state.playerstate;

import smashdudes.core.AnimationSequence;
import smashdudes.core.state.State;
import smashdudes.ecs.Entity;
import smashdudes.ecs.components.AnimationComponent;
import smashdudes.ecs.components.CharacterInputComponent;
import smashdudes.ecs.components.AnimationContainerComponent;
import smashdudes.ecs.components.VelocityComponent;
import smashdudes.ecs.events.Event;
import smashdudes.ecs.events.LandingEvent;

public class AirIdleState extends State
{
    private boolean beginFall = false;
    public AirIdleState(Entity entity)
    {
        super(entity);
    }

    // works in parent class? animation component not guaranteed for stateful entities
    @Override
    public void onEnter(float dt)
    {
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
        CharacterInputComponent ci = entity.getComponent(CharacterInputComponent.class);
        VelocityComponent v = entity.getComponent(VelocityComponent.class);
        AnimationContainerComponent container = entity.getComponent(AnimationContainerComponent.class);

        if(!beginFall && v.velocity.y <= 0)
        {
            entity.removeComponent(AnimationComponent.class);
            entity.addComponent(container.get(this.getClass()).getAnimation(1)); // ew
            beginFall = true;
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
    public State handleEvent(Event event)
    {
        if (event instanceof LandingEvent)
        {
            return new GroundIdleState(entity);
        }

        return this;
    }
}
