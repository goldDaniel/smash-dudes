package smashdudes.ecs.systems;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ArrayMap;
import com.badlogic.gdx.utils.viewport.Viewport;
import smashdudes.ecs.Engine;
import smashdudes.ecs.Entity;
import smashdudes.ecs.components.DrawComponent;
import smashdudes.ecs.components.PositionComponent;
import smashdudes.graphics.RenderPass;
import smashdudes.graphics.RenderResources;

import java.util.Comparator;

public class RenderDrawSystem extends DrawSystem
{
    private class Renderable
    {
        final Vector2 position;
        final DrawComponent draw;

        public Renderable(Vector2 position, DrawComponent d)
        {
            this.position = position;
            this.draw = d;
        }
    }

    private final ArrayMap<RenderPass, ShaderProgram> shaders = new ArrayMap<>();
    private final Array<Renderable> renderables = new Array<>();

    public RenderDrawSystem(Engine engine, Camera camera, Viewport viewport)
    {
        super(engine, camera, viewport);

        shaders.put(RenderPass.Default, null); //null will make the spritebatch use its default shader
        shaders.put(RenderPass.Stunned, RenderResources.getShader("shaders/spritebatch.default.vert.glsl", "shaders/spritebatch.stunned.frag.glsl"));


        registerComponentType(PositionComponent.class);
        registerComponentType(DrawComponent.class);
    }

    @Override
    public void renderEntity(Entity entity, float dt, float alpha)
    {
        PositionComponent p = entity.getComponent(PositionComponent.class);
        DrawComponent d = entity.getComponent(DrawComponent.class);

        Vector2 interpolatedPos = new Vector2();
        interpolatedPos.x = MathUtils.lerp(p.prevPosition.x, p.position.x, alpha);
        interpolatedPos.y = MathUtils.lerp(p.prevPosition.y, p.position.y, alpha);

        renderables.add(new Renderable(interpolatedPos, d));
    }

    @Override
    public void postRender()
    {
        //we must sort by texture before z-index. While sorting by texture after would reduce draw calls it can
        //mess up our z-index ordering.
        renderables.sort(Comparator.comparingInt(r -> r.draw.texture.glTarget));
        renderables.sort(Comparator.comparingInt(r -> r.draw.pass.ordinal()));
        renderables.sort(Comparator.comparingInt(r -> r.draw.zIndex));

        RenderPass lastPass = null;
        for(Renderable r : renderables)
        {
            if(lastPass == null || lastPass != r.draw.pass)
            {
                lastPass = r.draw.pass;
                if(sb.isDrawing()) sb.end();

                sb.setShader(shaders.get(r.draw.pass));
                sb.begin();
            }

            Vector2 pos = r.position;
            DrawComponent d = r.draw;

            sb.setColor(d.getColor());


            float width = d.scale.x;
            float height = d.scale.y;
            if(d.maintainAspectRatio)
            {
                width = d.scale.x;
                height = (float)d.texture.getHeight() / (float)d.texture.getWidth() * width;
            }

            if(d.facingLeft)
            {
                sb.draw(d.texture, pos.x + width / 2, pos.y - height / 2, -width, height);
            }
            else
            {
                sb.draw(d.texture, pos.x - width / 2, pos.y - height / 2, width, height);
            }
        }

        if(sb.isDrawing()) sb.end();
        renderables.clear();
    }
}
