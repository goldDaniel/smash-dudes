package smashdudes.gameplay.state.playerstate;

import smashdudes.ecs.Entity;
import smashdudes.ecs.components.AnimationComponent;
import smashdudes.ecs.components.AnimationContainerComponent;
import smashdudes.ecs.components.VelocityComponent;
import smashdudes.ecs.events.Event;
import smashdudes.ecs.events.RespawnEvent;
import smashdudes.ecs.events.StunnedEvent;
import smashdudes.gameplay.state.State;
import smashdudes.gameplay.state.playerstate.air.AirStunnedState;
import smashdudes.gameplay.state.playerstate.air.RespawnState;
import smashdudes.gameplay.state.playerstate.ground.GroundStunnedState;

public abstract class PlayerState extends State
{
    public PlayerState(Entity entity)
    {
        super(entity);
    }

    @Override
    public void onEnter()
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
            float velocityY = se.entity.getComponent(VelocityComponent.class).velocity.y;
            if(Math.abs(velocityY) > 0)
            {
                return new AirStunnedState(entity, se.stunTimer);
            }
            else
            {
                return new GroundStunnedState(entity, se.stunTimer);
            }
        }
        else if (event instanceof RespawnEvent)
        {
            return new RespawnState(entity);
        }

        return this;
    }

    public abstract boolean onGround();
}
