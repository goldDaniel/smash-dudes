package smashdudes.ecs.systems;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.Viewport;
import smashdudes.ecs.Engine;
import smashdudes.ecs.Entity;
import smashdudes.ecs.components.DebugDrawComponent;
import smashdudes.ecs.components.PositionComponent;

public class RenderDebugSystem extends GameSystem
{
    private final int WORLD_WIDTH = 20;
    private final int WORLD_HEIGHT = 12;

    private OrthographicCamera camera;
    private Viewport viewport;

    private final ShapeRenderer sh;


    public RenderDebugSystem(Engine engine, ShapeRenderer sh)
    {
        super(engine);
        this.sh = sh;

        registerComponentType(PositionComponent.class);
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
        sh.begin(ShapeRenderer.ShapeType.Line);
    }

    @Override
    public void updateEntity(Entity entity, float dt)
    {
        PositionComponent p = entity.getComponent(PositionComponent.class);
        DebugDrawComponent d = entity.getComponent(DebugDrawComponent.class);

        sh.setColor(d.color);
        float entityX = p.position.x - d.width / 2;
        float entityY = p.position.y - d.height / 2;
        sh.rect(entityX, entityY, d.width, d.height);

        for (Rectangle hitbox : d.hitboxes)
        {
            sh.rect(hitbox.x + p.position.x - hitbox.width / 2, hitbox.y + p.position.y - hitbox.height / 2, hitbox.width, hitbox.height);
        }

        d.hitboxes = new Array<Rectangle>();
    }

    @Override
    public void postUpdate()
    {
        sh.end();
    }
}
