package smashdudes.ecs.systems;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import smashdudes.ecs.Engine;
import smashdudes.ecs.Entity;
import smashdudes.ecs.components.*;

public class CountdownCameraSystem extends GameSystem
{
    private final Vector2 position = new Vector2(0, 0);
    private int numPlayers;
    private int currPlayer;

    public CountdownCameraSystem(Engine engine)
    {
        super(engine);

        registerComponentType(CountdownComponent.class);
        registerComponentType(CameraComponent.class);

        numPlayers = 0;
        currPlayer = 1;
    }

    @Override
    public void preUpdate()
    {
        Array<Entity> entities = engine.getEntities(PositionComponent.class, PlayerComponent.class);
        numPlayers = entities.size;

        for(int i = 0; i < numPlayers; i++)
        {
            if(i + 1 == currPlayer)
            {
                position.set(entities.get(i).getComponent(PositionComponent.class).position);
            }
        }
    }

    @Override
    public void updateEntity(Entity entity, float dt)
    {
        CountdownComponent count = entity.getComponent(CountdownComponent.class);
        CameraComponent cam = entity.getComponent(CameraComponent.class);

        //increments the currPlayer variable every maxDuration/numPlayer seconds. Making it so every character gets zoomed in on
        //to for an equal number of seconds. When currPlayer is 1, player 1 is zoomed in one, etc.
        float max = count.maxDuration;
        float curr = count.currDuration;
        float threshold = max / numPlayers;
        if(curr > 0)
        {
            if (curr <= max - currPlayer * threshold) // once threshold seconds have passed, go to the next player
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
        cam.camera.zoom = 1f;
        cam.camera.update();
    }
}
