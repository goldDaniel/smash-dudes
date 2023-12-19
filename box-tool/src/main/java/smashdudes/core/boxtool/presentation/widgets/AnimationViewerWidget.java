package smashdudes.core.boxtool.presentation.widgets;

import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import imgui.ImGui;
import imgui.extension.imguizmo.ImGuizmo;
import smashdudes.content.DTO;
import smashdudes.core.boxtool.logic.BoxToolContext;
import smashdudes.graphics.RenderResources;

public class AnimationViewerWidget extends ImGuiWidget
{
    static final float boxCenterRadius = 0.04f;
    static final float WORLD_WIDTH = 4;
    static final float WORLD_HEIGHT = 4;
    private final Camera camera = new OrthographicCamera(WORLD_WIDTH, WORLD_HEIGHT);
    private final Viewport viewport = new ExtendViewport(WORLD_WIDTH, WORLD_HEIGHT, camera);
    private FrameBuffer frameBuffer;

    private DTO.Animation previousAnimation = null;
    Animation<DTO.AnimationFrame> currentAnimation = null;

    public AnimationViewerWidget(BoxToolContext context)
    {
        super("Character Viewer", 0, context);
    }

    @Override
    protected void draw(ShapeRenderer sh, SpriteBatch sb)
    {
        DTO.Animation animation = context.getCurrentAnimation();
        if(animation == null) return;
        DTO.AnimationFrame currentFrame = animate(animation);

        // render character data to frameBuffer, then copy to imgui window
        resizeDrawBuffer();
        frameBuffer.bind();
        {
            ScreenUtils.clear(0,0,0,1);
            drawWorldSpaceGrid(sh);
            drawCharacter(RenderResources.getTexture(currentFrame.texturePath), sb);
            drawBoxes(currentFrame.attackboxes, Color.RED, sh);
            drawBoxes(currentFrame.bodyboxes, Color.GREEN, sh);

            drawDebugMouseCursor(sh);
        }
        FrameBuffer.unbind();
        ImGui.image(frameBuffer.getColorBufferTexture().getTextureObjectHandle(), frameBuffer.getWidth(),frameBuffer.getHeight(),0,1,1,0);
    }

    private DTO.AnimationFrame animate(DTO.Animation animation)
    {
        float frameDuration = animation.animationDuration / animation.frames.size;
        if(animation != previousAnimation)
        {
            previousAnimation = animation;
            context.stopAnimation();
            currentAnimation = new Animation<>(frameDuration, animation.frames, Animation.PlayMode.LOOP);
        }
        currentAnimation.setFrameDuration(frameDuration);

        DTO.AnimationFrame currentFrame = context.getAnimationFrame();
        if(context.isPlayingAnimation())
        {
            currentFrame = currentAnimation.getKeyFrame(context.getCurrentTime());
            context.setAnimationFrame(currentFrame);
        }

        return currentFrame;
    }

    void resizeDrawBuffer()
    {
        final int width = (int)ImGui.getWindowWidth();
        final int height = (int)ImGui.getWindowHeight() - 64;

        if(frameBuffer == null || frameBuffer.getWidth() != width || frameBuffer.getHeight() != height)
        {
            if(width <= 0 || height <= 0) return;

            if(frameBuffer != null) frameBuffer.dispose();
            frameBuffer = new FrameBuffer(Pixmap.Format.RGBA8888, width, height, false);
        }
        viewport.update(width, height);
        viewport.apply();
        camera.update();
    }

    private void drawWorldSpaceGrid(ShapeRenderer sh)
    {
        sh.setProjectionMatrix(camera.combined);
        sh.begin(ShapeRenderer.ShapeType.Line);
        sh.setColor(Color.LIGHT_GRAY);
        for(int y = -5; y <= 5; ++y)
        {
            sh.line(-5, y, 5, y);
        }
        for(int x = -5; x <= 5; ++x)
        {
            sh.line(x, -5, x, 5);
        }
        sh.end();
    }

    private void drawCharacter(Texture texture, SpriteBatch sb)
    {
        final float aspect = (float)texture.getWidth() / (float)texture.getHeight();

        final float drawWidth = context.getCharacter().scale;
        final float drawHeight = drawWidth * aspect;
        final float drawX = -drawWidth / 2;
        final float drawY = -drawHeight / 2;

        sb.setProjectionMatrix(camera.combined);
        sb.begin();
        sb.draw(texture, drawX, drawY, drawWidth, drawHeight);
        sb.end();
    }

    private void drawBoxes(Array<Rectangle> boxes, Color color, ShapeRenderer sh)
    {
        sh.setProjectionMatrix(camera.combined);
        sh.begin(ShapeRenderer.ShapeType.Line);
        for(Rectangle box : boxes)
        {
            float w = box.width;
            float h = box.height;
            float x = (box.x - w / 2);
            float y = (box.y - h / 2);

            sh.setColor(color);
            sh.rect(x, y, w, h);

            sh.line(x, y, x + w, y + h);
            sh.line(x, y + h, x + w, y);
        }
        sh.end();

        sh.begin(ShapeRenderer.ShapeType.Filled);
        for(Rectangle box : boxes)
        {
            sh.circle(box.x, box.y, boxCenterRadius, 16);
        }
        sh.end();
    }

    private void drawDebugMouseCursor(ShapeRenderer sh)
    {

        final float width  = ImGui.getWindowWidth();
        final float height = ImGui.getWindowHeight();
        final float mouseX = ImGui.getMousePosX() - ImGui.getWindowPosX();
        final float mouseY = ImGui.getMousePosY() - ImGui.getWindowPosY();
        {
            
            final float worldX = MathUtils.map(0, width, -WORLD_WIDTH / 2, WORLD_WIDTH / 2, mouseX);
            final float worldY = MathUtils.map(0, height, WORLD_HEIGHT / 2, -WORLD_HEIGHT / 2, mouseY);

            sh.begin(ShapeRenderer.ShapeType.Filled);
            sh.setColor(Color.WHITE);
            sh.circle(worldX, worldY, 0.4f, 16);
            sh.end();
        }
    }
}
