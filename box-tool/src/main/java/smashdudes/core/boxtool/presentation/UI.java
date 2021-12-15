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
import com.badlogic.gdx.utils.viewport.Viewport;
import imgui.ImGui;
import imgui.flag.ImGuiButtonFlags;
import imgui.flag.ImGuiWindowFlags;
import imgui.gl3.ImGuiImplGl3;
import imgui.glfw.ImGuiImplGlfw;
import smashdudes.core.RenderResources;
import smashdudes.core.boxtool.logic.ContentService;
import smashdudes.core.boxtool.presentation.commands.RectangleEditCommand;
import smashdudes.core.boxtool.presentation.viewmodel.VM;

import java.util.Objects;

public class UI
{
    private ContentService service = new ContentService();

    //State--------------------------------------------------
    private CommandList commandList = new CommandList();

    VM.Character character = null;

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
    private Viewport viewport;

    private Vector2 texturePos = new Vector2(3, 0);
    private float textureScale = 2;
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
        viewport = new ExtendViewport(WORLD_WIDTH, WORLD_HEIGHT, camera);

        long windowHandle = ((Lwjgl3Graphics) Gdx.graphics).getWindow().getWindowHandle();
        imGuiGlfw.init(windowHandle, true);
        imGuiGl3.init();
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
                currentTime = 0;
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
                    String filepath = Utils.chooseFileToLoad();
                    character = VM.mapping(service.readCharacter(filepath));
                }

                ImGui.endMenu();
            }
            if(ImGui.beginMenu("Edit"))
            {
                if(ImGui.menuItem("Redo"))
                {

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
        ImGui.begin("Character Data", ImGuiWindowFlags.NoResize | ImGuiWindowFlags.NoMove | ImGuiWindowFlags.NoCollapse);

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
        if(anim.usesSpriteSheet)
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
                if(ImGui.button("Delete frame"))
                {
                    toRemove.add(frame);
                    if (frame == selectedAnimationFrame)
                    {
                        selectedAnimationFrame = null;
                    }
                }

                drawBoxEditor("Hitboxes", frame.hitboxes);
                drawBoxEditor("Hurtboxes", frame.hurtboxes);

                ImGui.popID();
            }
        }

        anim.frames.removeAll(toRemove, true);
        toRemove.clear();
    }


    private void drawTexture(SpriteBatch sb)
    {
        sb.draw(RenderResources.getTexture(selectedAnimationFrame.texturePath), texturePos.x - textureScale * character.drawDim.x / 2 ,
                texturePos.y - textureScale * character.drawDim.y / 2, textureScale * character.drawDim.x, textureScale * character.drawDim.y);
    }

    private void drawAttackData(ShapeRenderer sh)
    {
        sh.setColor(Color.RED);
        for(FloatArray hurtbox : selectedAnimationFrame.hurtboxes)
        {
            sh.rect(textureScale * (hurtbox.get(0) - hurtbox.get(2) / 2) + texturePos.x, textureScale * (hurtbox.get(1) - hurtbox.get(3) / 2) + texturePos.y,
                    textureScale * hurtbox.get(2), textureScale * hurtbox.get(3));
        }
        sh.setColor(Color.BLUE);
        for(FloatArray hitbox : selectedAnimationFrame.hitboxes)
        {
            sh.rect(textureScale * (hitbox.get(0) - hitbox.get(2) / 2) + texturePos.x, textureScale * (hitbox.get(1) - hitbox.get(3) / 2) + texturePos.y,
                    textureScale * hitbox.get(2), textureScale * hitbox.get(3));
        }
    }

    private void drawBoxEditor(String name, Array<FloatArray> boxes)
    {
        Array<FloatArray> toRemove = new Array<>();
        ImGui.text(name);
        ImGui.sameLine();
        if(ImGui.button("Add##" + name))
        {
            boxes.add(new FloatArray(4));
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
        boxes.removeAll(toRemove, true);
        toRemove.clear();
    }

    private void playAnimation(float dt)
    {
        float frameDuration = 0;
        if (selectedAnimation.animationName.equals("idle"))
        {
            frameDuration = 1/8f;
        }
        else
        {
            frameDuration = 1/16f;
        }

        if (currentAnimation == null)
        {
            currentAnimation = new Animation<>(frameDuration, selectedAnimation.frames, Animation.PlayMode.LOOP);
        }

        currentTime += dt;
        selectedAnimationFrame = currentAnimation.getKeyFrame(currentTime);
    }
}
