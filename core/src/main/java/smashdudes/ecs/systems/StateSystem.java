package smashdudes.ecs.systems;

import smashdudes.ecs.Engine;
import smashdudes.ecs.Entity;
import smashdudes.ecs.components.StateComponent;
import smashdudes.ecs.events.Event;
import smashdudes.ecs.events.LandingEvent;
import smashdudes.ecs.events.StunnedEvent;

public class StateSystem extends GameSystem
{
    public StateSystem(Engine engine)
    {
        super(engine);
        registerComponentType(StateComponent.class);
        registerEventType(LandingEvent.class); // Todo (Nathan): use state events instead to avoid manually registering events
        registerEventType(StunnedEvent.class);
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
