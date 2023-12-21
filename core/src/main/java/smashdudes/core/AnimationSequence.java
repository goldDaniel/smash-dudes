package smashdudes.core;

import com.badlogic.gdx.utils.Array;
import smashdudes.ecs.components.AnimationComponent;

public class AnimationSequence
{
    private final Array<AnimationComponent> anims = new Array<>();

    public AnimationSequence(AnimationComponent... anims)
    {
        for(AnimationComponent anim: anims)
        {
            this.anims.add(anim);
        }
    }

    public AnimationComponent getAnimation(int i)
    {
        if(anims.notEmpty())
        {
            return anims.get(i);
        }

        return null;
    }

    public void reset()
    {
        for(AnimationComponent anim: anims)
        {
            anim.reset();
        }
    }
}
