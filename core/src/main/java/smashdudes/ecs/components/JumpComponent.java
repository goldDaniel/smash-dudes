package smashdudes.ecs.components;

import smashdudes.ecs.Component;

public class JumpComponent extends Component
{
    public float jumpStrength;

    public JumpComponent(){}

    public JumpComponent(float s) {jumpStrength = s; }
}
