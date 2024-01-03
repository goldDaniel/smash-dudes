package smashdudes.ecs.systems;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.viewport.Viewport;
import smashdudes.ecs.Engine;
import smashdudes.ecs.Entity;
import smashdudes.graphics.RenderResources;

public abstract class RenderSystem extends ISystem
{
    public RenderSystem(Engine engine)
    {
        super(engine);
    }

    protected void preRender() {}

    protected void postRender() {}

    protected void renderEntity(Entity entity, float dt, float alpha) {}

    public void resize(int w, int h) {}

    public void render(float dt, float alpha)
    {
        preRender();

        for(Entity e : getEntities())
        {
            renderEntity(e, dt, alpha);
        }

        postRender();
    }
}
