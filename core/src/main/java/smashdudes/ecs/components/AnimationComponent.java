package smashdudes.ecs.components;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import smashdudes.ecs.Component;



public class AnimationComponent extends Component
{
    public static class AnimationFrame
    {
        public final Texture texture;

        public final Array<Rectangle> hitboxes;
        public final Array<Rectangle> hurtboxes;

        public AnimationFrame(Texture texture, Array<Rectangle> hitboxes, Array<Rectangle> hurtboxes)
        {
            this.texture = texture;
            this.hitboxes = hitboxes;
            this.hurtboxes = hurtboxes;
        }
    }

    public float currentTime;
    public final Animation<AnimationFrame> currentAnimation;
    public float animationDuration;

    public AnimationComponent(Array<AnimationFrame> frames, float animationDuration)
    {
        float frameDuration = animationDuration / frames.size;
        currentAnimation = new Animation<>( frameDuration, frames, Animation.PlayMode.LOOP);
    }
}
