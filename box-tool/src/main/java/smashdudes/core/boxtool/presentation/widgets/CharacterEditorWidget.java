package smashdudes.core.boxtool.presentation.widgets;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.FloatArray;
import imgui.ImGui;
import imgui.flag.ImGuiWindowFlags;
import imgui.type.ImInt;
import imgui.type.ImString;
import smashdudes.core.RenderResources;
import smashdudes.core.boxtool.logic.ContentService;
import smashdudes.core.boxtool.presentation.Utils;
import smashdudes.core.boxtool.presentation.commands.*;
import smashdudes.core.boxtool.presentation.viewmodel.VM;

public class CharacterEditorWidget
{
    private static String addFrameTexture = "";
    private static ImInt addFrameIdx = new ImInt();

    private static ImString addAnimationName = new ImString();

    private static CommandList commandList;
    private static ContentService service;
    private static VM.Character character;

    private static Animation<VM.AnimationFrame> currentAnimation = null;
    private static VM.Animation selectedAnimation = null;
    private static VM.AnimationFrame selectedAnimationFrame = null;

    private static boolean playing = false;

    private static Vector2 texturePos = new Vector2(1.5f, 0);
    private static float currentTime = 0;

    public CharacterEditorWidget()
    {
        throw new IllegalStateException("DO NOT INSTANTIATE");
    }

    public static void render(CommandList commandList, ContentService service, VM.Character character, float dt)
    {
        CharacterEditorWidget.commandList = commandList;
        CharacterEditorWidget.service = service;
        CharacterEditorWidget.character = character;

        drawCharacterData();

        if (selectedAnimation != null && playing)
        {
            playAnimation(dt);
        }
        else
        {
            currentAnimation = null;
        }
    }

