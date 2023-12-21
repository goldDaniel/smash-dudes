package smashdudes.ecs.components;

import smashdudes.core.PlayerHandle;
import smashdudes.ecs.Component;

public class PlayerComponent extends Component
{
    public final PlayerHandle handle;
    public final String name;
    public int lives;

    public boolean facingLeft = false;

    public PlayerComponent(PlayerHandle handle, String name)
    {
        this.handle = handle;
        this.name = name;
    }
}
