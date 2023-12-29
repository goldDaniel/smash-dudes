package smashdudes.ecs.components;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.utils.Array;
import smashdudes.ecs.Component;
import smashdudes.graphics.AnimationFrame;


public class AnimationComponent extends Component
{
    private float currentTime;
    private final Animation currentAnimation;


    public AnimationComponent(Array frames, float animationDuration, Animation.PlayMode mode)
    {
        float frameDuration = animationDuration / frames.size;
        currentAnimation = new Animation<>( frameDuration, frames, mode);
    }

    public AnimationFrame getCurrentFrame()
    {
        return (AnimationFrame)currentAnimation.getKeyFrame(currentTime);
    }

    public void reset()
    {
        currentTime = 0;
        for(Object obj : currentAnimation.getKeyFrames())
        {
            AnimationFrame frame = (AnimationFrame)obj;
            frame.resetEvents();
        }
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
