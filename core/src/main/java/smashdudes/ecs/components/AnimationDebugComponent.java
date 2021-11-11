package smashdudes.ecs.components;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import smashdudes.content.DTO;
import smashdudes.ecs.Component;
import com.badlogic.gdx.graphics.g2d.Animation;

public class AnimationDebugComponent extends Component
{
    public float currentTime;

    public Array<Array<Rectangle>> hitboxes;
    public Array<Array<Rectangle>> hurtboxes;

    public Array<Animation<Rectangle>> currentHitboxes = new Array<>();
    public Array<Animation<Rectangle>> currentHurtboxes = new Array<>();

    public AnimationDebugComponent(Array<Array<Rectangle>> hitboxes,  Array<Array<Rectangle>> hurtboxes)
    {
        this.hitboxes = hitboxes;
        this.hurtboxes = hurtboxes;

        for (Array<Rectangle> hitbox : hitboxes)
        {
            currentHitboxes.add(new Animation<>(1f / 8f, hitbox, Animation.PlayMode.LOOP));
        }

        for (Array<Rectangle> hurtbox : hurtboxes)
        {
            currentHurtboxes.add(new Animation<>(1f / 8f, hurtbox, Animation.PlayMode.LOOP));
        }
    }
}
