package smashdudes.core.boxtool.presentation.widgets;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import imgui.ImGui;
import imgui.type.ImFloat;
import smashdudes.content.DTO;
import smashdudes.core.boxtool.logic.BoxToolContext;
import smashdudes.core.boxtool.logic.commands.AddBoxCommand;
import smashdudes.core.boxtool.logic.commands.PropertyEditCommand;
import smashdudes.core.boxtool.logic.commands.RectangleEditCommand;
import smashdudes.core.boxtool.logic.commands.RemoveBoxCommand;
import smashdudes.core.boxtool.presentation.Utils;
import smashdudes.gameplay.AttackBox;
import smashdudes.gameplay.BodyBox;
import smashdudes.gameplay.CombatBox;
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

        drawBoxEditor("Attackboxes", frame.attackboxes, AttackBox.class);
        drawBoxEditor("Bodyboxes", frame.bodyboxes, BodyBox.class);
    }


    private <T extends CombatBox> void drawBoxEditor(String name, Array<T> boxes, Class<T> clazz)
    {
        if(ImGui.collapsingHeader(name))
        {
            if(ImGui.button("Add##" + name))
            {
                context.execute(new AddBoxCommand(boxes, clazz));
            }

            for(T rect : boxes)
            {
                ImGui.pushID(Utils.getUniqueKey(rect));
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

                if(clazz == AttackBox.class)
                {
                    AttackBox b = (AttackBox)rect;
                    ImFloat angle = new ImFloat(MathUtils.radiansToDegrees * b.angle);
                    if(ImGui.inputFloat("Attack Angle", angle))
                    {
                        float result = MathUtils.degreesToRadians * angle.get();
                        context.execute(new PropertyEditCommand<>("angle", result, b));
                    }

                    ImFloat magnitude = new ImFloat(b.power);
                    if(ImGui.inputFloat("Attack Power", magnitude))
                    {
                        context.execute(new PropertyEditCommand<>("power", magnitude.get(), b));
                    }
                }

                ImGui.popID();
            }
        }
    }
}
