package smashdudes.ecs.systems;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import smashdudes.ecs.Engine;
import smashdudes.ecs.Entity;
import smashdudes.ecs.components.PlayerComponent;
import smashdudes.ecs.components.PositionComponent;

public class CameraSystem extends GameSystem
{
    private OrthographicCamera camera;

    private Array<Vector2> positions = new Array<>();

    public CameraSystem(Engine engine)
    {
        super(engine);

        registerComponentType(PositionComponent.class);
        registerComponentType(PlayerComponent.class);
    }

    public void setCamera(OrthographicCamera camera)
    {
        this.camera = camera;
    }

    @Override
    protected void preUpdate()
    {
        positions.clear();
    }

    @Override
    protected void updateEntity(Entity entity, float dt)
    {
        PositionComponent p = entity.getComponent(PositionComponent.class);
        positions.add(p.position);
    }

    @Override
    protected void postUpdate()
    {
        Vector2 min = new Vector2(Float.MAX_VALUE, Float.MAX_VALUE);
        Vector2 max = new Vector2(Float.MIN_VALUE, Float.MIN_VALUE);;

        Vector2 averagePosition = new Vector2();
        for(Vector2 p : positions)
        {
            if(p.x < min.x) min.x = p.x;
            if(p.y < min.y) min.y = p.y;

            if(p.x > max.x) max.x = p.x;
            if(p.y > max.y) max.y = p.y;

            averagePosition.add(p);
        }
        averagePosition.scl(1f/positions.size);

        camera.position.lerp(new Vector3(averagePosition, 0), 1/50f);
        camera.zoom = MathUtils.lerp(camera.zoom, MathUtils.clamp(max.dst(min) * 0.1f, 1.2f, 2.2f), 1/50f);
        camera.update();
    }
}
