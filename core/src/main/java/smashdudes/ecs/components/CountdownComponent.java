package smashdudes.ecs.components;

import smashdudes.ecs.Component;

public class CountdownComponent extends Component
{
    public final float maxDuration;
    public float currDuration;


    public CountdownComponent(float maxDuration)
    {
        this.maxDuration = maxDuration;
        currDuration = maxDuration;
    }
}
