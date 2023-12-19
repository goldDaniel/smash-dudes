package smashdudes.core.boxtool.presentation.widgets;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import imgui.ImGui;
import smashdudes.content.DTO;
import smashdudes.core.boxtool.logic.BoxToolContext;
import smashdudes.core.boxtool.presentation.Utils;
import smashdudes.core.boxtool.presentation.commands.AddBoxCommand;
import smashdudes.core.boxtool.presentation.commands.RectangleEditCommand;
import smashdudes.core.boxtool.presentation.commands.RemoveBoxCommand;
import smashdudes.graphics.RenderResources;

public class AnimationFrameWidget extends ImGuiWidget
{
    public AnimationFrameWidget(BoxToolContext context)
    {
        super("Animation Frame", 0, context);
    }

    @Override
    protected void draw(ShapeRenderer sh, SpriteBatch sb)
    {
        DTO.Animation animation = context.getCurrentAnimation();
        DTO.AnimationFrame frame = context.getAnimationFrame();
        if(frame == null)
        {
            ImGui.text("No Frame Selected");
            return;
        }

        ImGui.text("Frame for " + animation.animationName);
        ImGui.separator();

        Texture texture = RenderResources.getTexture(frame.texturePath);

        float frameDrawWidth = 192;
        float frameDrawHeight = frameDrawWidth * (float) texture.getHeight() / (float) texture.getWidth();
        ImGui.image(texture.getTextureObjectHandle(), frameDrawWidth, frameDrawHeight, 0, 0, 1, 1);

        drawBoxEditor("Attackboxes", frame.attackboxes);
        drawBoxEditor("Bodyboxes", frame.bodyboxes);
    }


    private void drawBoxEditor(String name, Array<Rectangle> boxes)
    {
        if(ImGui.collapsingHeader(name))
        {
            if(ImGui.button("Add##" + name))
            {
                context.execute(new AddBoxCommand(boxes, new Rectangle()));
            }

            for(Rectangle rect : boxes)
            {
                ImGui.pushID(Utils.getUniqueKey(rect));
                ImGui.text("" + (boxes.indexOf(rect, true) + 1));
                ImGui.sameLine();
                float[] temp = {rect.x, rect.y, rect.width, rect.height};
                if(ImGui.inputFloat4("", temp))
                {
                    context.execute(new RectangleEditCommand(rect, temp));
                }

                ImGui.sameLine();
                if(ImGui.button("Remove##" + name + rect))
                {
                    context.execute(new RemoveBoxCommand(boxes, rect));
                }

                ImGui.popID();
            }
        }
    }
}
