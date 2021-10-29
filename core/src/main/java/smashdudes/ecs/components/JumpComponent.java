package smashdudes.ecs.components;

import smashdudes.ecs.Component;

public class JumpComponent extends Component
{
    public float jumpStrength;

    public float extraJumpStrength;

    public int maxJumps;

    public int remainingJumps;

    public JumpComponent(){}

    public JumpComponent(float s) {jumpStrength = s; }

    public JumpComponent(float s, float es, int n)
    {
        jumpStrength = s;
        extraJumpStrength = es;
        maxJumps = n;
        remainingJumps = maxJumps;
    }
}
