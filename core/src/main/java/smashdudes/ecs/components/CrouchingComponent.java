package smashdudes.ecs.components;

import smashdudes.ecs.Component;

public class CrouchingComponent extends Component
{
    public float ratio;

    public CrouchingComponent(float ratio)
    {
        this.ratio = ratio;
    }
}
