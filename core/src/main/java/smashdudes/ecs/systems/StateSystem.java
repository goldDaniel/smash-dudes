package smashdudes.ecs.systems;

import smashdudes.core.state.playerstate.AirIdleState;
import smashdudes.core.state.playerstate.GroundIdleState;
import smashdudes.ecs.Engine;
import smashdudes.ecs.Entity;
import smashdudes.ecs.components.StateComponent;
import smashdudes.ecs.events.Event;
import smashdudes.ecs.events.LandingEvent;

public class StateSystem extends GameSystem
{
    public StateSystem(Engine engine)
    {
        super(engine);
        registerComponentType(StateComponent.class);
        registerEventType(LandingEvent.class);
    }
    @Override
    public void updateEntity(Entity entity, float dt)
    {
        StateComponent s = entity.getComponent(StateComponent.class);
        s.state.update(dt);
        Event event = s.state.popEvent();
        while (event != null)
        {
            engine.addEvent(event);
            event = s.state.popEvent();
        }
        s.setNextState(s.state.getNextState());
    }

    @Override
    protected void handleEvent(Event event)
    {
        Entity entity = event.entity;
        StateComponent s = entity.getComponent(StateComponent.class);
        s.handleEvent(event);
    }
}
