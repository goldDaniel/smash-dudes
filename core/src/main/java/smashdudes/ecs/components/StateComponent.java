package smashdudes.ecs.components;

import smashdudes.core.state.State;
import smashdudes.ecs.Component;

public class StateComponent extends Component
{
    public State state;

    public StateComponent(State state)
    {
        this.state = state;
    }

    public void setNextState(State next)
    {
        state.onExit();
        state = next;
    }
}
