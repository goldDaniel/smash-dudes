package smashdudes.ecs.components;

import smashdudes.core.InputConfig;
import smashdudes.ecs.Component;

public class PlayerControllerComponent extends Component
{
    public InputConfig config;

    public PlayerControllerComponent(InputConfig config)
    {
        this.config = config;
    }
}
