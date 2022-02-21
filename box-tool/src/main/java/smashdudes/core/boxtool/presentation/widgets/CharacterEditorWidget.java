package smashdudes.core.boxtool.presentation.widgets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
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
import smashdudes.core.boxtool.logic.ContentService;
import smashdudes.core.boxtool.presentation.Utils;
import smashdudes.core.boxtool.presentation.commands.*;
import smashdudes.graphics.RenderResources;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

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

        if(ImGui.button("Save.."))
        {
            String path = Utils.chooseFileToSave();
            if(path != null)
            {
                service.updateCharacter(character, path);
            }
        }

        ImGui.sameLine();

        if(ImGui.button("Change Portrait.."))
        {
            String portraitPath = Utils.chooseFileToLoad(Gdx.files.local("textures"), "png", "jpeg", "jpg");
            FileHandle directory = Gdx.files.internal("characters/" + character.name + "/portrait");
            String path = Gdx.files.getLocalStoragePath().toString() + directory.toString() + "/portrait.png";
            if (portraitPath != null && directory != null)
            {
                try
                {
                    Files.copy(Paths.get(portraitPath), Paths.get(path), StandardCopyOption.REPLACE_EXISTING);
                }
                catch(IOException e)
                {
                    e.printStackTrace();
                }
            }
        }

        ImGui.separator();

        if(ImGui.collapsingHeader("Properties"))
        {
            ImGui.text("Jump Strength: ");
            ImGui.sameLine();
            ImFloat jump = new ImFloat();
            jump.set(character.jumpStrength);
            if (ImGui.inputFloat("##jumpStrengthID", jump))
            {
                commandList.execute(new PropertyEditCommand<>("jumpStrength", jump.get(), character));
            }

            ImGui.text("Air Speed: ");
            ImGui.sameLine();
            ImFloat air = new ImFloat();
            air.set(character.airSpeed);
            if (ImGui.inputFloat("##airSpeedID", air))
            {
                commandList.execute(new PropertyEditCommand<>("airSpeed", air.get(), character));
            }

            ImGui.text("Run Speed: ");
            ImGui.sameLine();
            ImFloat run = new ImFloat();
            run.set(character.runSpeed);
            if (ImGui.inputFloat("##runSpeedID", run))
            {
                commandList.execute(new PropertyEditCommand<>("runSpeed", run.get(), character));
            }

            ImGui.text("Friction: ");
            ImGui.sameLine();
            ImFloat fric = new ImFloat();
            fric.set(character.deceleration);
            if (ImGui.inputFloat("##decelerationID", fric))
            {
                commandList.execute(new PropertyEditCommand<>("deceleration", fric.get(), character));
            }

            ImGui.text("Gravity: ");
            ImGui.sameLine();
            ImFloat gravity = new ImFloat();
            gravity.set(character.gravity);
            if (ImGui.inputFloat("##gravityID", gravity))
            {
                commandList.execute(new PropertyEditCommand<>("gravity", gravity.get(), character));
            }

            ImGui.text("Weight: ");
            ImGui.sameLine();
            ImFloat weight = new ImFloat();
            weight.set(character.weight);
            if (ImGui.inputFloat("##weightID", weight))
            {
                commandList.execute(new PropertyEditCommand<>("weight", weight.get(), character));
            }

            ImGui.text("Terrain Collider");
            ImGui.sameLine();
            float[] dim = {character.terrainCollider.x, character.terrainCollider.y, character.terrainCollider.width, character.terrainCollider.height};
            if (ImGui.inputFloat4("##colliderDimID", dim))
            {
                Rectangle rect = new Rectangle(dim[0], dim[1], dim[2], dim[3]);
                commandList.execute(new PropertyEditCommand<>("terrainCollider", rect, character));
            }

            ImGui.text("Scale:");
            ImGui.sameLine();
            ImFloat scale = new ImFloat();
            scale.set(character.scale);
            if (ImGui.inputFloat("##scaleID", scale))
            {
                commandList.execute(new PropertyEditCommand<>("scale", scale.get(), character));
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
        ImGui.begin(anim.animationName + "##" +Utils.getUniqueKey(anim));
        {
            if (ImGui.button("Add Frame.."))
            {
                addFrameIdx.set(anim.frames.size);
                addFrameTexture = "";
                ImGui.openPopup("Add Frame?");
            }
            ImGui.setNextWindowSize(400, 120);
            if(ImGui.beginPopupModal("Add Frame?", ImGuiWindowFlags.NoResize))
            {
                ImGui.inputInt("Frame Index", addFrameIdx);

                if(ImGui.button("select texture..."))
                {
                    FileHandle directory = Gdx.files.internal("characters/" + character.name + "/animations/");
                    String readTexture = Utils.chooseFileToLoad(directory, "png", "jpg", "jpeg");

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
            ImGui.separator();
            ImGui.text("Animation Frame Data");

            ImGui.text("Animation Duration: ");
            ImGui.sameLine();
            ImFloat duration = new ImFloat();
            duration.set(anim.animationDuration);
            if(ImGui.inputFloat("##animDurationID", duration))
            {
                commandList.execute(new PropertyEditCommand<>("animationDuration", duration.get(), anim));
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

            int frameNumber = 0;
            for (DTO.AnimationFrame frame : anim.frames)
            {
                if (ImGui.collapsingHeader("frame " + frameNumber++))
                {
                    ImGui.pushID(Utils.getUniqueKey(frame));

                    if (ImGui.button("Show frame"))
                    {
                        selectedAnimationFrame = frame;
                    }

                    ImGui.sameLine();
                    if (ImGui.button("Delete Frame.."))
                    {
                        ImGui.openPopup("Delete Frame?");
                    }
                    ImGui.setNextWindowSize(128, 56);
                    if (ImGui.beginPopupModal("Delete Frame?", ImGuiWindowFlags.NoResize))
                    {
                        if (ImGui.button("Delete"))
                        {
                            toRemove.add(frame);
                            if (frame == selectedAnimationFrame)
                            {
                                selectedAnimationFrame = null;
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

                    ImGui.sameLine();
                    changeTexture("characters/" + character.name + "/animations/", frame);

                    ImGui.sameLine();
                    if (ImGui.button("/\\"))
                    {
                        int index = anim.frames.indexOf(frame, true);
                        if (index > 0)
                        {
                            int swapIndex = index - 1;
                            commandList.execute(new ArraySwapCommand(anim.frames, index, swapIndex));
                        }
                    }
                    ImGui.sameLine();
                    if (ImGui.button("\\/"))
                    {
                        int index = anim.frames.indexOf(frame, true);
                        if (index < anim.frames.size - 1)
                        {
                            int swapIndex = index + 1;
                            commandList.execute(new ArraySwapCommand(anim.frames, index, swapIndex));
                        }
                    }
                    ImGui.sameLine();
                    Texture texture = RenderResources.getTexture(frame.texturePath);

                    float frameDrawWidth = 80;
                    float frameDrawHeight = frameDrawWidth * (float) texture.getHeight() / (float) texture.getWidth();

                    ImGui.image(texture.getTextureObjectHandle(),
                            frameDrawWidth, frameDrawHeight,
                            0, 0, 1, 1);


                    drawBoxEditor("Attackboxes", frame.attackboxes);
                    drawBoxEditor("Bodyboxes", frame.bodyboxes);

                    ImGui.text("Projectiles");
                    ImGui.sameLine();
                    if(ImGui.button("Add##projectileID"))
                    {
                        commandList.execute(new AddProjectileCommand(frame.projectiles, new DTO.Projectile()));
                    }
                    for(DTO.Projectile projectile : frame.projectiles)
                    {
                        ImGui.pushID("##" + Utils.getUniqueKey(projectile));
                        ImGui.text("");
                        ImGui.text("" + (frame.projectiles.indexOf(projectile, true) + 1));
                        ImGui.sameLine();

                        float[] speed = {projectile.speed.x, projectile.speed.y};
                        if(ImGui.inputFloat2("speed##projSpeedID", speed))
                        {
                            DTO.Projectile p = projectile.copy();
                            p.speed.x = speed[0];
                            p.speed.y = speed[1];
                            commandList.execute(new ProjectileEditCommand(projectile, p));
                        }

                        float[] dim = {projectile.dim.x, projectile.dim.y};
                        ImGui.text(" ");
                        ImGui.sameLine();
                        if(ImGui.inputFloat2("dimensions##projDimID", dim))
                        {
                            DTO.Projectile p = projectile.copy();
                            p.dim.x = dim[0];
                            p.dim.y = dim[1];
                            commandList.execute(new ProjectileEditCommand(projectile, p));
                        }

                        float[] pos = {projectile.pos.x, projectile.pos.y};
                        ImGui.text(" ");
                        ImGui.sameLine();
                        if(ImGui.inputFloat2("position##projPosID", pos))
                        {
                            DTO.Projectile p = projectile.copy();
                            p.pos.x = pos[0];
                            p.pos.y = pos[1];
                            commandList.execute(new ProjectileEditCommand(projectile, p));
                        }

                        ImFloat knockback = new ImFloat(projectile.knockback);
                        ImGui.text(" ");
                        ImGui.sameLine();
                        if(ImGui.inputFloat("knockback##projKnockID", knockback))
                        {
                            commandList.execute(new PropertyEditCommand<>("knockback", knockback.get(), projectile));
                        }

                        ImFloat damage = new ImFloat(projectile.damage);
                        ImGui.text(" ");
                        ImGui.sameLine();
                        if(ImGui.inputFloat("damage##projDamageID", damage))
                        {
                            commandList.execute(new PropertyEditCommand<>("damage", damage.get(), projectile));
                        }

                        ImFloat lifeTime = new ImFloat(projectile.lifeTime);
                        ImGui.text(" ");
                        ImGui.sameLine();
                        if(ImGui.inputFloat("life time##projLifeTimeID", lifeTime))
                        {
                            commandList.execute(new PropertyEditCommand<>("lifeTime", lifeTime.get(), projectile));
                        }

                        changeTexture("textures/", projectile);

                        ImGui.sameLine();
                        if(ImGui.button("Remove"))
                        {
                            commandList.execute(new RemoveProjectileCommand(frame.projectiles, projectile));
                        }

                        ImGui.popID();
                    }

                    ImGui.popID();
                }
            }
        }
        ImGui.end();

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
            ImGui.text("" + (boxes.indexOf(rect, true) + 1));
            ImGui.sameLine();
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
            for(Rectangle bodybox : selectedAnimationFrame.bodyboxes)
            {
                float w = bodybox.width;
                float h = bodybox.height;
                float x = (bodybox.x - w / 2) + texturePos.x;
                float y = (bodybox.y - h / 2) + texturePos.y;

                sh.rect(x, y, w, h);
            }
            sh.setColor(Color.BLUE);
            for(Rectangle attackbox : selectedAnimationFrame.attackboxes)
            {
                float w = attackbox.width;
                float h = attackbox.height;
                float x = (attackbox.x - w / 2) + texturePos.x;
                float y = (attackbox.y - h / 2) + texturePos.y;

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

    private static <U> void changeTexture(String filePath, U instance)
    {
        if (ImGui.button("Change Texture.."))
        {
            FileHandle directory = Gdx.files.internal(filePath);
            String readTexture = Utils.chooseFileToLoad(directory, "png", "jpg", "jpeg");
            if (readTexture != null)
            {
                String workingDir = Gdx.files.getLocalStoragePath();
                readTexture = readTexture.replace(workingDir, "");
                commandList.execute(new PropertyEditCommand<>("texturePath", readTexture, instance));
            }
        }
    }
}
