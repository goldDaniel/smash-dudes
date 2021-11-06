package smashdudes.ecs.components;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.utils.Array;
import smashdudes.ecs.Component;



public class AnimationComponent extends Component
{
    public static class AnimationFrame
    {
        public final Texture texture;

        public AnimationFrame(Texture texture)
        {
            this.texture = texture;
        }
    }

    public float currentTime;
    public final Animation<AnimationFrame> currentAnimation;

    public AnimationComponent(Array<AnimationFrame> frames)
    {
        currentAnimation = new Animation<>( 1f/8f, frames, Animation.PlayMode.LOOP);
    }
}
