package smashdudes.ecs.components;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import smashdudes.content.DTO;
import smashdudes.ecs.Component;
import com.badlogic.gdx.graphics.g2d.Animation;

public class AnimationDebugComponent extends Component
{
    public float currentTime;

    public Animation<Array<Rectangle>> currentHitboxes;
    public Animation<Array<Rectangle>> currentHurtboxes;

    public AnimationDebugComponent(Array<Array<Rectangle>> hitboxes,  Array<Array<Rectangle>> hurtboxes)
    {
        currentHitboxes = new Animation<>(1f / 8f, hitboxes, Animation.PlayMode.LOOP);
        currentHurtboxes = new Animation<>(1f / 8f, hurtboxes, Animation.PlayMode.LOOP);
    }
}
