package smashdudes.ecs.components;

import smashdudes.gameplay.state.State;
import smashdudes.ecs.Component;
import smashdudes.ecs.events.Event;

public class StateComponent extends Component
{
    public State state;
    public final State defaultState;

    public StateComponent(State state)
    {
        this.state = state;
        this.defaultState = state;
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
