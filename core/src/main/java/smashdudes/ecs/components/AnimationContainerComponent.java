package smashdudes.ecs.components;

import com.badlogic.gdx.utils.ObjectMap;
import smashdudes.core.AnimationSequence;
import smashdudes.core.state.State;
import smashdudes.ecs.Component;



public class AnimationContainerComponent extends Component
{
    private final ObjectMap<Class<? extends State>, AnimationSequence> animationSequenceMap = new ObjectMap<>();

    public AnimationSequence get(Class<? extends State> clazz)
    {
        return animationSequenceMap.get(clazz);
    }

    public void put(Class<? extends State> clazz, AnimationSequence seq)
    {
        animationSequenceMap.put(clazz, seq);
    }
}
