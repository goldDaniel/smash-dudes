package smashdudes.ecs.systems;

import smashdudes.ecs.Engine;
import smashdudes.ecs.Entity;
import smashdudes.ecs.components.StateComponent;
import smashdudes.ecs.events.*;

public class StateSystem extends GameSystem
{
    public StateSystem(Engine engine)
    {
        super(engine);
        registerComponentType(StateComponent.class);
        registerEventType(StateEvent.class);
    }
    @Override
    public void updateEntity(Entity entity, float dt)
    {
        StateComponent s = entity.getComponent(StateComponent.class);
        s.state.update(dt);
        s.setNextState(s.state.getNextState());
    }

    @Override
    protected void handleEvent(Event event)
    {
        StateEvent stateEvent = (StateEvent)event;
        event = stateEvent.event;

        Entity entity = event.entity;
        StateComponent s = entity.getComponent(StateComponent.class);
        s.handleEvent(event);
    }
}
