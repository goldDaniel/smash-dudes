package smashdudes.ecs.systems;

import com.badlogic.gdx.utils.Array;
import smashdudes.ecs.Component;
import smashdudes.ecs.Engine;
import smashdudes.ecs.Entity;
import smashdudes.ecs.events.Event;

public abstract class ISystem
{
    private final Array<Class<? extends Component>> components = new Array<>();
    private final Array<Class<? extends Event>> events = new Array<>();
    private boolean enabled = true;

    protected final Engine engine;

    public ISystem(Engine engine)
    {
        this.engine = engine;
    }

    public void registerComponentType(Class<? extends Component> clazz)
    {
        if(components.contains(clazz, true)) throw new IllegalStateException("System already has component registered");

        components.add(clazz);
    }

    public void registerEventType(Class<? extends Event> clazz)
    {
        if(events.contains(clazz, true)) throw new IllegalStateException("System already has event registered");

        events.add(clazz);
    }

    public final void receiveEvent(Event event)
    {
        if(events.contains(event.getClass(), true))
        {
            handleEvent(event);
        }
    }

    protected final Array<Entity> getEntities()
    {
        return engine.getEntities(components);
    }

    protected void handleEvent(Event event) {}

    public boolean isEnabled()
    {
        return enabled;
    }

    public void setEnabled(boolean value)
    {
        enabled = value;
    }
}
