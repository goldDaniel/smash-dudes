package smashdudes.ecs.systems;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.viewport.Viewport;
import smashdudes.ecs.Engine;
import smashdudes.graphics.RenderResources;

public abstract class DrawSystem extends RenderSystem
{
    protected final Camera camera;
    protected final Viewport viewport;

    protected final SpriteBatch sb = RenderResources.getSpriteBatch();
    protected final ShapeRenderer sh = RenderResources.getShapeRenderer();

    public DrawSystem(Engine engine, Camera camera, Viewport viewport)
    {
        super(engine);
        this.camera = camera;
        this.viewport = viewport;
    }

    @Override
    protected void preRender()
    {
        sb.setProjectionMatrix(camera.combined);
        sh.setProjectionMatrix(camera.combined);
    }

    @Override
    public void resize(int w, int h)
    {
        viewport.update(w, h);
        viewport.apply();
    }
}
