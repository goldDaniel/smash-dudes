package smashdudes.ecs.systems;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.Viewport;
import smashdudes.ecs.Engine;
import smashdudes.ecs.Entity;
import smashdudes.ecs.components.SpriteDrawComponent;
import smashdudes.ecs.components.PositionComponent;
import smashdudes.ecs.events.Event;

public class RenderSystem extends GameSystem
{
    private final int WORLD_WIDTH = 20;
    private final int WORLD_HEIGHT = 12;

    private OrthographicCamera camera;
    private Viewport viewport;

    private SpriteBatch sb;


    public RenderSystem(Engine engine)
    {
        super(engine);
        registerComponentType(PositionComponent.class);
        registerComponentType(SpriteDrawComponent.class);

        sb = new SpriteBatch();
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
        ScreenUtils.clear(Color.BLACK);

        sb.setProjectionMatrix(camera.combined);
        sb.begin();
    }

    @Override
    public void updateEntity(Entity entity, float dt)
    {
        PositionComponent p = entity.getComponent(PositionComponent.class);
        SpriteDrawComponent d = entity.getComponent(SpriteDrawComponent.class);

        sb.draw(d.sprite, p.position.x - d.width / 2, p.position.y - d.height / 2, d.width, d.height);
    }

    @Override
    public void postUpdate()
    {
        sb.end();
    }
}
