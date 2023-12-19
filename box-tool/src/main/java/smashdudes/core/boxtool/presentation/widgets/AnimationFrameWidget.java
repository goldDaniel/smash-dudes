package smashdudes.core.boxtool.presentation.widgets;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import imgui.ImGui;
import smashdudes.core.boxtool.logic.BoxToolContext;

public class AnimationFrameWidget extends ImGuiWidget
{
    public AnimationFrameWidget(BoxToolContext context)
    {
        super("Animation Frame", 0, context);
    }

    @Override
    protected void Draw(ShapeRenderer sh, SpriteBatch sb)
    {
        if(context.getAnimationFrame() == null)
        {
            ImGui.text("No Frame Selected");
            return;
        }

        ImGui.text("Frame for " + context.getCurrentAnimation().animationName);
        ImGui.separator();
    }
}
