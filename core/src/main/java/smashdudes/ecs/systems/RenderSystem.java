package smashdudes.ecs.systems;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.Viewport;
import smashdudes.ecs.Engine;
import smashdudes.ecs.Entity;
import smashdudes.ecs.components.DrawComponent;
import smashdudes.ecs.components.PositionComponent;

public class RenderSystem extends GameSystem
{
    private OrthographicCamera camera;
    private Viewport viewport;

    private final SpriteBatch sb;

    public RenderSystem(Engine engine, SpriteBatch sb)
    {
        super(engine);
        this.sb = sb;

        registerComponentType(PositionComponent.class);
        registerComponentType(DrawComponent.class);
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
        sb.setProjectionMatrix(camera.combined);
        sb.begin();
    }

    @Override
    public void updateEntity(Entity entity, float dt)
    {
        PositionComponent p = entity.getComponent(PositionComponent.class);
        DrawComponent d = entity.getComponent(DrawComponent.class);

        float width = d.scale;
        float height = (float)d.texture.getHeight() / (float)d.texture.getWidth() * d.scale;

        if(d.facingLeft)
        {
            sb.draw(d.texture, p.position.x + width / 2, p.position.y - height / 2, -width, height);
        }
        else
        {
            sb.draw(d.texture, p.position.x - width / 2, p.position.y - height / 2, width, height);
        }

    }

    @Override
    public void postUpdate()
    {
        sb.end();
    }
}
