package smashdudes.ecs.systems;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.viewport.Viewport;
import smashdudes.ecs.Engine;
import smashdudes.ecs.Entity;
import smashdudes.ecs.components.DebugDrawComponent;

public class RenderDebugSystem extends DrawSystem
{
    public RenderDebugSystem(Engine engine, Camera camera, Viewport viewport)
    {
        super(engine, camera, viewport);

        registerComponentType(DebugDrawComponent.class);
    }

    @Override
    public void renderEntity(Entity entity, float dt, float alpha)
    {
        DebugDrawComponent d = entity.getComponent(DebugDrawComponent.class);

        for(ShapeRenderer.ShapeType type : ShapeRenderer.ShapeType.values())
        {
            Gdx.gl20.glEnable(GL20.GL_BLEND);
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
