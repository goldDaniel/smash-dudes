package smashdudes.particletool.presentation;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import imgui.ImGui;
import smashdudes.core.ImGuiWidget;
import smashdudes.core.logic.commands.Command;
import smashdudes.core.logic.selectable.SelectableMovable;
import smashdudes.core.logic.selectable.SelectionContext;
import smashdudes.graphics.effects.ParticleEmitter;
import smashdudes.graphics.effects.ParticleEmitterConfig;
import smashdudes.particletool.logic.ParticleEditorContext;

public class EffectViewerWidget extends ImGuiWidget
{
    // Rendering ///////////////////////////////
    static final float WORLD_WIDTH = 16;
    static final float WORLD_HEIGHT = 16;

    private final OrthographicCamera camera = new OrthographicCamera(WORLD_WIDTH, WORLD_HEIGHT);
    private final Viewport viewport = new ExtendViewport(WORLD_WIDTH, WORLD_HEIGHT, camera);
    private FrameBuffer frameBuffer;


    // State ///////////////////////////////////
    private final ParticleEditorContext context;
    private final SelectionContext selectionContext = new SelectionContext();
    private int previousSelectableCount = 0;

    public EffectViewerWidget(ParticleEditorContext context)
    {
        super("Effect View", 0);
        this.context = context;
    }

    @Override
    protected void draw(ShapeRenderer sh, SpriteBatch sb)
    {
        if(context.isPlaying())
        {
            for(ParticleEmitter emitter : context.getEmitters())
            {
                emitter.update(Gdx.graphics.getDeltaTime());
            }
        }

        if(context.getEffect().emitterConfigs.size != previousSelectableCount)
        {
            previousSelectableCount = context.getEffect().emitterConfigs.size;
            selectionContext.clearSelectables();
            for(ParticleEmitterConfig config : context.getEffect().emitterConfigs)
            {
                selectionContext.addSelectable(new SelectableMovable(config.origin, selectable ->
                {
                    Vector2 next = (Vector2)selectable.getClone();
                    context.execute(new Command()
                    {
                        final Vector2 ref = (Vector2)selectable.getOriginal();
                        final Vector2 prevValue = ref.cpy();
                        final Vector2 newValue = next.cpy();
                        @Override
                        protected void execute()
                        {
                            ref.set(newValue);
                        }

                        @Override
                        protected void undo()
                        {
                            ref.set(prevValue);
                        }
                    });
                }));
            }
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
            sb.setBlendFunctionSeparate(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA, GL20.GL_ONE, GL20.GL_ONE);
            sb.setProjectionMatrix(viewport.getCamera().combined);
            sb.begin();
            for(ParticleEmitter emitter : context.getEmitters())
            {
                emitter.render(sb);
            }
            sb.end();

            selectionContext.draw(getMouseWorldPos(), sh);
        }
        FrameBuffer.unbind();
        ImGui.image(frameBuffer.getColorBufferTexture().getTextureObjectHandle(), frameBuffer.getWidth(),frameBuffer.getHeight(),0,1,1,0);

        selectionContext.doSelection(getMouseWorldPos());
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
        for(float x = 0; x <= maxX; x++)
        {
            sh.line(x, minY, x, maxY);
        }
        for(float x = -1; x >= minX; x--)
        {
            sh.line(x, minY, x, maxY);
        }

        for(float y = 0; y <= maxY; y++)
        {
            sh.line(minX, y, maxX, y);
        }
        for(float y = -1; y >= minY; y--)
        {
            sh.line(minX, y, maxX, y);
        }

        sh.end();
    }

    private void resizeDrawBuffer()
    {
        final int width = (int) ImGui.getWindowWidth();
        final int height = (int)ImGui.getWindowHeight() - 64;

        if(frameBuffer == null || frameBuffer.getWidth() != width || frameBuffer.getHeight() != height)
        {
            if(width <= 0 || height <= 0) return;

            if(frameBuffer != null) frameBuffer.dispose();
            frameBuffer = new FrameBuffer(Pixmap.Format.RGBA8888, width, height, false);
        }
        viewport.update(width, height);
        viewport.apply();
    }

    private Vector2 getMouseWorldPos()
    {
        final float mouseX = ImGui.getMousePosX();
        final float mouseY = ImGui.getMousePosY();

        final float viewportX  = ImGui.getWindowPosX();
        final float viewportY = ImGui.getWindowPosY();
        final float viewportW = frameBuffer.getWidth();
        final float viewportH = frameBuffer.getHeight();

        return getMouseWorldPos(mouseX, mouseY, viewportX, viewportY, viewportW, viewportH);
    }

    private Vector2 getMouseWorldPos(float mouseX, float mouseY, float viewportX, float viewportY, float viewportW, float viewportH)
    {
        Vector2 result = new Vector2();
        Vector3 worldSpace = viewport.getCamera().unproject(new Vector3(mouseX, mouseY, 0), viewportX, viewportY, viewportW, viewportH);
        result.x = worldSpace.x;
        result.y = worldSpace.y;

        return result;
    }
}
