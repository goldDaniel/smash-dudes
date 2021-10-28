package smashdudes.ecs.components;

import smashdudes.ecs.Component;

public class GravityComponent extends Component
{
    public float gravityStrength;

    public GravityComponent(float gravityStrength)
    {
        this.gravityStrength = gravityStrength;
    }
}
