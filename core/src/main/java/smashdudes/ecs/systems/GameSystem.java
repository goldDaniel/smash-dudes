package smashdudes.ecs.systems;

import com.badlogic.gdx.utils.Array;
import smashdudes.ecs.Component;
import smashdudes.ecs.Engine;
import smashdudes.ecs.Entity;
import smashdudes.ecs.events.Event;

public abstract class GameSystem
{
    private final Array<Class<? extends Component>> components = new Array<>();
    private final Array<Class<? extends Event>> events = new Array<>();

    protected final Engine engine;

    public GameSystem(Engine engine)
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

    public final void update(float dt)
    {
        preUpdate();

        for(Entity e : engine.getEntities(components))
        {
            updateEntity(e, dt);
        }

        postUpdate();
    }

    public final void receiveEvent(Event event)
    {
        if(events.contains(event.getClass(), true))
        {
            if(engine.getEntities(components, true).contains(event.entity, true))
            {
                handleEvent(event);
            }
        }
    }

    protected void handleEvent(Event event) {}

    protected void preUpdate() {}

    protected abstract void updateEntity(Entity entity, float dt);

    protected void postUpdate() {}
}
