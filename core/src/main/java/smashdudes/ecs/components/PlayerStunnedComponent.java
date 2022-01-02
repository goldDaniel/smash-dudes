package smashdudes.ecs.components;

import smashdudes.ecs.Component;

public class PlayerStunnedComponent extends Component
{

    public final float stunnedTime;

    private float stunTimer = 0;

    public PlayerStunnedComponent(float stunnedTime)
    {
        this.stunnedTime = stunnedTime;
    }

    public void update(float dt)
    {
        stunTimer += dt;
    }

    public boolean isStunned()
    {
        return stunTimer < stunnedTime;
    }
}
