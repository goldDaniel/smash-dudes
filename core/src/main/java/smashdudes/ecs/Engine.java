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

    private final Array<GameSystem> gameSystems = new Array<>();
    private final Array<RenderSystem> renderSystems = new Array<>();

    private final Queue<Event> gameEvents = new Queue<>();
    private final Queue<Event> renderEvents = new Queue<>();

    private boolean isUpdating = false;

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

        RenderDrawSystem rs = new RenderDrawSystem(this, RenderResources.getSpriteBatch());
        RenderDebugSystem drs = new RenderDebugSystem(this, RenderResources.getShapeRenderer());
        UIRenderSystem urs = new UIRenderSystem(this, RenderResources.getSpriteBatch(), RenderResources.getFont("KeepOnTruckin", 24));

        rs.setCamera(camera);
        drs.setCamera(camera);

        rs.setViewport(viewport);
        drs.setViewport(viewport);

        PlayerControllerSystem ctrlSys = new PlayerControllerSystem(this);
        ctrlSys.setEnabled(false);

        gameSystems.add(new DebugResetSystem(this));
        gameSystems.add(new PreviousPositionSystem(this));
        gameSystems.add(new CountdownSystem(this));
        gameSystems.add(new StateSystem(this));
        gameSystems.add(new RenderDirectionSystem(this));
        gameSystems.add(ctrlSys);
        gameSystems.add(new AIControllerSystem(this));
        gameSystems.add(new GravitySystem(this));
        gameSystems.add(new MovementSystem(this));
        gameSystems.add(new TerrainCollisionSystem(this));
        gameSystems.add(new LandingSystem(this));
        gameSystems.add(new HitDetectionSystem(this));
        gameSystems.add(new HitResolutionSystem(this));
        gameSystems.add(new DeathSystem(this));
        gameSystems.add(new RespawnSystem(this));
        gameSystems.add(new AudioSystem(this));


        gameSystems.add(new ParticleSystem(this));
        gameSystems.add(new ParticleEmitterSystem(this));
        gameSystems.add(new AnimationDebugSystem(this));
        gameSystems.add(new GameOverSystem(this, transition));


        renderSystems.add(new CountdownCameraSystem(this));
        renderSystems.add(new AnimationSystem(this));
        BackgroundSystem backgroundSystem = new BackgroundSystem(this);
        backgroundSystem.setCamera(camera);
        renderSystems.add(backgroundSystem);

        renderSystems.add(new AveragePositionCameraSystem(this));
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

            while(gameEvents.notEmpty())
            {
                Event ge = gameEvents.removeFirst();

                for(int i = 0; i < gameSystems.size; i++)
                {
                    gameSystems.get(i).receiveEvent(ge);
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

    public void render(float dt, float alpha)
    {
        for (RenderSystem r : renderSystems)
        {
            if(r.isEnabled())
            {
                r.render(dt, alpha);
            }
        }

        while(renderEvents.notEmpty())
        {
            Event re = renderEvents.removeFirst();

            for(int i = 0; i < renderSystems.size; i++)
            {
                renderSystems.get(i).receiveEvent(re);
            }
        }
    }

    public void addEvent(Event event)
    {
        if (event.isImmediate())
        {
            for(int i = 0; i < gameSystems.size; i++)
            {
                gameSystems.get(i).receiveEvent(event);
            }
            for(int i = 0; i < renderSystems.size; i++)
            {
                renderSystems.get(i).receiveEvent(event);
            }
        }
        else
        {
            gameEvents.addLast(event);
            renderEvents.addLast(event);
        }
    }

    public void resize(int w, int h)
    {
        for(RenderSystem r : renderSystems)
        {
            r.resize(w, h);
        }
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

        for(RenderSystem system : renderSystems)
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

        for(RenderSystem system : renderSystems)
        {
            if(system.getClass().equals(clazz))
            {
                system.setEnabled(true);
                return;
            }
        }
    }
}
