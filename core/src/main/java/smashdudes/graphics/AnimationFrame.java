package smashdudes.graphics;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import smashdudes.content.AnimationEvent;
import smashdudes.gameplay.AttackBox;
import smashdudes.gameplay.BodyBox;
import smashdudes.gameplay.CombatBox;

public class AnimationFrame
{
    public final Texture texture;

    public final Array<AttackBox> attackboxes;
    public final Array<BodyBox> bodyboxes;

    public final Array<AnimationEvent> events;


    public AnimationFrame(Texture texture, Array<AttackBox> attackboxes, Array<BodyBox> bodyboxes, Array<AnimationEvent> events)
    {
        this.texture = texture;
        this.attackboxes = attackboxes;
        this.bodyboxes = bodyboxes;
        this.events = events;
    }

    public void resetEvents()
    {
        for(AnimationEvent event : events)
        {
            event.eventFired = false;
        }
    }

    public Array<AttackBox> getAttackboxesRelativeTo(Vector2 pos, boolean mirrorX)
    {
        return getRelativeTo(attackboxes, pos, mirrorX);
    }

    public Array<BodyBox> getBodyboxesRelativeTo(Vector2 pos, boolean mirrorX)
    {
        return getRelativeTo(bodyboxes, pos, mirrorX);
    }

    private <T extends CombatBox> Array<T> getRelativeTo(Array<T> boxes, Vector2 pos, boolean mirrorX)
    {
        Array<T> result = new Array<>();

        for(T relative : boxes)
        {
            @SuppressWarnings("unchecked")
            T absolute = (T)relative.clone();

            absolute.width = relative.width;
            absolute.height = relative.height;

            int dir = mirrorX ? -1 : 1;

            absolute.x = pos.x + (dir * relative.x) - relative.width / 2;
            absolute.y = pos.y + relative.y - relative.height / 2;

            result.add(absolute);
        }

        return result;
    }
}
