package smashdudes.ecs.components;

import smashdudes.core.input.InputState;
import smashdudes.ecs.Component;

public class CharacterInputComponent extends Component
{
    public InputState previousState = new InputState();
    public InputState currentState = new InputState();
}
