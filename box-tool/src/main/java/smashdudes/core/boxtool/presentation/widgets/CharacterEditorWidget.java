package smashdudes.core.boxtool.presentation.widgets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import imgui.ImGui;
import imgui.flag.ImGuiWindowFlags;
import imgui.type.ImFloat;
import imgui.type.ImInt;
import imgui.type.ImString;
import smashdudes.content.DTO;
import smashdudes.core.RenderResources;
import smashdudes.core.boxtool.logic.ContentService;
import smashdudes.core.boxtool.presentation.Utils;
import smashdudes.core.boxtool.presentation.commands.*;

public class CharacterEditorWidget
{
    private static String addFrameTexture = "";
    private static ImInt addFrameIdx = new ImInt();

    private static ImString addAnimationName = new ImString();

    private static CommandList commandList;
    private static ContentService service;
    private static DTO.Character character;

    private static Animation<DTO.AnimationFrame> currentAnimation = null;
    private static DTO.Animation selectedAnimation = null;
    private static DTO.AnimationFrame selectedAnimationFrame = null;

    private static boolean playing = false;

    private static Vector2 texturePos = new Vector2(1.5f, 0);
    private static float currentTime = 0;

    public CharacterEditorWidget()
    {
        throw new IllegalStateException("DO NOT INSTANTIATE");
    }

    public static void reset()
    {
        addFrameTexture = "";
        addFrameIdx = new ImInt();

        addAnimationName = new ImString();

        commandList = null;
        service = null;
        character = null;

        currentAnimation = null;
        selectedAnimation = null;
        selectedAnimationFrame = null;

        playing = false;

        texturePos = new Vector2(1.5f, 0);
        currentTime = 0;
    }

