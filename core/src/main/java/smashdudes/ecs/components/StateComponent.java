package smashdudes.ecs.components;

import smashdudes.core.state.State;
import smashdudes.ecs.Component;
import smashdudes.ecs.events.Event;

public class StateComponent extends Component
{
    public State state;
    public final State defaultState;

    public StateComponent(State state, State defaultState)
    {
        this.state = state;
        this.defaultState = defaultState;
    }

    public void setNextState(State next)
    {
        if (next == state) return;

        state.onExit();
        state = next;
    }

    public void handleEvent(Event event)
    {
        setNextState(state.handleEvent(event));
    }
}
