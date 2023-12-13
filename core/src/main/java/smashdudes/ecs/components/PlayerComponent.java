package smashdudes.ecs.components;

import smashdudes.core.PlayerHandle;
import smashdudes.ecs.Component;
import smashdudes.gameplay.PlayerState;

public class PlayerComponent extends Component
{
    public final PlayerHandle handle;
    public final String name;
    public int lives;

    public boolean facingLeft = false;

    public PlayerState currentState = PlayerState.Air_Idle;
    public PlayerComponent(PlayerHandle handle, String name)
    {
        this.handle = handle;
        this.name = name;
    }
}
