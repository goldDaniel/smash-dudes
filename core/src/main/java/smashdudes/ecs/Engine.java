package smashdudes.ecs;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Queue;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import smashdudes.ecs.components.CameraComponent;
import smashdudes.ecs.components.CountdownComponent;
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

    private final RenderSystem rs;
    private final RenderDebugSystem drs;
    private final UIRenderSystem urs;

    public Engine(GameOverSystem.IScreenTransition transition)
    {
        int WORLD_WIDTH = 20;
        int WORLD_HEIGHT = 12;

        Entity camEntity = createEntity();
        CameraComponent cam = new CameraComponent();
        camEntity.addComponent(new CountdownComponent(3));
        camEntity.addComponent(cam);

        OrthographicCamera camera = new OrthographicCamera(WORLD_WIDTH, WORLD_HEIGHT);
        cam.camera = camera;
        camera.zoom = 5f;

        ExtendViewport viewport = new ExtendViewport(WORLD_WIDTH,WORLD_HEIGHT, camera);

        rs = new RenderSystem(this, RenderResources.getSpriteBatch());
        drs = new RenderDebugSystem(this, RenderResources.getShapeRenderer());
        urs = new UIRenderSystem(this, RenderResources.getSpriteBatch(), RenderResources.getFont("crimes", 24));


        PlayerControllerSystem ctrlSys = new PlayerControllerSystem(this);
        ctrlSys.setEnabled(false);

        systems.add(new CountdownSystem(this));
        systems.add(new PlayerIdleSystem(this));
        systems.add(new PlayerRunningSystem(this));
        systems.add(new PlayerInAirSystem(this));
        systems.add(new GroundAttackSystem(this));
        systems.add(new PlayerLandingSystem(this));
        systems.add(new RenderDirectionSystem(this));
        systems.add(ctrlSys);
        systems.add(new AIControllerSystem(this));
        systems.add(new GravitySystem(this));
        systems.add(new MovementSystem(this));
        systems.add(new TerrainCollisionSystem(this));
        systems.add(new HitDetectionSystem(this));
        systems.add(new HitResolutionSystem(this));
        systems.add(new PlayerStunnedSystem(this));
        systems.add(new DeathSystem(this));
        systems.add(new RespawnSystem(this));
        systems.add(new AudioSystem(this));

        systems.add(new AnimationSystem(this));
        systems.add(new ParticleSystem(this));
        systems.add(new ParticleEmitterSystem(this));
        systems.add(new AnimationDebugSystem(this));

        systems.add(new GameOverSystem(this, transition));


        rs.setCamera(camera);
        drs.setCamera(camera);

        rs.setViewport(viewport);
        drs.setViewport(viewport);

        systems.add(new CountdownCameraSystem(this));
        systems.add(new AveragePositionCameraSystem(this));
        systems.add(rs);
        systems.add(drs);
        systems.add(urs);
    }

    public Entity createEntity()
    {
        Entity e = Entity.create();

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
        if(!activeEntities.contains(entity, true)) throw new IllegalArgumentException("Entity is not in ECS!!");

        if(isUpdating)
        {
            deadEntities.add(entity);
        }
        else
        {
            activeEntities.removeValue(entity, true);
        }
    }
    
    public Array<Entity> getEntities(Class<? extends Component>... components)
    {
        return getEntities(new Array<>(components));
    }

    public Array<Entity> getEntities(Array<Class<? extends Component>> components)
    {
        Array<Entity> result = new Array<>();

        for(Entity entity : activeEntities)
        {
            if(entity.hasComponent(components))
            {
                result.add(entity);
            }
        }

        return result;
    }

    public void update(float dt)
    {
        isUpdating = true;
        {
            for (int i = 0; i < systems.size; i++)
            {
                if(systems.get(i).isEnabled())
                {
                    systems.get(i).update(dt);
                }
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
        Entity.destroy(deadEntities);
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
        urs.resize(w, h);
    }

    public <T extends GameSystem> void enableSystem(Class<T> clazz)
    {
        for(GameSystem system : systems)
        {
            if(system.getClass().equals(clazz))
            {
                system.setEnabled(true);
                return;
            }
        }
    }

    public <T extends GameSystem> void disableSystem(Class<T> clazz)
    {
        for(GameSystem system : systems)
        {
            if(system.getClass().equals(clazz))
            {
                system.setEnabled(false);
                return;
            }
        }
    }
}
