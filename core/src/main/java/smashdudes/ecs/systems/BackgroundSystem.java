package smashdudes.ecs.systems;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.ArrayMap;
import smashdudes.ecs.Engine;
import smashdudes.ecs.Entity;
import smashdudes.ecs.components.BackgroundComponent;
import smashdudes.ecs.components.DrawComponent;
import smashdudes.ecs.components.PositionComponent;

public class BackgroundSystem extends GameSystem
{
    private OrthographicCamera camera;

    private ArrayMap<DrawComponent, Float> initialScale = new ArrayMap<>();

    public BackgroundSystem(Engine engine)
    {
        super(engine);

        registerComponentType(PositionComponent.class);
        registerComponentType(BackgroundComponent.class);
        registerComponentType(DrawComponent.class);
    }

    public void setCamera(OrthographicCamera cam)
    {
        this.camera = cam;
    }

    @Override
    protected void updateEntity(Entity entity, float dt)
    {
        PositionComponent pos = entity.getComponent(PositionComponent.class);
        BackgroundComponent background = entity.getComponent(BackgroundComponent.class);
        DrawComponent draw = entity.getComponent(DrawComponent.class);

        if(!initialScale.containsKey(draw))
        {
            initialScale.put(draw, draw.scale);
        }

        float x = camera.position.x + background.offset.x * camera.zoom + camera.position.x * background.parallax.x;
        float y = camera.position.y + background.offset.y * camera.zoom + camera.position.y * background.parallax.y;

        pos.position.set(x, y);

        draw.scale = initialScale.get(draw) * camera.zoom;
    }
}