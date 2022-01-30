package smashdudes.ecs.components;

import smashdudes.ecs.Component;

public class PlayerResetComponent extends Component
{
    public final float maxDuration;
    public float currDuration;

    public PlayerResetComponent(float maxDuration)
    {
        this.maxDuration = maxDuration;
        currDuration = 0;
    }
}
