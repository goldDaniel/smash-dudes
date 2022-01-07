package smashdudes.ecs.components;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.utils.Array;
import smashdudes.ecs.Component;
import smashdudes.graphics.AnimationFrame;


public class AnimationComponent extends Component
{


    private float currentTime;
    private final Animation<AnimationFrame> currentAnimation;


    public AnimationComponent(Array<AnimationFrame> frames, float animationDuration, Animation.PlayMode mode)
    {
        float frameDuration = animationDuration / frames.size;
        currentAnimation = new Animation<>( frameDuration, frames, mode);
    }

    public AnimationFrame getCurrentFrame()
    {
        return currentAnimation.getKeyFrame(currentTime);
    }

    public void reset()
    {
        currentTime = 0;
    }

    public void update(float dt)
    {
        currentTime += dt;
    }

    public boolean isFinished()
    {
        return currentAnimation.isAnimationFinished(currentTime);
    }
}
