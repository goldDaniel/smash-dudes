package smashdudes.ecs.systems;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import smashdudes.ecs.Engine;
import smashdudes.ecs.Entity;
import smashdudes.ecs.components.AveragePositionCameraComponent;
import smashdudes.ecs.components.CameraComponent;
import smashdudes.ecs.components.PlayerComponent;
import smashdudes.ecs.components.PositionComponent;

public class AveragePositionCameraSystem extends GameSystem
{
    private final float excludeThreshold = 50f;

    //bounding box of all player positions
    private final Vector2 min = new Vector2(Float.MAX_VALUE, Float.MAX_VALUE);
    private final Vector2 max = new Vector2(Float.MIN_VALUE, Float.MIN_VALUE);
    private final Vector2 averagePosition = new Vector2();

    public AveragePositionCameraSystem(Engine engine)
    {
        super(engine);

        registerComponentType(CameraComponent.class);
        registerComponentType(AveragePositionCameraComponent.class);
    }

    @Override
    protected void preUpdate()
    {
        min.set(Float.MAX_VALUE, Float.MAX_VALUE);
        max.set(Float.MIN_VALUE, Float.MIN_VALUE);
        averagePosition.setZero();

        Array<Vector2> positions = new Array<>();
        //add the zero vector so the camera always gravitates toward the center of the stage
        positions.add(new Vector2());

        Array<Entity> entities = engine.getEntities(PositionComponent.class, PlayerComponent.class);

        for(Entity e: entities)
        {
            PositionComponent p = e.getComponent(PositionComponent.class);
            if(p.position.len2() < excludeThreshold*excludeThreshold)
            {
                positions.add(p.position);
            }
        }

        for(Vector2 p : positions)
        {
            if(p.x < min.x) min.x = p.x;
            if(p.y < min.y) min.y = p.y;

            if(p.x > max.x) max.x = p.x;
            if(p.y > max.y) max.y = p.y;

            averagePosition.add(p);
        }
        averagePosition.scl(1f/positions.size);
    }

    @Override
    protected void updateEntity(Entity entity, float dt)
    {
        CameraComponent cam = entity.getComponent(CameraComponent.class);

        cam.camera.position.lerp(new Vector3(averagePosition, 0), 1/50f);
        cam.camera.zoom = MathUtils.lerp(cam.camera.zoom, MathUtils.clamp(max.dst(min) * 0.1f, 1.4f, 2.6f), 1/50f);
        cam.camera.update();
    }
}
