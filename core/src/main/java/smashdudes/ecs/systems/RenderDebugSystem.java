package smashdudes.ecs.systems;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.viewport.Viewport;
import smashdudes.ecs.Engine;
import smashdudes.ecs.Entity;
import smashdudes.ecs.components.DebugDrawComponent;

public class RenderDebugSystem extends GameSystem
{

    private OrthographicCamera camera;
    private Viewport viewport;

    private final ShapeRenderer sh;


    public RenderDebugSystem(Engine engine, ShapeRenderer sh)
    {
        super(engine);
        this.sh = sh;

        registerComponentType(DebugDrawComponent.class);
    }

    public void setCamera(OrthographicCamera camera)
    {
        this.camera = camera;
    }

    public void setViewport(Viewport viewport)
    {
        this.viewport = viewport;
    }

    public void resize(int w, int h)
    {
        viewport.update(w, h);
        viewport.apply();
    }

    @Override
    public void preUpdate()
    {
        sh.setProjectionMatrix(camera.combined);
    }

    @Override
    public void updateEntity(Entity entity, float dt)
    {
        DebugDrawComponent d = entity.getComponent(DebugDrawComponent.class);

        for(ShapeRenderer.ShapeType type : ShapeRenderer.ShapeType.values())
        {
            sh.begin(type);

            for(DebugDrawComponent.DebugRect rect : d.get(type))
            {
                sh.setColor(rect.getColor());

                Rectangle r = rect.getRect();
                sh.rect(r.x, r.y, r.width, r.height);
            }

            sh.end();
        }

        d.reset();
    }
}
