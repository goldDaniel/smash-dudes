package smashdudes.ecs.systems;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import smashdudes.ecs.Engine;
import smashdudes.ecs.Entity;
import smashdudes.ecs.components.DrawComponent;
import smashdudes.ecs.components.PositionComponent;

public class RenderSystem extends GameSystem
{
    private final int WORLD_WIDTH = 20;
    private final int WORLD_HEIGHT = 12;

    private Camera camera;
    private ExtendViewport viewport;

    private ShapeRenderer sh;


    public RenderSystem(Engine engine)
    {
        super(engine);
        registerComponentType(PositionComponent.class);
        registerComponentType(DrawComponent.class);

        camera = new OrthographicCamera(WORLD_WIDTH, WORLD_HEIGHT);
        viewport = new ExtendViewport(WORLD_WIDTH,WORLD_HEIGHT, camera);
        sh = new ShapeRenderer();
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

        sh.setProjectionMatrix(camera.combined);
        sh.begin(ShapeRenderer.ShapeType.Filled);
    }

    @Override
    public void updateEntity(Entity entity, float dt)
    {
        PositionComponent p = entity.getComponent(PositionComponent.class);
        DrawComponent d = entity.getComponent(DrawComponent.class);

        sh.setColor(d.color);
        sh.rect(p.position.x - d.width / 2, p.position.y - d.height / 2, d.width, d.height);
    }

    @Override
    public void postUpdate()
    {
        sh.end();
    }
}
