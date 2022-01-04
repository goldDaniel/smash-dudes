package smashdudes.ecs.systems;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
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

    private FrameBuffer targetBuffer;

    private OrthographicCamera camera;
    private Viewport viewport;

    private final SpriteBatch sb;

    public RenderSystem(Engine engine, SpriteBatch sb)
    {
        super(engine);
        this.sb = sb;

        //null will make the spritebatch use its default shader
        shaders.put(RenderPass.Default, null);
        shaders.put(RenderPass.NoTexture, loadShader("shaders/spritebatch.default.vert.glsl", "shaders/spritebatch.stunned.frag.glsl"));

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

        if(targetBuffer != null)
        {
            targetBuffer.dispose();
        }
        targetBuffer = new FrameBuffer(Pixmap.Format.RGBA8888, w, h, false);
    }

    @Override
    public void preUpdate()
    {
        targetBuffer.begin();
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

        targetBuffer.end();
        sb.setProjectionMatrix(new Matrix4());
        sb.begin();
        sb.draw(targetBuffer.getColorBufferTexture(), -1, 1, 2, -2);
        sb.end();
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
