package smashdudes.ecs;

import com.badlogic.gdx.Gdx;
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

    private final Array<GameSystem> gameSystems = new Array<>();
    private final Array<GameSystem> renderSystems = new Array<>();

    private final Queue<Event> events = new Queue<>();

    private boolean isUpdating = false;

    private final RenderSystem rs;
    private final RenderDebugSystem drs;
    private final UIRenderSystem urs;

    public Engine(int worldWidth, int worldHeight, GameOverSystem.IScreenTransition transition)
    {
        Entity camEntity = createEntity();
        CameraComponent cam = new CameraComponent();
        camEntity.addComponent(new CountdownComponent(3));
        camEntity.addComponent(cam);

        OrthographicCamera camera = new OrthographicCamera(worldWidth, worldHeight);
        cam.camera = camera;
        camera.zoom = 5f;

        ExtendViewport viewport = new ExtendViewport(worldWidth,worldHeight, camera);

        rs = new RenderSystem(this, RenderResources.getSpriteBatch());
        drs = new RenderDebugSystem(this, RenderResources.getShapeRenderer());
        urs = new UIRenderSystem(this, RenderResources.getSpriteBatch(), RenderResources.getFont("KeepOnTruckin", 24));


        PlayerControllerSystem ctrlSys = new PlayerControllerSystem(this);
        ctrlSys.setEnabled(false);

        gameSystems.add(new CountdownSystem(this));
        gameSystems.add(new PlayerIdleSystem(this));
        gameSystems.add(new PlayerRunningSystem(this));
        gameSystems.add(new PlayerInAirSystem(this));
        gameSystems.add(new GroundAttackSystem(this));
        gameSystems.add(new PlayerLandingSystem(this));
        gameSystems.add(new RenderDirectionSystem(this));
        gameSystems.add(ctrlSys);
        gameSystems.add(new AIControllerSystem(this));
        gameSystems.add(new GravitySystem(this));
        gameSystems.add(new MovementSystem(this));
        gameSystems.add(new TerrainCollisionSystem(this));
        gameSystems.add(new HitDetectionSystem(this));
        gameSystems.add(new HitResolutionSystem(this));
        gameSystems.add(new PlayerStunnedSystem(this));
        gameSystems.add(new ProjectileSystem(this));
        gameSystems.add(new DeathSystem(this));
        gameSystems.add(new RespawnSystem(this));
        gameSystems.add(new AudioSystem(this));

        gameSystems.add(new AnimationSystem(this));
        gameSystems.add(new ParticleSystem(this));
        gameSystems.add(new ParticleEmitterSystem(this));
        gameSystems.add(new AnimationDebugSystem(this));

        gameSystems.add(new GameOverSystem(this, transition));


        rs.setCamera(camera);
        drs.setCamera(camera);

        rs.setViewport(viewport);
        drs.setViewport(viewport);

        gameSystems.add(new CountdownCameraSystem(this));
        gameSystems.add(new AveragePositionCameraSystem(this));

        BackgroundSystem backgroundSystem = new BackgroundSystem(this);
        backgroundSystem.setCamera(camera);
        gameSystems.add(backgroundSystem);

        renderSystems.add(rs);
        renderSystems.add(drs);
        renderSystems.add(urs);
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
            Entity.destroy(entity);
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
            for (int i = 0; i < gameSystems.size; i++)
            {
                if(gameSystems.get(i).isEnabled())
                {
                    gameSystems.get(i).update(dt);
                }
            }

            while (events.notEmpty())
            {
                Event e = events.removeFirst();
                for (GameSystem s : gameSystems)
                {
                    s.receiveEvent(e);
                }
                for (GameSystem s : renderSystems)
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

    public void render()
    {
        for (int i = 0; i < renderSystems.size; i++)
        {
            if(renderSystems.get(i).isEnabled())
            {
                renderSystems.get(i).update(Gdx.graphics.getDeltaTime());
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
        urs.resize(w, h);
    }

    public <T extends GameSystem> void enableSystem(Class<T> clazz)
    {
        for(GameSystem system : gameSystems)
        {
            if(system.getClass().equals(clazz))
            {
                system.setEnabled(true);
                return;
            }
        }

        for(GameSystem system : renderSystems)
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
        for(GameSystem system : gameSystems)
        {
            if(system.getClass().equals(clazz))
            {
                system.setEnabled(false);
                return;
            }
        }

        for(GameSystem system : renderSystems)
        {
            if(system.getClass().equals(clazz))
            {
                system.setEnabled(true);
                return;
            }
        }
    }
}
