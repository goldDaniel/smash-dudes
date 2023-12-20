package smashdudes.ecs.systems;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.viewport.Viewport;
import smashdudes.ecs.Engine;
import smashdudes.ecs.Entity;
import smashdudes.ecs.components.DebugDrawComponent;

public class RenderDebugSystem extends RenderSystem
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
    public void preRender()
    {
        sh.setProjectionMatrix(camera.combined);
    }

    @Override
    public void renderEntity(Entity entity, float dt, float alpha)
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
    }
}
