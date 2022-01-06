package smashdudes.ecs;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Queue;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import smashdudes.ecs.events.Event;
import smashdudes.ecs.systems.*;
import smashdudes.graphics.RenderResources;

public class Engine
{
    private final Array<Entity> activeEntities = new Array<>();
    private final Array<Entity> createdEntities = new Array<>();
    private final Array<Entity> deadEntities = new Array<>();

    private final Array<GameSystem> systems = new Array<>();

    private final Queue<Event> events = new Queue<>();

    private boolean isUpdating = false;

    private final RenderDebugSystem drs;
    private final RenderSystem rs;

    public Engine()
    {
        rs = new RenderSystem(this, RenderResources.getSpriteBatch(), RenderResources.getFont());
        drs = new RenderDebugSystem(this, RenderResources.getShapeRenderer());


        systems.add(new PlayerIdleSystem(this));
        systems.add(new PlayerRunningSystem(this));
        systems.add(new PlayerInAirSystem(this));
        systems.add(new GroundAttackSystem(this));
        systems.add(new PlayerLandingSystem(this));
        systems.add(new RenderDirectionSystem(this));
        systems.add(new PlayerControllerSystem(this));
        systems.add(new AIControllerSystem(this));
        systems.add(new GravitySystem(this));
        systems.add(new MovementSystem(this));
        systems.add(new TerrainCollisionSystem(this));
        systems.add(new HitDetectionSystem(this));
        systems.add(new HitResolutionSystem(this));
        systems.add(new PlayerStunnedSystem(this));
        systems.add(new PlayerResetSystem(this));
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

        if(isUpdating)
        {
            createdEntities.add(e);
        }
        else
        {
            activeEntities.add(e);
        }

        return e;
    }

    public void destroyEntity(Entity entity)
    {
        if(isUpdating)
        {
            deadEntities.add(entity);
        }
        else
        {
            activeEntities.removeValue(entity, true);
        }
    }

    public Array<Entity> getEntities(boolean includeDisabled, Class<? extends Component>... components)
    {
        return getEntities(includeDisabled, new Array<>(components));
    }

    public Array<Entity> getEntities(boolean includeDisabled, Array<Class<? extends Component>> components)
    {
        Array<Entity> result = new Array<>();

        for(Entity entity : activeEntities)
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
        isUpdating = true;
        {
            for (GameSystem s : systems)
            {
                s.update(dt);
            }

            while (events.notEmpty())
            {
                Event e = events.removeFirst();
                for (GameSystem s : systems)
                {
                    s.receiveEvent(e);
                }
            }
        }
        isUpdating = false;

        activeEntities.removeAll(deadEntities, true);
        deadEntities.clear();

        activeEntities.addAll(createdEntities);
        createdEntities.clear();
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
