package smashdudes.ecs.components;

import smashdudes.ecs.Component;

public class PlayerAnimationContainerComponent extends Component
{
    public AnimationComponent idle;
    public AnimationComponent running;
    public AnimationComponent jumping;
    public AnimationComponent falling;
}
