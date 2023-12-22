package smashdudes.core.boxtool.presentation.widgets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import imgui.ImGui;
import imgui.flag.ImGuiWindowFlags;
import imgui.type.ImFloat;
import imgui.type.ImInt;
import smashdudes.content.DTO;
import smashdudes.core.boxtool.logic.BoxToolContext;
import smashdudes.core.boxtool.logic.commands.*;
import smashdudes.core.boxtool.presentation.Utils;

public class AnimationWidget extends ImGuiWidget
{
    private final ImInt addFrameIdx = new ImInt();
    private String addFrameTexturePath = "";

    public AnimationWidget(BoxToolContext context)
    {
        super("Animation", 0, context);
    }

    @Override
    protected void draw(ShapeRenderer sh, SpriteBatch sb)
    {
        DTO.Animation animation = context.getCurrentAnimation();
        if(animation == null)
        {
            ImGui.text("No animation selected");
            return;
        }

        ImGui.text(animation.animationName);
        ImGui.separator();

        if(context.isPlayingAnimation())
        {
            if(ImGui.button("Pause"))
            {
                 context.pauseAnimation();
            }
        }
        else
        {
            if(ImGui.button("Play"))
            {
                context.startAnimation();
            }
        }
        ImGui.sameLine();
        if(ImGui.button("Stop"))
        {
            context.stopAnimation();
        }

        ImGui.sameLine();

        ImFloat duration = new ImFloat(animation.animationDuration);
        if(ImGui.inputFloat("Animation Duration", duration))
        {
            if(duration.get() < 0) duration.set(0);
            context.execute(new PropertyEditCommand<>("animationDuration", duration.get(), animation));
        }


        if(ImGui.button("Add Frame..."))
        {
            ImGui.openPopup("Add Frame?");
        }
        drawAddFramePopup();

        ImGui.sameLine();

        if (ImGui.button("Delete animation..."))
        {
            ImGui.openPopup("Delete Animation?");
        }
        drawDeleteAnimationPopup();


        if(ImGui.collapsingHeader("Frames"))
        {

            for(int i = 0; i < animation.frames.size; ++i)
            {
                drawPerFrameControl(animation, animation.frames.get(i), i);
            }
        }
    }

    private void drawPerFrameControl(DTO.Animation animation, DTO.AnimationFrame frame, int frameNumber)
    {
        ImGui.pushID(Utils.getUniqueKey(frame));

        if (ImGui.button("/\\"))
        {
            int index = animation.frames.indexOf(frame, true);
            if (index > 0)
            {
                int swapIndex = index - 1;
                context.execute(new ArraySwapCommand(animation.frames, index, swapIndex));
            }
        }
        ImGui.sameLine();
        if (ImGui.button("\\/"))
        {
            int index = animation.frames.indexOf(frame, true);
            if (index < animation.frames.size - 1)
            {
                int swapIndex = index + 1;
                context.execute(new ArraySwapCommand(animation.frames, index, swapIndex));
            }
        }

        ImGui.sameLine();

        if(ImGui.button("Delete Frame..."))
        {
            ImGui.openPopup("Delete Frame?");
        }
        drawDeleteFramePopup(animation, frame);

        ImGui.sameLine();

        ImGui.setNextItemWidth(120);
        if(ImGui.selectable("Frame " + frameNumber, context.getAnimationFrame() == frame))
        {
            context.setAnimationFrame(frame);
            context.stopAnimation();
        }

        ImGui.popID();
    }

    private void drawDeleteFramePopup(DTO.Animation animation, DTO.AnimationFrame frame)
    {
        ImGui.setNextWindowSize(128, 56);
        if (ImGui.beginPopupModal("Delete Frame?", ImGuiWindowFlags.NoResize))
        {
            if (ImGui.button("Delete"))
            {
                context.execute(new RemoveFrameCommand(animation, frame));
                if (frame == context.getAnimationFrame())
                {
                    context.setAnimationFrame(null);
                }
                ImGui.closeCurrentPopup();
            }
            ImGui.sameLine();
            if (ImGui.button("Cancel"))
            {
                ImGui.closeCurrentPopup();
            }

            ImGui.endPopup();
        }
    }

    private void drawDeleteAnimationPopup()
    {
        if(ImGui.beginPopupModal("Delete Animation?", ImGuiWindowFlags.NoResize))
        {
            if(ImGui.button("Delete"))
            {
                context.execute(new DeleteAnimationCommand(context.getCharacter().animations, context.getCurrentAnimation()));
                context.setCurrentAnimation(null);

                ImGui.closeCurrentPopup();
            }
            ImGui.sameLine();
            if(ImGui.button("Cancel"))
            {
                ImGui.closeCurrentPopup();
            }

            ImGui.endPopup();
        }
    }

    private void drawAddFramePopup()
    {
        if(ImGui.beginPopupModal("Add Frame?", ImGuiWindowFlags.NoResize))
        {
            ImGui.inputInt("Frame Index", addFrameIdx);

            if(ImGui.button("select texture..."))
            {
                FileHandle directory = Gdx.files.internal("characters/" + context.getCharacter().name + "/animations/");
                String readTexture = Utils.chooseFileToLoad(directory, "png", "jpg", "jpeg");

                if(readTexture != null)
                {
                    addFrameTexturePath = readTexture;

                    String workingDir = Gdx.files.getLocalStoragePath();
                    addFrameTexturePath = addFrameTexturePath.replace(workingDir, "");
                }
            }
            ImGui.text(addFrameTexturePath);

            if(ImGui.button("Confirm"))
            {
                if(!addFrameTexturePath.isEmpty())
                {
                    DTO.AnimationFrame newFrame = new DTO.AnimationFrame();
                    newFrame.texturePath = addFrameTexturePath;

                    Command c = new AddFrameCommand(context.getCurrentAnimation(), addFrameIdx.get(), newFrame);
                    context.execute(c);

                    ImGui.closeCurrentPopup();
                }
            }
            ImGui.sameLine();
            if(ImGui.button("Cancel"))
            {
                ImGui.closeCurrentPopup();
            }

            ImGui.endPopup();
        }
    }
}