    private static void drawCharacterData()
    {
        ImGui.begin("Character Data: " + character.name, ImGuiWindowFlags.NoCollapse);

        if(ImGui.button("Save"))
        {
            String path = Utils.chooseFileToSave();
            if(path != null)
            {
                service.updateCharacter(VM.mapping(character), path);
            }
        }

        ImGui.separator();

        ImGui.text("Animations");

        ImGui.sameLine();

        if (ImGui.button("Add animation"))
        {
            addAnimationName.set("");
            ImGui.openPopup("Add Animation?");
        }
        if(ImGui.beginPopupModal("Add Animation?"))
        {
            ImGui.inputText("Animation Name", addAnimationName);

            if(ImGui.button("Confirm"))
            {
                if(!addAnimationName.get().equals(""))
                {
                    VM.Animation anim = new VM.Animation();
                    anim.animationName = addAnimationName.get();

                    commandList.execute(new AddAnimationCommand(character.animations, anim));
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

        for(VM.Animation entry : character.animations)
        {
            if(ImGui.selectable(entry.animationName, selectedAnimation == entry))
            {
                selectedAnimation = entry;
            }
        }

        if(selectedAnimation != null)
        {
            drawAnimationFrameData(selectedAnimation);
        }

        ImGui.end();
    }

    private static void drawAnimationFrameData(VM.Animation anim)
    {
        Array<VM.AnimationFrame> toRemove = new Array<>();
        ImGui.separator();
        ImGui.text("Animation Frame Data");

        ImGui.labelText(anim.usesSpriteSheet + "", "Uses spritesheet");
        if (anim.usesSpriteSheet)
        {
            ImGui.labelText(anim.textureFilePath, "Texture File Path");
        }
        ImGui.labelText(anim.animationName, "Animation Name");

        if (ImGui.button("Play/Pause animation"))
        {
            playing = !playing;
        }

        ImGui.sameLine();
        if (ImGui.button("Delete animation"))
        {
            character.animations.removeValue(anim, true);

            if (selectedAnimation.frames.contains(selectedAnimationFrame, true))
            {
                selectedAnimationFrame = null;
            }
            selectedAnimation = null;
        }

        ImGui.text("Frames");
        ImGui.sameLine();
        if (ImGui.button("Add Frame.."))
        {
            addFrameIdx.set(0);
            addFrameTexture = "";
            ImGui.openPopup("Add Frame?");
        }
        if(ImGui.beginPopupModal("Add Frame?"))
        {
            ImGui.inputInt("Frame Index", addFrameIdx);
            ImGui.text(addFrameTexture);
            ImGui.sameLine();
            if(ImGui.button("select texture..."))
            {
                String texture = Utils.chooseFileToLoad("png", "jpg", "jpeg");
                addFrameTexture = texture;
            }

            if(ImGui.button("Confirm"))
            {
                if(!addFrameTexture.equals(""))
                {
                    VM.AnimationFrame newFrame = new VM.AnimationFrame();
                    newFrame.texturePath = addFrameTexture;

                    Command c = new AddFrameCommand(selectedAnimation, addFrameIdx.get(), newFrame);
                    commandList.execute(c);

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


        int frameNumber = 0;
        for(VM.AnimationFrame frame : anim.frames)
        {
            if(ImGui.collapsingHeader("frame " + frameNumber++))
            {
                ImGui.pushID(frame + "");

                if(ImGui.button("Show frame"))
                {
                    selectedAnimationFrame = frame;
                }

                ImGui.sameLine();
                if (ImGui.button("Delete Frame.."))
                {
                    ImGui.openPopup("Delete Frame?");
                }
                if(ImGui.beginPopupModal("Delete Frame?"))
                {
                    if(ImGui.button("Delete"))
                    {
                        toRemove.add(frame);
                        if (frame == selectedAnimationFrame)
                        {
                            selectedAnimationFrame = null;
                        }
                        ImGui.closeCurrentPopup();
                    }
                    ImGui.sameLine();
                    if(ImGui.button("Cancel"))
                    {
                        ImGui.closeCurrentPopup();
                    }

                    ImGui.endPopup();
                }

                ImGui.sameLine();
                if(ImGui.button("/\\"))
                {
                    int index = anim.frames.indexOf(frame, true);
                    if(index > 0)
                    {
                        int swapIndex = index - 1;
                        commandList.execute(new ArraySwapCommand(anim.frames, index, swapIndex));
                    }
                }
                ImGui.sameLine();
                if(ImGui.button("\\/"))
                {
                    int index = anim.frames.indexOf(frame, true);
                    if(index < anim.frames.size - 1)
                    {
                        int swapIndex = index + 1;
                        commandList.execute(new ArraySwapCommand(anim.frames, index, swapIndex));
                    }
                }


                drawBoxEditor("Hitboxes", frame.hitboxes);
                drawBoxEditor("Hurtboxes", frame.hurtboxes);

                ImGui.popID();
            }
        }

        if(toRemove.notEmpty())
        {
            commandList.execute(new RemoveFrameCommand(selectedAnimation, toRemove));
        }
        toRemove.clear();
    }

    private static void drawBoxEditor(String name, Array<FloatArray> boxes)
    {
        Array<FloatArray> toRemove = new Array<>();
        ImGui.text(name);
        ImGui.sameLine();
        if(ImGui.button("Add##" + name))
        {
            FloatArray arr = new FloatArray(4);
            for (int i = 0; i < arr.items.length; i++)
            {
                arr.add(0);
            }

            commandList.execute(new AddBoxCommand(boxes, arr));
        }
        for(FloatArray rect : boxes)
        {
            ImGui.pushID(rect.items + "");

            float[] temp = {rect.items[0], rect.items[1],rect.items[2], rect.items[3]};
            if(ImGui.inputFloat4("", temp))
            {
                commandList.execute(new RectangleEditCommand(rect, temp));
            }

            ImGui.sameLine();
            if(ImGui.button("Remove##" + name + rect))
            {
                toRemove.add(rect);
            }

            ImGui.popID();
        }

        for(FloatArray f : toRemove)
        {
            commandList.execute(new RemoveBoxCommand(boxes, f));
        }
        toRemove.clear();
    }

    public static void drawTexture(SpriteBatch sb)
    {
        if(selectedAnimationFrame != null)
        {
            float w = character.drawDim.x;
            float h = character.drawDim.y;
            float x = texturePos.x - w / 2 ;
            float y = texturePos.y  - h / 2;
            sb.draw(RenderResources.getTexture(selectedAnimationFrame.texturePath), x, y, w, h);
        }

    }

    public static void drawAttackData(ShapeRenderer sh)
    {
        if(selectedAnimationFrame != null)
        {
            sh.setColor(Color.RED);
            for(FloatArray hurtbox : selectedAnimationFrame.hurtboxes)
            {
                float w = hurtbox.get(2);
                float h = hurtbox.get(3);
                float x = (hurtbox.get(0) - w / 2) + texturePos.x;
                float y = (hurtbox.get(1) - h / 2) + texturePos.y;

                sh.rect(x, y, w, h);
            }
            sh.setColor(Color.BLUE);
            for(FloatArray hitbox : selectedAnimationFrame.hitboxes)
            {
                float w = hitbox.get(2);
                float h = hitbox.get(3);
                float x = (hitbox.get(0) - w / 2) + texturePos.x;
                float y = (hitbox.get(1) - h / 2) + texturePos.y;

                sh.rect(x, y, w, h);
            }
        }
    }

    private static  void playAnimation(float dt)
    {
        float frameDuration = 1/16f;
        if (selectedAnimation.animationName.equals("idle"))
        {
            frameDuration = 1/8f;
        }

        if (currentAnimation == null)
        {
            currentAnimation = new Animation<>(frameDuration, selectedAnimation.frames, Animation.PlayMode.LOOP);
        }

        currentTime += dt;
        selectedAnimationFrame = currentAnimation.getKeyFrame(currentTime);
    }
}
