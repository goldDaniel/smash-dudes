package smashdudes.ecs.components;

import smashdudes.core.PlayerHandle;
import smashdudes.ecs.Component;

public class PlayerComponent extends Component
{
    public final PlayerHandle handle;
    public boolean facingLeft = false;

    public PlayerComponent(PlayerHandle handle)
    {
        this.handle = handle;
    }
}
