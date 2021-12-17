package smashdudes.core.boxtool.presentation;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Graphics;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.FloatArray;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import imgui.ImGui;
import imgui.flag.ImGuiWindowFlags;
import imgui.gl3.ImGuiImplGl3;
import imgui.glfw.ImGuiImplGlfw;
import imgui.type.ImInt;
import imgui.type.ImString;
import smashdudes.core.RenderResources;
import smashdudes.core.boxtool.logic.ContentService;
import smashdudes.core.boxtool.presentation.commands.*;
import smashdudes.core.boxtool.presentation.viewmodel.VM;

public class UI
{
    private ContentService service = new ContentService();

    //State--------------------------------------------------
    private CommandList commandList = new CommandList();

    VM.Character character = null;

    String addFrameTexture = "";
    ImInt addFrameIdx = new ImInt();

    ImString addAnimationName = new ImString();

    boolean playing = false;
    Animation<VM.AnimationFrame> currentAnimation = null;
    private VM.Animation selectedAnimation = null;
    private VM.AnimationFrame selectedAnimationFrame = null;
    //State--------------------------------------------------

    //Rendering---------------------------------------------
    private ImGuiImplGlfw imGuiGlfw = new ImGuiImplGlfw();
    private ImGuiImplGl3 imGuiGl3 = new ImGuiImplGl3();

    private final SpriteBatch sb;
    private final ShapeRenderer sh;

    private OrthographicCamera camera;
    private ExtendViewport viewport;

    private Vector2 texturePos = new Vector2(3, 0);
    private float currentTime = 0;
    //Rendering---------------------------------------------

    public UI(SpriteBatch sb, ShapeRenderer sh)
    {
        ImGui.createContext();
        this.sb = sb;
        this.sh = sh;

        int WORLD_WIDTH = 16;
        int WORLD_HEIGHT = 9;

        camera = new OrthographicCamera(WORLD_WIDTH, WORLD_HEIGHT);
        camera.zoom = 1/2.f;
        texturePos.x = texturePos.x * camera.zoom;
        viewport = new ExtendViewport(WORLD_WIDTH, WORLD_HEIGHT, camera);

        long windowHandle = ((Lwjgl3Graphics) Gdx.graphics).getWindow().getWindowHandle();
        imGuiGlfw.init(windowHandle, true);
        imGuiGl3.init();
    }

    public void resize(int w, int h)
    {
        viewport.update(w,h);
        viewport.apply();
    }

    public void draw()
    {
        if(Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT) &&
           Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT) &&
           Gdx.input.isKeyJustPressed(Input.Keys.Z))
        {
            commandList.redo();
        }
        else if(Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT) &&
                Gdx.input.isKeyJustPressed(Input.Keys.Z))
        {
            commandList.undo();
        }

        float dt = Gdx.graphics.getDeltaTime();
        camera.update();

        imGuiGlfw.newFrame();
        ScreenUtils.clear(0,0,0,1);
        ImGui.newFrame();

        drawMainMenuBar();


        if(character != null)
        {
            drawCharacterData();

            if (selectedAnimation != null && playing)
            {
                playAnimation(dt);
            }
            else
            {
                currentAnimation = null;
            }

            if (selectedAnimationFrame != null)
            {
                sb.setProjectionMatrix(camera.combined);
                sb.begin();
                drawTexture(sb);
                sb.end();

                sh.setProjectionMatrix(camera.combined);
                sh.begin(ShapeRenderer.ShapeType.Line);
                drawAttackData(sh);
                sh.end();
            }
        }

        ImGui.showDemoWindow();

        ImGui.render();

        imGuiGl3.renderDrawData(ImGui.getDrawData());
    }

    public void dispose()
    {
        imGuiGl3.dispose();
        imGuiGlfw.dispose();
        ImGui.destroyContext();
    }

    private void drawMainMenuBar()
    {
        ImGui.beginMainMenuBar();
        {
            if(ImGui.beginMenu("File"))
            {
                if(ImGui.menuItem("Load..."))
                {
                    String filepath = Utils.chooseFileToLoad("json");
                    character = VM.mapping(service.readCharacter(filepath));
                    commandList.clear();
                }

                ImGui.endMenu();
            }
            if(ImGui.beginMenu("Edit"))
            {
                if(ImGui.menuItem("Redo"))
                {
                    commandList.redo();
                }
                if(ImGui.menuItem("Undo"))
                {
                    commandList.undo();
                }
                ImGui.endMenu();
            }
        }
        ImGui.endMainMenuBar();
    }

    private void drawCharacterData()
    {
        ImGui.begin("Character Data", ImGuiWindowFlags.NoCollapse);

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

    private void drawAnimationFrameData(VM.Animation anim)
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


    private void drawTexture(SpriteBatch sb)
    {
        float w = character.drawDim.x;
        float h = character.drawDim.y;
        float x = texturePos.x - w / 2 ;
        float y = texturePos.y  - h / 2;
        sb.draw(RenderResources.getTexture(selectedAnimationFrame.texturePath), x, y, w, h);
    }

    private void drawAttackData(ShapeRenderer sh)
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

    private void drawBoxEditor(String name, Array<FloatArray> boxes)
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

    private void playAnimation(float dt)
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
