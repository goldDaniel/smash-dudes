package smashdudes.core.boxtool.presentation.widgets;

import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import imgui.ImGui;
import smashdudes.content.DTO;
import smashdudes.core.boxtool.logic.BoxToolContext;
import smashdudes.graphics.RenderResources;

public class AnimationViewerWidget extends ImGuiWidget
{
    static final int WORLD_WIDTH = 4;
    static final int WORLD_HEIGHT = 4;
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
        if(animation == null)
        {
            return;
        }

        if(animation != previousAnimation)
        {
            previousAnimation = animation;
            context.stopAnimation();

            float frameDuration = animation.animationDuration / animation.frames.size;
            currentAnimation = new Animation<>(frameDuration, animation.frames, Animation.PlayMode.LOOP);
        }

        DTO.AnimationFrame currentFrame = context.getAnimationFrame();
        if(context.isPlayingAnimation())
        {
            currentFrame = currentAnimation.getKeyFrame(context.getCurrentTime());
            context.setAnimationFrame(currentFrame);
        }


        final int width = (int)ImGui.getWindowWidth();
        final int height = (int)ImGui.getWindowHeight() - 64;

        if(frameBuffer == null || frameBuffer.getWidth() != width || frameBuffer.getHeight() != height)
        {
            if(frameBuffer != null) frameBuffer.dispose();
            frameBuffer = new FrameBuffer(Pixmap.Format.RGBA8888, width, height, false);

            viewport.update(width, height);
            viewport.apply();
        }
        camera.update();

        Texture texture = RenderResources.getTexture(currentFrame.texturePath);
        final float aspect = (float)texture.getWidth() / (float)texture.getHeight();


        final float drawWidth = context.getCharacter().scale;
        final float drawHeight = drawWidth * aspect;
        final float drawX = -drawWidth / 2;
        final float drawY = -drawHeight / 2;

        frameBuffer.bind();
        ScreenUtils.clear(0,0,0,1);
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

        sb.setProjectionMatrix(camera.combined);
        sb.begin();
        sb.draw(texture, drawX, drawY, drawWidth, drawHeight);
        sb.end();

        sh.begin(ShapeRenderer.ShapeType.Line);
        sh.setColor(Color.RED);
        for(Rectangle bodybox : currentFrame.bodyboxes)
        {
            float w = bodybox.width;
            float h = bodybox.height;
            float x = (bodybox.x - w / 2);
            float y = (bodybox.y - h / 2);

            sh.rect(x, y, w, h);
        }
        sh.setColor(Color.GREEN);
        for(Rectangle attackbox : currentFrame.attackboxes)
        {
            float w = attackbox.width;
            float h = attackbox.height;
            float x = (attackbox.x - w / 2);
            float y = (attackbox.y - h / 2);

            sh.rect(x, y, w, h);
        }

        sh.end();
        FrameBuffer.unbind();

        ImGui.image(frameBuffer.getColorBufferTexture().getTextureObjectHandle(), width,height,0,1,1,0);
    }
}
