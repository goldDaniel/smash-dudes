package smashdudes.core.boxtool.presentation.widgets;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import imgui.ImGui;
import smashdudes.content.DTO;
import smashdudes.core.boxtool.logic.BoxToolContext;
import smashdudes.graphics.RenderResources;

public class AnimationViewerWidget extends ImGuiWidget
{
    private DTO.Animation previousAnimation = null;
    Animation<DTO.AnimationFrame> currentAnimation = null;

    public AnimationViewerWidget(BoxToolContext context)
    {
        super("Character Viewer", 0, context);
    }

    @Override
    protected void Draw(ShapeRenderer sh, SpriteBatch sb)
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
        Texture texture = RenderResources.getTexture(currentFrame.texturePath);

        float frameDrawWidth = ImGui.getWindowWidth() / 2;
        float frameDrawHeight = frameDrawWidth * (float) texture.getHeight() / (float) texture.getWidth();

        float avail = ImGui.getContentRegionAvailX();

        float off = (avail - frameDrawWidth) * 0.5f;
        if (off > 0.0f)
        {
            ImGui.setCursorPosX(ImGui.getCursorPosX() + off);
        }
        ImGui.setCursorPosX(ImGui.getCursorPosX() + 20);

        ImGui.image(texture.getTextureObjectHandle(), frameDrawWidth, frameDrawHeight, 0, 0, 1, 1);
    }
}
