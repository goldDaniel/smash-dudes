package smashdudes.ecs.components;

import com.badlogic.gdx.utils.ObjectMap;
import smashdudes.ecs.Component;

public class AnimationContainerComponent<T> extends Component
{
    private AnimationComponent defaultComponent;

    private final ObjectMap<Class<? extends T>, AnimationComponent> animationMap = new ObjectMap<>();

    public AnimationComponent get(Class<? extends T> clazz)
    {
        return animationMap.get(clazz);
    }

    public void put(Class<? extends T> clazz, AnimationComponent comp)
    {
        animationMap.put(clazz, comp);
    }

    public AnimationComponent getDefault()
    {
        return defaultComponent;
    }

    public void setDefault(Class<? extends T> clazz)
    {
        defaultComponent = animationMap.get(clazz);
    }
}
