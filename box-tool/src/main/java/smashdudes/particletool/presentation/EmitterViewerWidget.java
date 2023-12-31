package smashdudes.particletool.presentation;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import imgui.ImGui;
import smashdudes.core.ImGuiWidget;
import smashdudes.graphics.effects.Particle;
import smashdudes.graphics.effects.ParticleEmitter;
import smashdudes.particletool.logic.ParticleEditorContext;

public class EmitterViewerWidget extends ImGuiWidget
{
    // Rendering ///////////////////////////////
    static final float WORLD_WIDTH = 16;
    static final float WORLD_HEIGHT = 16;

    private final OrthographicCamera camera = new OrthographicCamera(WORLD_WIDTH, WORLD_HEIGHT);
    private final Viewport viewport = new ExtendViewport(WORLD_WIDTH, WORLD_HEIGHT, camera);
    private FrameBuffer frameBuffer;


    // State ///////////////////////////////////
    private final ParticleEditorContext context;
    private final ParticleEmitter emitter;

    public EmitterViewerWidget(ParticleEditorContext context)
    {
        super("Emitter View", 0);
        this.context = context;

        emitter = new ParticleEmitter(context.getParticleEmitterConfig(), new Pool<Particle>(2048)
        {
            @Override
            protected Particle newObject()
            {
                return new Particle();
            }
        });
    }

    @Override
    protected void draw(ShapeRenderer sh, SpriteBatch sb)
    {
        if(context.isPlaying())
        {
            emitter.update(Gdx.graphics.getDeltaTime());
        }

        if(Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT)  && Gdx.input.isKeyJustPressed(Input.Keys.EQUALS))
        {
            camera.zoom /= 1.1f;
            camera.update();
        }
        if(Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT)  && Gdx.input.isKeyJustPressed(Input.Keys.MINUS))
        {
            camera.zoom *= 1.1f;
            camera.update();
        }
        if(Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT)  && Gdx.input.isKeyJustPressed(Input.Keys.NUM_0))
        {
            camera.zoom = 1f;
            camera.update();
        }

        resizeDrawBuffer();
        if(frameBuffer == null) return;

        frameBuffer.bind();
        {
            ScreenUtils.clear(0,0,0,1);
            drawUnitGrid(sh);

            sb.enableBlending();
            sb.setProjectionMatrix(viewport.getCamera().combined);
            sb.begin();
            emitter.render(sb);
            sb.end();
        }
        FrameBuffer.unbind();
        ImGui.image(frameBuffer.getColorBufferTexture().getTextureObjectHandle(), frameBuffer.getWidth(),frameBuffer.getHeight(),0,1,1,0);
    }

    private void drawUnitGrid(ShapeRenderer sh)
    {

        float minX = camera.zoom * -camera.viewportWidth / 2;
        float maxX = camera.zoom *  camera.viewportWidth / 2;
        float minY = camera.zoom * -camera.viewportHeight / 2;
        float maxY = camera.zoom *  camera.viewportHeight / 2;
        sh.begin(ShapeRenderer.ShapeType.Line);
        sh.setProjectionMatrix(camera.combined);
        sh.setColor(Color.LIGHT_GRAY);
        for(float x = minX; x <= maxX; x++)
        {
            sh.line(x, minY, x, maxY);
        }
        for(float y = minY; y <= maxY; y++)
        {
            sh.line(minX, y, maxX, y);
        }
        sh.end();
    }

    private void resizeDrawBuffer()
    {
        final int width = (int) ImGui.getWindowWidth();
        final int height = (int)ImGui.getWindowHeight();

        if(frameBuffer == null || frameBuffer.getWidth() != width || frameBuffer.getHeight() != height)
        {
            if(width <= 0 || height <= 0) return;

            if(frameBuffer != null) frameBuffer.dispose();
            frameBuffer = new FrameBuffer(Pixmap.Format.RGBA8888, width, height, false);
        }
        viewport.update(width, height);
        viewport.apply();
    }
}
