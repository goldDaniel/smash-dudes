package smashdudes.ecs;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.Pool;

public class Entity implements Pool.Poolable
{
    private static Pool<Entity> entityPool = new Pool<Entity>()
    {
        @Override
        protected Entity newObject()
        {
            return new Entity();
        }
    };

    private static int nextID = 1;

    private int componentSignature;

    private ObjectMap<Class<? extends Component>, Component> components = new ObjectMap<>();
    public final int ID = nextID++;

    protected static Entity create()
    {
        Entity result = entityPool.obtain();
        result.components.clear();
        return result;
    }

    protected static void destroy(Entity entity)
    {
        entityPool.free(entity);
        entity.components.clear();
    }
    protected static void destroy(Array<Entity> entities)
    {
        entityPool.freeAll(entities);
        for (Entity entity : entities)
        {
            entity.components.clear();
        }
    }

    private Entity() {}


    public<T extends Component> T removeComponent(Class<T> clazz)
    {
        T component = (T)components.get(clazz);
        components.remove(clazz);
        return component;
    }

    public Array<Component> removeAllOtherComponents(Class<? extends Component>... componentsToKeep)
    {
        return removeAllOtherComponents(new Array<>(componentsToKeep));
    }

    public Array<Component> removeAllOtherComponents(Array<Class<? extends Component>> componentsToKeep)
    {
        Array<Component> result = new Array<>();
        Array<Class<? extends Component>> toRemove = new Array<>();

        for(ObjectMap.Entry entry : components)
        {
            if(!componentsToKeep.contains((Class<? extends Component>) entry.key, true))
            {
                toRemove.add((Class<? extends Component>) entry.key);
            }
        }

        for(Class<? extends Component> clazz : toRemove)
        {
            result.add(this.removeComponent(clazz));
        }

        return result;
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

    public boolean hasComponent(Class<? extends Component> clazz)
    {
        return components.containsKey(clazz);
    }

    public boolean hasComponent(Array<Class<? extends Component>> clazz)
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

    @Override
    public void reset()
    {
        components.clear();
    }
}
