package smashdudes.ecs.components;

import smashdudes.ecs.Component;

public class SprintingComponent extends Component
{
    public float percentFaster;

    public SprintingComponent(float p)
    {
        percentFaster = p;
    }
}