    public static void render(CommandList commandList, ContentService service, DTO.Character character, float dt)
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
                service.updateCharacter(character, path);
            }
        }

        ImGui.separator();

        ImGui.text("Properties");

        ImGui.text("Jump Strength: ");
        ImGui.sameLine();
        ImFloat jump = new ImFloat();
        jump.set(character.jumpStrength);
        if(ImGui.inputFloat("##jumpStrengthID", jump))
        {
            commandList.execute(new JumpEditCommand(character, jump.get()));
        }

        ImGui.text("Gravity: ");
        ImGui.sameLine();
        ImFloat gravity = new ImFloat();
        gravity.set(character.gravity);
        if(ImGui.inputFloat("##gravityID", gravity))
        {
            commandList.execute(new GravityEditCommand(character, gravity.get()));
        }

        ImGui.text("Weight: ");
        ImGui.sameLine();
        ImFloat weight = new ImFloat();
        weight.set(character.weight);
        if(ImGui.inputFloat("##weightID", weight))
        {
            commandList.execute(new WeightEditCommand(character, weight.get()));
        }

        ImGui.text("Terrain Collider");
        ImGui.sameLine();
        float[] dim = {character.terrainCollider.x, character.terrainCollider.y, character.terrainCollider.width, character.terrainCollider.height};
        if(ImGui.inputFloat4("##colliderDimID", dim))
        {
            commandList.execute(new ColliderDimEditCommand(character, dim));
        }

        ImGui.text("Scale:");
        ImGui.sameLine();
        ImFloat scale = new ImFloat();
        scale.set(character.scale);
        if(ImGui.inputFloat("##scaleID", scale))
        {
            commandList.execute(new ScaleEditCommand(character, scale.get()));
        }

        ImGui.separator();

        ImGui.text("Animations");

        ImGui.sameLine();

        if (ImGui.button("Add animation"))
        {
            addAnimationName.set("");
            ImGui.openPopup("Add Animation?");
        }
        ImGui.setNextWindowSize(360, 78);
        if(ImGui.beginPopupModal("Add Animation?", ImGuiWindowFlags.NoResize))
        {
            ImGui.inputText("Animation Name", addAnimationName);

            if(ImGui.button("Confirm"))
            {
                if(!addAnimationName.get().equals(""))
                {
                    DTO.Animation anim = new DTO.Animation();
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

        for(DTO.Animation entry : character.animations)
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

    private static void drawAnimationFrameData(DTO.Animation anim)
    {
        Array<DTO.AnimationFrame> toRemove = new Array<>();
        ImGui.separator();
        ImGui.text("Animation Frame Data");

        ImGui.text("Animation Duration: ");
        ImGui.sameLine();
        ImFloat duration = new ImFloat();
        duration.set(anim.animationDuration);
        if(ImGui.inputFloat("##animDurationID", duration))
        {
            commandList.execute(new AnimationDurationCommand(anim, duration.get()));
        }

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
            ImGui.openPopup("Delete Animation?");
        }
        ImGui.setNextWindowSize(128, 56);
        if(ImGui.beginPopupModal("Delete Animation?", ImGuiWindowFlags.NoResize))
        {
            if(ImGui.button("Delete"))
            {
                commandList.execute(new DeleteAnimationCommand(character.animations, anim));
                if (anim.frames.contains(selectedAnimationFrame, true))
                {
                    selectedAnimationFrame = null;
                }
               selectedAnimation = null;

                ImGui.closeCurrentPopup();
            }
            ImGui.sameLine();
            if(ImGui.button("Cancel"))
            {
                ImGui.closeCurrentPopup();
            }

            ImGui.endPopup();
        }

        ImGui.text("Frames");
        ImGui.sameLine();
        if (ImGui.button("Add Frame.."))
        {
            addFrameIdx.set(0);
            addFrameTexture = "";
            ImGui.openPopup("Add Frame?");
        }
        ImGui.setNextWindowSize(400, 120);
        if(ImGui.beginPopupModal("Add Frame?", ImGuiWindowFlags.NoResize))
        {
            ImGui.inputInt("Frame Index", addFrameIdx);

            if(ImGui.button("select texture..."))
            {
                String readTexture = Utils.chooseFileToLoad("png", "jpg", "jpeg");
                if(readTexture != null)
                {
                    addFrameTexture = readTexture;

                    String workingDir = Gdx.files.getLocalStoragePath();
                    addFrameTexture = addFrameTexture.replace(workingDir, "");
                }
            }
            ImGui.text(addFrameTexture);

            if(ImGui.button("Confirm"))
            {
                if(!addFrameTexture.equals(""))
                {
                    DTO.AnimationFrame newFrame = new DTO.AnimationFrame();
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
        for(DTO.AnimationFrame frame : anim.frames)
        {
            if(ImGui.collapsingHeader("frame " + frameNumber++))
            {
                ImGui.pushID(Utils.getUniqueKey(frame));

                if(ImGui.button("Show frame"))
                {
                    selectedAnimationFrame = frame;
                }

                ImGui.sameLine();
                if (ImGui.button("Delete Frame.."))
                {
                    ImGui.openPopup("Delete Frame?");
                }
                ImGui.setNextWindowSize(128, 56);
                if(ImGui.beginPopupModal("Delete Frame?", ImGuiWindowFlags.NoResize))
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

    private static void drawBoxEditor(String name, Array<Rectangle> boxes)
    {
        Array<Rectangle> toRemove = new Array<>();
        ImGui.text(name);
        ImGui.sameLine();
        if(ImGui.button("Add##" + name))
        {
            Rectangle rect = new Rectangle();

            commandList.execute(new AddBoxCommand(boxes, rect));
        }

        for(Rectangle rect : boxes)
        {
            ImGui.pushID(Utils.getUniqueKey(rect));

            float[] temp = {rect.x, rect.y, rect.width, rect.height};
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

        for(Rectangle r : toRemove)
        {
            commandList.execute(new RemoveBoxCommand(boxes, r));
        }
        toRemove.clear();
    }

    public static void drawTerrainCollider(ShapeRenderer sh)
    {
        if (character != null)
        {
            float w = character.terrainCollider.width;
            float h = character.terrainCollider.height;
            float x = texturePos.x - w / 2 + character.terrainCollider.x;
            float y = texturePos.y - h / 2 + character.terrainCollider.y;
            sh.setColor(Color.GOLD);
            sh.rect(x, y, w, h);
        }
    }

    public static void drawTexture(SpriteBatch sb)
    {
        if(selectedAnimationFrame != null)
        {
            Texture t =  RenderResources.getTexture(selectedAnimationFrame.texturePath);

            float ratio = (float)t.getHeight() / (float)t.getWidth();

            float w = character.scale;
            float h = w * ratio;
            float x = texturePos.x - w / 2 ;
            float y = texturePos.y  - h / 2;
            sb.draw(t, x, y, w, h);
        }

    }

    public static void drawAttackData(ShapeRenderer sh)
    {
        if(selectedAnimationFrame != null)
        {
            sh.setColor(Color.RED);
            for(Rectangle hurtbox : selectedAnimationFrame.hurtboxes)
            {
                float w = hurtbox.width;
                float h = hurtbox.height;
                float x = (hurtbox.x - w / 2) + texturePos.x;
                float y = (hurtbox.y - h / 2) + texturePos.y;

                sh.rect(x, y, w, h);
            }
            sh.setColor(Color.BLUE);
            for(Rectangle hitbox : selectedAnimationFrame.hitboxes)
            {
                float w = hitbox.width;
                float h = hitbox.height;
                float x = (hitbox.x - w / 2) + texturePos.x;
                float y = (hitbox.y - h / 2) + texturePos.y;

                sh.rect(x, y, w, h);
            }
        }
    }

    private static void playAnimation(float dt)
    {
        if(selectedAnimation.frames.isEmpty()) return;
        float frameDuration = selectedAnimation.animationDuration / selectedAnimation.frames.size;

        if (currentAnimation == null)
        {
            currentAnimation = new Animation<>(frameDuration, selectedAnimation.frames, Animation.PlayMode.LOOP);
        }

        currentTime += dt;
        selectedAnimationFrame = currentAnimation.getKeyFrame(currentTime);
    }
}
