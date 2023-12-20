package smashdudes.core.boxtool.presentation.widgets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import imgui.ImGui;
import smashdudes.content.DTO;
import smashdudes.core.boxtool.logic.BoxToolContext;
import smashdudes.core.boxtool.presentation.commands.AddBoxCommand;
import smashdudes.core.boxtool.presentation.commands.RectangleEditCommand;
import smashdudes.graphics.RenderResources;

public class AnimationViewerWidget extends ImGuiWidget
{
    static final float boxCenterRadius = 0.04f;
    static final float WORLD_WIDTH = 4;
    static final float WORLD_HEIGHT = 4;
    private final Camera camera = new OrthographicCamera(WORLD_WIDTH, WORLD_HEIGHT);
    private final Viewport viewport = new ExtendViewport(WORLD_WIDTH, WORLD_HEIGHT, camera);
    private FrameBuffer frameBuffer;

    private static class SelectedRectangle
    {
        private Rectangle original = null;
        private Rectangle clone = null;

        boolean horizontal = false;
    }
    private SelectedRectangle moveSelectedRectangle = null;
    private SelectedRectangle scaleSelectedRectangle = null;

    private DTO.Animation previousAnimation = null;
    private Animation<DTO.AnimationFrame> currentAnimation = null;

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
        }
        FrameBuffer.unbind();
        ImGui.image(frameBuffer.getColorBufferTexture().getTextureObjectHandle(), frameBuffer.getWidth(),frameBuffer.getHeight(),0,1,1,0);

        mouseMoveBoxes(currentFrame);

        drawContextMenu(currentFrame);
    }

    private SelectedRectangle getScaleSelectedRectangle(Array<Rectangle> rectangles, Vector2 mousePos)
    {
        Rectangle horizontalRect = new Rectangle();
        Rectangle verticalRect = new Rectangle();
        for(Rectangle r : rectangles)
        {
            horizontalRect.set(r.x, r.y, 0.1f, 0.01f);
            if(horizontalRect.contains(mousePos))
            {
                SelectedRectangle rectangle = new SelectedRectangle();
                rectangle.original = r;
                rectangle.clone = new Rectangle(r);
                rectangle.horizontal = true;

                return rectangle;
            }
        }
        return null;
    }
    private SelectedRectangle getMoveSelectedRectangle(Array<Rectangle> rectangles, Vector2 mousePos)
    {
        Circle circle = new Circle();
        for(Rectangle r : rectangles)
        {
            circle.set(r.x, r.y, boxCenterRadius);
            if(circle.contains(mousePos))
            {
                SelectedRectangle rectangle = new SelectedRectangle();
                rectangle.original = r;
                rectangle.clone = new Rectangle(r);

                return rectangle;
            }
        }
        return null;
    }

    private void mouseMoveBoxes(DTO.AnimationFrame frame)
    {
        // no grabbing hitboxes when frames are changing
        if(context.isPlayingAnimation())
        {
            moveSelectedRectangle = null;
            return;
        }

        Vector2 mousePos = getMouseWorldPos();
        if(moveSelectedRectangle != null && Gdx.input.isButtonPressed(Input.Buttons.LEFT))
        {
            moveSelectedRectangle.clone.x = mousePos.x;
            moveSelectedRectangle.clone.y = mousePos.y;
        }
        else if(moveSelectedRectangle != null && !Gdx.input.isButtonPressed(Input.Buttons.LEFT))
        {
            context.execute(new RectangleEditCommand(moveSelectedRectangle.original, new float[]{ mousePos.x, mousePos.y, moveSelectedRectangle.original.width, moveSelectedRectangle.original.height }));
            moveSelectedRectangle = null;
        }
        else if(Gdx.input.isButtonJustPressed(Input.Buttons.LEFT))
        {
            if(moveSelectedRectangle == null) moveSelectedRectangle = getMoveSelectedRectangle(frame.bodyboxes, mousePos);
            if(moveSelectedRectangle == null) moveSelectedRectangle = getMoveSelectedRectangle(frame.attackboxes, mousePos);
        }
    }

    private void mouseScaleBoxes(DTO.AnimationFrame frame)
    {
        // no grabbing hitboxes when frames are changing
        if(context.isPlayingAnimation())
        {
            scaleSelectedRectangle = null;
            return;
        }

        Vector2 mousePos = getMouseWorldPos();
        if(!Gdx.input.isButtonPressed(Input.Buttons.LEFT) && scaleSelectedRectangle != null)
        {
            context.execute(new RectangleEditCommand(scaleSelectedRectangle.original, new float[]{ scaleSelectedRectangle.original.x, scaleSelectedRectangle.original.x, scaleSelectedRectangle.original.x, moveSelectedRectangle.clone.width, moveSelectedRectangle.clone.height }));
            scaleSelectedRectangle = null;
        }

        if(Gdx.input.isButtonJustPressed(Input.Buttons.LEFT))
        {
            if(scaleSelectedRectangle == null) scaleSelectedRectangle = getScaleSelectedRectangle(frame.bodyboxes, mousePos);
            if(scaleSelectedRectangle == null) scaleSelectedRectangle = getScaleSelectedRectangle(frame.attackboxes, mousePos);
        }

        if(scaleSelectedRectangle != null && Gdx.input.isButtonPressed(Input.Buttons.LEFT))
        {
            if(scaleSelectedRectangle.horizontal) scaleSelectedRectangle.clone.width  += Gdx.input.getDeltaX() * 0.001f;
            else                                  scaleSelectedRectangle.clone.height += Gdx.input.getDeltaY() * 0.001f;
        }
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
        for(Rectangle b : boxes)
        {
            Rectangle box = b;
            if(moveSelectedRectangle != null && moveSelectedRectangle.original == b)
            {
                box = moveSelectedRectangle.clone;
            }

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

        Circle circle = new Circle();
        for(Rectangle b : boxes)
        {
            Rectangle box = b;
            if(moveSelectedRectangle != null && moveSelectedRectangle.original == b)
            {
                box = moveSelectedRectangle.clone;
            }

            sh.setColor(Color.LIGHT_GRAY);
            circle.set(box.x, box.y, boxCenterRadius);
            if(circle.contains(getMouseWorldPos()))
            {
                sh.setColor(Color.WHITE);
            }
            sh.circle(box.x, box.y, boxCenterRadius, 16);
        }
        sh.end();
    }

    private void drawContextMenu(DTO.AnimationFrame currentFrame)
    {
        final float viewportX  = ImGui.getWindowPosX();
        final float viewportY = ImGui.getWindowPosY() + 32;
        final float viewportW = frameBuffer.getWidth();
        final float viewportH = frameBuffer.getHeight();

        if (ImGui.beginPopupContextItem("Add Box"))
        {
            final float mouseX = ImGui.getWindowPosX();
            final float mouseY = ImGui.getWindowPosY();
            Vector2 worldPos = getMouseWorldPos(mouseX ,mouseY, viewportX, viewportY, viewportW, viewportH);

            if (ImGui.button("Create Attack Box"))
            {
                context.execute(new AddBoxCommand(currentFrame.attackboxes, new Rectangle(worldPos.x, worldPos.y, 0.5f, 0.5f)));
                ImGui.closeCurrentPopup();
            }
            ImGui.sameLine();
            if (ImGui.button("Create Body Box"))
            {
                context.execute(new AddBoxCommand(currentFrame.bodyboxes, new Rectangle(worldPos.x, worldPos.y, 0.5f, 0.5f)));
                ImGui.closeCurrentPopup();
            }
            if (ImGui.button("Cancel"))
            {
                ImGui.closeCurrentPopup();
            }

            ImGui.endPopup();
        }
    }

    private Vector2 getMouseWorldPos()
    {
        final float mouseX = ImGui.getMousePosX();
        final float mouseY = ImGui.getMousePosY();

        final float viewportX  = ImGui.getWindowPosX();
        final float viewportY = ImGui.getWindowPosY() + 16;
        final float viewportW = frameBuffer.getWidth();
        final float viewportH = frameBuffer.getHeight();

        return getMouseWorldPos(mouseX, mouseY, viewportX, viewportY, viewportW, viewportH);
    }

    private Vector2 getMouseWorldPos(float mouseX, float mouseY, float viewportX, float viewportY, float viewportW, float viewportH)
    {
        Vector2 result = new Vector2();
        Vector3 worldSpace = camera.unproject(new Vector3(mouseX, mouseY, 0), viewportX, viewportY, viewportW, viewportH);
        result.x = worldSpace.x;
        result.y = worldSpace.y;

        return result;
    }
}
