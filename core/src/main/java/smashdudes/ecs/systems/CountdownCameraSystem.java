package smashdudes.ecs.systems;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import smashdudes.ecs.Engine;
import smashdudes.ecs.Entity;
import smashdudes.ecs.components.*;

import java.lang.reflect.GenericArrayType;

public class CountdownCameraSystem extends GameSystem
{
    private final Vector2 position = new Vector2(0, 0);
    private int numPlayers;
    private int currPlayer;

    private float timer;

    public CountdownCameraSystem(Engine engine)
    {
        super(engine);

        registerComponentType(CountdownComponent.class);
        registerComponentType(CameraComponent.class);

        numPlayers = 0;
        currPlayer = 1;
        timer = 0;
    }

    @Override
    public void preUpdate()
    {
        Array<Entity> entities = engine.getEntities(PositionComponent.class, PlayerComponent.class);
        for(Entity e : entities)
        {
            int ID = e.getComponent(PlayerComponent.class).handle.ID;

            if(ID == currPlayer)
            {
                position.set(e.getComponent(PositionComponent.class).position);
            }
            if(ID > numPlayers)
            {
                numPlayers = ID;
            }
        }
    }

    @Override
    public void updateEntity(Entity entity, float dt)
    {
        CountdownComponent count = entity.getComponent(CountdownComponent.class);
        CameraComponent cam = entity.getComponent(CameraComponent.class);

        float max = count.maxDuration;
        float curr = count.currDuration;
        float threshold = max / numPlayers;
        timer += dt;
        if(timer < max)
        {
            if (curr <= max - currPlayer * threshold)
            {
                currPlayer++;
            }
        }
        else
        {
            entity.removeComponent(CountdownComponent.class);
            entity.addComponent(new AveragePositionCameraComponent());
        }

        cam.camera.position.lerp(new Vector3(position, 0), 1/50f);
        cam.camera.zoom = 1f;//MathUtils.lerp(cam.camera.zoom, MathUtils.clamp(max.dst(min) * 0.1f, 1.4f, 2.6f), 1/50f);
        cam.camera.update();
    }
}
