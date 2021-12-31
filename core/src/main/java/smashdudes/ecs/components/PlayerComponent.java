package smashdudes.ecs.components;

import smashdudes.core.PlayerHandle;
import smashdudes.ecs.Component;

public class PlayerComponent extends Component
{
    public final PlayerHandle handle;
    public final String identifier;

    public boolean facingLeft = false;

    public PlayerComponent(PlayerHandle handle, String id)
    {
        this.handle = handle;
        this.identifier = id;
    }
}
