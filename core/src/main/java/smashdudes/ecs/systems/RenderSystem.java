package smashdudes.ecs.systems;

import smashdudes.ecs.Engine;
import smashdudes.ecs.Entity;

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
