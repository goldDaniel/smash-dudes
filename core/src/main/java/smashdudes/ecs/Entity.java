package smashdudes.ecs;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ArrayMap;
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
    public final int ID = nextID++;

    protected static Entity create()
    {
        return entityPool.obtain();
    }

    protected static void destroy(Entity entity)
    {
        entityPool.free(entity);
    }
    protected static void destroy(Array<Entity> entity)
    {
        entityPool.freeAll(entity);
    }

    private Entity() {}

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
