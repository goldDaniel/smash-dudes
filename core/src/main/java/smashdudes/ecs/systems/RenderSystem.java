package smashdudes.ecs.systems;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import smashdudes.ecs.Engine;
import smashdudes.ecs.Entity;
import smashdudes.ecs.components.SpriteDrawComponent;
import smashdudes.ecs.components.PositionComponent;
import smashdudes.ecs.events.CameraUpdateEvent;
import smashdudes.ecs.events.Event;

public class RenderSystem extends GameSystem
{
    private final int WORLD_WIDTH = 20;
    private final int WORLD_HEIGHT = 12;

    private OrthographicCamera camera;
    private ExtendViewport viewport;

    private SpriteBatch sb;


    public RenderSystem(Engine engine)
    {
        super(engine);
        registerComponentType(PositionComponent.class);
        registerComponentType(SpriteDrawComponent.class);

        registerEventType(CameraUpdateEvent.class);

        camera = new OrthographicCamera(WORLD_WIDTH, WORLD_HEIGHT);
        camera.zoom = 1.2f;

        viewport = new ExtendViewport(WORLD_WIDTH,WORLD_HEIGHT, camera);
        sb = new SpriteBatch();
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

    @Override
    public void handleEvent(Event event)
    {
        CameraUpdateEvent e = (CameraUpdateEvent)event;

        this.camera = e.camera;
        viewport.setCamera(camera);
        viewport.apply();
    }
}
