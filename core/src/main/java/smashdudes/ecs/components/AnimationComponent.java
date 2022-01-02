package smashdudes.ecs.components;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
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

        public Array<Rectangle> getHitboxesRelativeTo(Vector2 pos, boolean mirror)
        {
            return getRelativeTo(hitboxes, pos, mirror);
        }

        public Array<Rectangle> getHurtboxesRelativeTo(Vector2 pos, boolean mirror)
        {
            return getRelativeTo(hurtboxes, pos, mirror);
        }

        private Array<Rectangle> getRelativeTo(Array<Rectangle> boxes, Vector2 pos, boolean mirror)
        {
            Array<Rectangle> result = new Array<>();

            int dir = mirror ? -1 : 1;

            for(Rectangle relative : boxes)
            {
                Rectangle absolute = new Rectangle();
                absolute.width = dir * relative.width;
                absolute.height = relative.height;
                absolute.x = dir * (relative.x - relative.width / 2) + pos.x;
                absolute.y = (relative.y - relative.height / 2) + pos.y;

                result.add(absolute);
            }

            return result;
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
