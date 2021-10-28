package smashdudes.ecs;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Queue;
import smashdudes.ecs.events.Event;
import smashdudes.ecs.systems.*;

public class Engine
{
    private Array<Entity> entities = new Array<>();
    private Array<GameSystem> systems = new Array<>();

    private Queue<Event> events = new Queue<>();

    private RenderSystem rs;

    public Engine()
    {
        rs = new RenderSystem(this);

        systems.add(new InputSystem(this));
        systems.add(new GravitySystem(this));
        systems.add(new JumpInputSystem(this));
        systems.add(new MovementSystem(this));
        systems.add(new TerrainCollisionSystem(this));
        systems.add(rs);
    }

    public Entity createEntity()
    {
        Entity e = new Entity();
        entities.add(e);

        return e;
    }

    public Array<Entity> getEntities(Array<Class<? extends Component>> components, boolean includeDisabled)
    {
        Array<Entity> result = new Array<>();

        for(Entity entity : entities)
        {
            boolean valid = true;
            for(Class<? extends Component> component : components)
            {
                Component comp = entity.getComponent(component);
                if(comp == null || (!includeDisabled && !comp.isEnabled()))
                {
                    valid = false;
                }
            }

            if(valid)
            {
                result.add(entity);
            }
        }

        return result;
    }


    public Array<Entity> getEntities(Array<Class<? extends Component>> components)
    {
        return getEntities(components, false);
    }

    public void update()
    {
        float dt = 1f/Gdx.graphics.getDisplayMode().refreshRate;

        for(GameSystem s : systems)
        {
            s.update(dt);
        }

        while(events.notEmpty())
        {
            Event e = events.removeFirst();
            for(GameSystem s : systems)
            {
                s.receiveEvent(e);
            }
        }
    }

    public void postEvent(Event event)
    {
        events.addLast(event);
    }


    public void resize(int w, int h)
    {
        rs.resize(w, h);
    }
}
