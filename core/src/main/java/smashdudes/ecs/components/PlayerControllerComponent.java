package smashdudes.ecs.components;

import smashdudes.core.input.GameInputRetriever;
import smashdudes.core.input.InputConfig;
import smashdudes.ecs.Component;

public class PlayerControllerComponent extends Component
{
    public final GameInputRetriever retriever;

    public PlayerControllerComponent(GameInputRetriever config)
    {
        this.retriever = config;
    }
}
