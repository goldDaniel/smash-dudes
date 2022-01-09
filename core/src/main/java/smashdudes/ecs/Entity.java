package smashdudes.ecs;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ArrayMap;
import com.badlogic.gdx.utils.ObjectMap;

public class Entity
{
    private static int nextID = 1;
    public final int ID = nextID++;

    protected Entity() {}

    private ObjectMap<Class<? extends Component>, Component> components = new ObjectMap<>();

    public<T extends Component> void removeComponent(Class<T> clazz)
    {
        components.remove(clazz);
    }

    public void addComponent(Component c)
    {
        if(components.containsKey(c.getClass()))
        {
            throw new IllegalStateException("Component already exists on entity!");
        }

        components.put(c.getClass(), c);
    }

    public<T extends Component> T getComponent(Class<T> clazz)
    {
        return (T)components.get(clazz);
    }

    public boolean hasComponent(Class<? extends Component>... clazz)
    {
        for(Class<? extends Component> c : clazz)
        {
            if(!components.containsKey(c))
            {
                return false;
            }
        }
        return true;
    }
}
