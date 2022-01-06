package smashdudes.ecs.systems;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ArrayMap;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.Viewport;
import smashdudes.ecs.Engine;
import smashdudes.ecs.Entity;
import smashdudes.ecs.components.DrawComponent;
import smashdudes.ecs.components.PositionComponent;
import smashdudes.graphics.RenderPass;
import smashdudes.graphics.RenderResources;

public class RenderSystem extends GameSystem
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
    private final ArrayMap<ShaderProgram, Array<Renderable>> renderables = new ArrayMap<>();

    private OrthographicCamera camera;
    private Viewport viewport;

    private final SpriteBatch sb;
    private final BitmapFont font;

    private float dt;

    public RenderSystem(Engine engine, SpriteBatch sb, BitmapFont font)
    {
        super(engine);
        this.sb = sb;
        this.font = font;

        //null will make the spritebatch use its default shader
        shaders.put(RenderPass.Default, null);
        shaders.put(RenderPass.Stunned, RenderResources.getShader("shaders/spritebatch.default.vert.glsl", "shaders/spritebatch.stunned.frag.glsl"));

        for(ShaderProgram s : shaders.values())
        {
            renderables.put(s, new Array<>());
        }

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
        ScreenUtils.clear(0,0,0,1);

        for(ShaderProgram r : shaders.values())
        {
            renderables.get(r).clear();
        }

        sb.setProjectionMatrix(camera.combined);
    }

    @Override
    public void updateEntity(Entity entity, float dt)
    {
        this.dt = dt;
        PositionComponent p = entity.getComponent(PositionComponent.class);
        DrawComponent d = entity.getComponent(DrawComponent.class);

        ShaderProgram shader = shaders.get(d.pass);
        renderables.get(shader).add(new Renderable(p.position, d));
    }

    @Override
    public void postUpdate()
    {
        for (ShaderProgram shader  : shaders.values())
        {
            sb.setShader(shader);
            sb.begin();

            for(Renderable r : renderables.get(shader))
            {
                Vector2 pos = r.position;
                DrawComponent d = r.draw;

                sb.setColor(d.getColor());

                float width = d.scale;
                float height = (float)d.texture.getHeight() / (float)d.texture.getWidth() * d.scale;

                if(d.facingLeft)
                {
                    sb.draw(d.texture, pos.x + width / 2, pos.y - height / 2, -width, height);
                }
                else
                {
                    sb.draw(d.texture, pos.x - width / 2, pos.y - height / 2, width, height);
                }
            }

            sb.end();
        }

        sb.setShader(null);
        sb.setProjectionMatrix(new OrthographicCamera(1280, 720).combined);
        sb.begin();
        font.draw(sb, "FPS: " + Gdx.graphics.getFramesPerSecond(), -640, -300);
        sb.end();
    }
}
