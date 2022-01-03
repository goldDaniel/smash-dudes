package smashdudes.ecs.systems;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ArrayMap;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.Viewport;
import smashdudes.ecs.Engine;
import smashdudes.ecs.Entity;
import smashdudes.ecs.components.DrawComponent;
import smashdudes.ecs.components.PositionComponent;
import smashdudes.ecs.events.AttackEvent;
import smashdudes.ecs.events.Event;
import smashdudes.graphics.RenderPass;

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

    private ArrayMap<RenderPass, Array<Renderable>> renderables = new ArrayMap<>();
    private ArrayMap<RenderPass, ShaderProgram> shaders = new ArrayMap<>();

    private OrthographicCamera camera;
    private Viewport viewport;

    private final SpriteBatch sb;

    public RenderSystem(Engine engine, SpriteBatch sb)
    {
        super(engine);
        this.sb = sb;

        for(RenderPass r : RenderPass.values())
        {
            renderables.put(r, new Array<>());
        }

        //null will make the spritebatch use its default shader
        shaders.put(RenderPass.Default, null);
        shaders.put(RenderPass.NoTexture, loadShader("shaders/spritebatch.default.vert.glsl", "shaders/spritebatch.notexture.frag.glsl"));

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
        ScreenUtils.clear(0,0,0,0);
        for(RenderPass r : RenderPass.values())
        {
            renderables.get(r).clear();
        }

        sb.setProjectionMatrix(camera.combined);
    }

    @Override
    public void updateEntity(Entity entity, float dt)
    {
        PositionComponent p = entity.getComponent(PositionComponent.class);
        DrawComponent d = entity.getComponent(DrawComponent.class);

        renderables.get(d.pass).add(new Renderable(p.position, d));
    }

    @Override
    public void postUpdate()
    {
        for (ObjectMap.Entry<RenderPass, ShaderProgram> v : shaders)
        {
            sb.setShader(v.value);
            sb.begin();

            for(Renderable r : renderables.get(v.key))
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

    }

    private ShaderProgram loadShader(String vertexPath, String fragmentPath)
    {
        FileHandle vertHandle = Gdx.files.internal(vertexPath);
        FileHandle fragHandle = Gdx.files.internal(fragmentPath);

        ShaderProgram shader = new ShaderProgram(vertHandle, fragHandle);

        if (!shader.isCompiled()) throw new IllegalArgumentException("Error compiling shader: " + shader.getLog());
        return shader;
    }
}
