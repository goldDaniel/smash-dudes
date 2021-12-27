package smashdudes.ecs.components;

import smashdudes.core.input.IGameInputRetriever;
import smashdudes.ecs.Component;

public class PlayerControllerComponent extends Component
{
    public final IGameInputRetriever retriever;

    public PlayerControllerComponent(IGameInputRetriever config)
    {
        this.retriever = config;
    }
}
