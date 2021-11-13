package smashdudes.ecs;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Queue;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import smashdudes.core.RenderResources;
import smashdudes.ecs.events.Event;
import smashdudes.ecs.systems.*;

public class Engine
{
    private Array<Entity> entities = new Array<>();
    private Array<GameSystem> systems = new Array<>();

    private Queue<Event> events = new Queue<>();

    private RenderDebugSystem drs;
    private RenderSystem rs;

    public Engine()
    {
        rs = new RenderSystem(this, RenderResources.getSpriteBatch());
        drs = new RenderDebugSystem(this, RenderResources.getShapeRenderer());

        systems.add(new CharacterInputSystem(this));
        systems.add(new CharacterJumpInputSystem(this));
        systems.add(new PlayerControllerSystem(this));
        systems.add(new AIControllerSystem(this));
        systems.add(new GravitySystem(this));
        systems.add(new MovementSystem(this));
        systems.add(new TerrainCollisionSystem(this));
        systems.add(new AudioSystem(this));

        systems.add(new AnimationSystem(this));
        systems.add(new AnimationDebugSystem(this));

        int WORLD_WIDTH = 20;
        int WORLD_HEIGHT = 12;

        OrthographicCamera camera = new OrthographicCamera(WORLD_WIDTH, WORLD_HEIGHT);
        camera.zoom = 1.2f;

        ExtendViewport viewport = new ExtendViewport(WORLD_WIDTH,WORLD_HEIGHT, camera);

        CameraSystem cs = new CameraSystem(this);
        cs.setCamera(camera);
        drs.setCamera(camera);
        rs.setCamera(camera);

        drs.setViewport(viewport);
        rs.setViewport(viewport);

        systems.add(cs);
        systems.add(rs);
        systems.add(drs);
    }

    public Entity createEntity()
    {
        Entity e = new Entity();
        entities.add(e);

        return e;
    }

    public Array<Entity> getEntities(boolean includeDisabled, Class<? extends Component>... components)
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

    public Array<Entity> getEntities(boolean includeDisabled, Array<Class<? extends Component>> components)
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

    public Array<Entity> getEntities(Class<? extends Component>... components)
    {
        return getEntities(false, components);
    }

    public Array<Entity> getEntities(Array<Class<? extends Component>> components)
    {
        return getEntities(false, components);
    }

    public void update(float dt)
    {
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

    public void addEvent(Event event)
    {
        events.addLast(event);
    }

    public void resize(int w, int h)
    {
        rs.resize(w, h);
        drs.resize(w, h);
    }
}
