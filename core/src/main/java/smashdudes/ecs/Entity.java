package smashdudes.ecs;

import com.badlogic.gdx.utils.Array;

public class Entity
{
    private static int nextID = 1;
    public final int ID = nextID++;

    protected Entity() {}

    private Array<Component> components = new Array<>();

    public<T extends Component> void removeComponent(Class<T> clazz)
    {
        //this is just for validation/testing to make sure we dont remove components the entity doesnt have
        //will be removed eventually because its slow
        for(Component other : components)
        {
            if(other.getClass() == clazz)
            {
                //we remove while iterating but thats ok because we exit immediately after
                components.removeValue(other, true);
                return;
            }
        }

        throw new IllegalStateException("Attempting to remove component entity doesnt have!");
    }

    public void addComponent(Component c)
    {
        //this is just for validation/testing to make sure we dont add more than one component
        //will be removed eventually because its slow
        for(Component other : components)
        {
            if(other.getClass() == c.getClass())
            {
                throw new IllegalStateException("Component already exists on entity!");
            }
        }

        components.add(c);
    }

    public<T extends Component> T getComponent(Class<T> clazz)
    {
        for(Component c : components)
        {
            if(c.getClass() == clazz)
            {
                return (T)c;
            }
        }

        return null;
    }

    public boolean hasComponent(Class<? extends Component>... clazz)
    {
        for(Class<? extends Component> c : clazz)
        {
            if(getComponent(c) == null)
            {
                return false;
            }
        }
        return true;
    }
}
