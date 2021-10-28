package smashdudes.ecs.systems;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import smashdudes.ecs.Engine;
import smashdudes.ecs.Entity;
import smashdudes.ecs.components.PlayerComponent;
import smashdudes.ecs.components.PositionComponent;
import smashdudes.ecs.events.CameraUpdateEvent;

public class CameraSystem extends GameSystem
{
    private OrthographicCamera camera;

    private int count;
    private Vector2 averagePosition = new Vector2();

    public CameraSystem(Engine engine)
    {
        super(engine);
        camera = new OrthographicCamera(20, 12);

        registerComponentType(PositionComponent.class);
        registerComponentType(PlayerComponent.class);
    }

    @Override
    protected void preUpdate()
    {
        count = 0;
        averagePosition.setZero();
    }

    @Override
    protected void updateEntity(Entity entity, float dt)
    {
        PositionComponent p = entity.getComponent(PositionComponent.class);

        averagePosition.add(p.position);
        count++;
    }

    @Override
    protected void postUpdate()
    {
        averagePosition.scl(1f/count);
        camera.position.lerp(new Vector3(averagePosition, 0), 1/50f);

        engine.addEvent(new CameraUpdateEvent(camera));
    }
}
