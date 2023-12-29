package smashdudes.boxtool.presentation.widgets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import imgui.ImGui;
import imgui.type.ImFloat;
import smashdudes.boxtool.logic.BoxToolContext;
import smashdudes.boxtool.logic.commands.*;
import smashdudes.content.AnimationEvent;
import smashdudes.content.AnimationEventType;
import smashdudes.content.DTO;
import smashdudes.core.Utils;
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

        drawEventEditor(frame);
        drawBoxEditor("Attackboxes", frame.attackboxes, AttackBox.class);
        drawBoxEditor("Bodyboxes", frame.bodyboxes, BodyBox.class);
    }

    private void drawEventEditor(DTO.AnimationFrame currentFrame)
    {
        if(ImGui.collapsingHeader("Events"))
        {
            if(ImGui.button("Add Event"))
            {
                context.execute(new ArrayAddCommand<>(currentFrame.events, AnimationEvent.class));
            }

            AnimationEventType[] eventOptions = AnimationEventType.values();
            for(AnimationEvent event : currentFrame.events)
            {
                String eventTypeString = event.type.toString();
                ImGui.pushItemWidth(ImGui.getWindowWidth()  * 0.30f);
                if(ImGui.beginCombo("##" + event, eventTypeString))
                {
                    for(AnimationEventType type : eventOptions)
                    {
                        String typeString = type.toString();
                        boolean isSelected = typeString.equalsIgnoreCase(eventTypeString);
                        if(ImGui.selectable(typeString, isSelected))
                        {
                            context.execute(new PropertyEditCommand<>("type", type, event));
                        }
                        if(isSelected)
                        {
                            ImGui.setItemDefaultFocus();
                        }

                    }
                    ImGui.endCombo();
                }

                ImGui.sameLine();
                if(event.type == AnimationEventType.Audio)
                {
                    if(ImGui.button("Select Audio File...##" + event))
                    {
                        String file = Utils.chooseFileToLoad(Gdx.files.internal("Audio"), "mp3", "wav", "ogg");
                        if(file != null)
                        {
                            int idx = file.toLowerCase().indexOf("audio");
                            file = file.substring(idx);
                            context.execute(new PropertyEditCommand<>("data", file, event));
                        }
                    }
                }
                ImGui.sameLine();
                ImGui.text(event.data);
                ImGui.popItemWidth();

                ImGui.sameLine();
                if(ImGui.button("Delete##" + event))
                {
                    context.execute(new ArrayRemoveCommand<>(currentFrame.events, event));
                }
            }
        }
    }

    private <T extends CombatBox> void drawBoxEditor(String name, Array<T> boxes, Class<T> clazz)
    {
        if(ImGui.collapsingHeader(name))
        {
            if(ImGui.button("Add##" + name))
            {
                context.execute(new AddBoxCommand<>(boxes, clazz));
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
                    context.execute(new ArrayRemoveCommand<>(boxes, rect));
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
