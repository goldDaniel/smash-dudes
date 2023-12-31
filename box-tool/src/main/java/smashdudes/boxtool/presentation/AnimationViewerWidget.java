package smashdudes.boxtool.presentation;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import imgui.ImGui;
import smashdudes.content.DTO;
import smashdudes.boxtool.logic.BoxToolContext;
import smashdudes.core.logic.commands.AddBoxCommand;
import smashdudes.core.logic.commands.RectangleEditCommand;
import smashdudes.core.logic.selectable.Selectable;
import smashdudes.core.logic.selectable.SelectableRectangle;
import smashdudes.gameplay.AttackBox;
import smashdudes.gameplay.BodyBox;
import smashdudes.graphics.RenderResources;

public class AnimationViewerWidget extends BoxToolWidget
{
    // Rendering ///////////////////////////////
    static final float WORLD_WIDTH = 4;
    static final float WORLD_HEIGHT = 4;
    private final Viewport viewport = new ExtendViewport(WORLD_WIDTH, WORLD_HEIGHT);
    private FrameBuffer frameBuffer;

    // State tracking ///////////////////////////
    private int previousAnimationFrameCount = 0;

    private DTO.AnimationFrame previousAnimationFrame = null;
    private DTO.Animation previousAnimation = null;
    private Animation<DTO.AnimationFrame> currentAnimation = null;


    // Selection ////////////////////////////////
    boolean rebuildSelectables = true;
    private Selectable selectedRect = null;
    private final Array<Selectable> selectables = new Array<>();

    public AnimationViewerWidget(BoxToolContext context)
    {
        super("Character Viewer", 0, context);
        context.addRedoCallback(() -> rebuildSelectables = true);
        context.addUndoCallback(() -> rebuildSelectables = true);
    }

    @Override
    protected void draw(ShapeRenderer sh, SpriteBatch sb)
    {
        DTO.Animation animation = context.getCurrentAnimation();
        if(animation == null) return;
        DTO.AnimationFrame currentFrame = animate(animation);
        if(currentFrame == null) return;

        // render character data to frameBuffer, then copy to imgui window
        resizeDrawBuffer();
        frameBuffer.bind();
        {
            ScreenUtils.clear(0,0,0,1);
            drawWorldSpaceGrid(sh);
            drawCharacter(RenderResources.getTexture(currentFrame.texturePath), sb);
            drawBoxes(sh);
        }
        FrameBuffer.unbind();
        ImGui.image(frameBuffer.getColorBufferTexture().getTextureObjectHandle(), frameBuffer.getWidth(),frameBuffer.getHeight(),0,1,1,0);

        doSelection();
        drawContextMenu(currentFrame);
    }

    private DTO.AnimationFrame animate(DTO.Animation animation)
    {
        float frameDuration = animation.animationDuration / animation.frames.size;
        if(animation != previousAnimation || previousAnimationFrameCount != animation.frames.size)
        {
            previousAnimation = animation;
            previousAnimationFrameCount = animation.frames.size;
            context.stopAnimation();
            currentAnimation = new Animation<>(frameDuration, animation.frames, Animation.PlayMode.LOOP);
            rebuildSelectables = true;
        }
        currentAnimation.setFrameDuration(frameDuration);


        DTO.AnimationFrame currentFrame = context.getAnimationFrame();
        if(previousAnimationFrame == null || currentFrame != previousAnimationFrame)
        {
            previousAnimationFrame = currentFrame;
            rebuildSelectables = true;
        }

        if(currentFrame != null && context.isPlayingAnimation())
        {
            currentFrame = currentAnimation.getKeyFrame(context.getCurrentTime());
            context.setAnimationFrame(currentFrame);
        }
        if(currentFrame != null && rebuildSelectables)
        {
            selectables.clear();
            for(Rectangle r : currentFrame.attackboxes)
            {

                selectables.add(new SelectableRectangle(r, Color.RED, (Selectable selectable) ->
                {
                    Rectangle copy = (Rectangle)selectable.getClone();
                    float[] data = new float[]{copy.x, copy.y, copy.width, copy.height};
                    context.execute(new RectangleEditCommand((Rectangle)selectable.getOriginal(), data));
                }));
            }
            for(Rectangle r : currentFrame.bodyboxes)
            {
                selectables.add(new SelectableRectangle(r, Color.GREEN, (Selectable selectable) ->
                {
                    Rectangle copy = (Rectangle)selectable.getClone();
                    float[] data = new float[]{copy.x, copy.y, copy.width, copy.height};
                    context.execute(new RectangleEditCommand((Rectangle)selectable.getOriginal(), data));
                }));
            }

            rebuildSelectables = false;
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
    }

    private void drawBoxes(ShapeRenderer sh)
    {
        Vector2 mousePos = getMouseWorldPos();
        sh.setProjectionMatrix(viewport.getCamera().combined);
        for(Selectable rect : selectables)
        {
            rect.draw(mousePos, sh);
        }
    }

    private void drawWorldSpaceGrid(ShapeRenderer sh)
    {
        sh.setProjectionMatrix(viewport.getCamera().combined);
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
        float aspect = (float)texture.getWidth() / (float)texture.getHeight();

        if(aspect > 1) aspect = 1.0f / aspect;
        final float drawWidth = context.getCharacter().scale;
        final float drawHeight = drawWidth * aspect;
        final float drawX = -drawWidth / 2;
        final float drawY = -drawHeight / 2;

        sb.setProjectionMatrix(viewport.getCamera().combined);
        sb.begin();
        sb.draw(texture, drawX, drawY, drawWidth, drawHeight);
        sb.end();
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
                context.execute(new AddBoxCommand(currentFrame.attackboxes, AttackBox.class, new Rectangle(worldPos.x, worldPos.y, 0.5f, 0.5f)));
                rebuildSelectables = true;
                ImGui.closeCurrentPopup();
            }
            ImGui.sameLine();
            if (ImGui.button("Create Body Box"))
            {
                context.execute(new AddBoxCommand(currentFrame.bodyboxes, BodyBox.class, new Rectangle(worldPos.x, worldPos.y, 0.5f, 0.5f)));
                rebuildSelectables = true;
                ImGui.closeCurrentPopup();
            }
            if (ImGui.button("Cancel"))
            {
                ImGui.closeCurrentPopup();
            }

            ImGui.endPopup();
        }
    }

    private void doSelection()
    {
        Vector2 mouseWorldPos = getMouseWorldPos();
        Vector2 mouseDelta = new Vector2();
        mouseDelta.x = Gdx.input.getDeltaX() * 0.005f;
        mouseDelta.y = -Gdx.input.getDeltaY() * 0.005f;

        // attempt selection
        if(selectedRect == null && Gdx.input.isButtonJustPressed(Input.Buttons.LEFT))
        {
            for(Selectable selectable : selectables)
            {
                if(selectable.select(mouseWorldPos))
                {
                    selectedRect = selectable;
                }
            }
        }
        else if(!Gdx.input.isButtonPressed(Input.Buttons.LEFT))
        {
            if(selectedRect != null)
            {
                selectedRect.release();
                selectedRect = null;
            }
        }
        else if(selectedRect != null)
        {
            selectedRect.drag(mouseWorldPos, mouseDelta);
        }
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
