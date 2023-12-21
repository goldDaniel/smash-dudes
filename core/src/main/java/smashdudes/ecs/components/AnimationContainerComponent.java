package smashdudes.ecs.components;

import com.badlogic.gdx.utils.ObjectMap;
import smashdudes.core.AnimationSequence;
import smashdudes.core.state.State;
import smashdudes.ecs.Component;



public class AnimationContainerComponent extends Component
{
    private AnimationSequence defaultSequence = null;
    private final ObjectMap<Class<?>, AnimationSequence> animationSequenceMap = new ObjectMap<>();

    public AnimationSequence get(Class<?> clazz)
    {
        return animationSequenceMap.get(clazz);
    }

    public void put(Class<?> clazz, AnimationSequence seq)
    {
        animationSequenceMap.put(clazz, seq);
    }

    public AnimationSequence get()
    {
        return defaultSequence;
    }

    public void setDefault(Class<?> clazz)
    {
        defaultSequence = animationSequenceMap.get(clazz);
    }
}
