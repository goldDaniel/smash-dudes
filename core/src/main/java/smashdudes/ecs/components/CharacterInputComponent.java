package smashdudes.ecs.components;

import smashdudes.core.KeyState;
import smashdudes.ecs.Component;

public class CharacterInputComponent extends Component
{
    public KeyState previousState = new KeyState();
    public KeyState currentState = new KeyState();
}
