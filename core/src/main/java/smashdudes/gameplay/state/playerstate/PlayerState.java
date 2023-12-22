package smashdudes.gameplay.state.playerstate;

import smashdudes.ecs.Entity;
import smashdudes.ecs.components.AnimationComponent;
import smashdudes.ecs.components.AnimationContainerComponent;
import smashdudes.ecs.events.Event;
import smashdudes.ecs.events.RespawnEvent;
import smashdudes.ecs.events.StunnedEvent;
import smashdudes.gameplay.state.State;

public abstract class PlayerState extends State
{
    public PlayerState(Entity entity)
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
    public State handleEvent(Event event)
    {
        if (event instanceof StunnedEvent)
        {
            StunnedEvent se = (StunnedEvent)event;
            return new StunnedState(entity, se.stunTimer);
        }
        else if (event instanceof RespawnEvent)
        {
            return new RespawnState(entity);
        }

        return this;
    }
}
