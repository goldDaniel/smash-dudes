package smashdudes.core.boxtool.presentation;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Graphics;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import imgui.ImGui;
import imgui.flag.ImGuiWindowFlags;
import imgui.gl3.ImGuiImplGl3;
import imgui.glfw.ImGuiImplGlfw;
import smashdudes.content.DTO;
import smashdudes.core.boxtool.logic.ContentService;

public class UI
{
    private ContentService service = new ContentService();

    private ImGuiImplGlfw imGuiGlfw = new ImGuiImplGlfw();
    private ImGuiImplGl3 imGuiGl3 = new ImGuiImplGl3();

    public UI()
    {
        ImGui.createContext();

        long windowHandle = ((Lwjgl3Graphics) Gdx.graphics).getWindow().getWindowHandle();
        imGuiGlfw.init(windowHandle, true);
        imGuiGl3.init();
    }

    public void draw()
    {
        imGuiGlfw.newFrame();
        ScreenUtils.clear(0,0,0,1);
        ImGui.newFrame();

        drawMainMenuBar();


        if(service.hasLoadedCharacter())
        {
            drawCharacterData();
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
                    service.loadFile();
                }

                ImGui.endMenu();
            }
        }
        ImGui.endMainMenuBar();
    }

    private DTO.Animation selectedAnimation = null;
    private void drawCharacterData()
    {
        ImGui.begin("Character Data", ImGuiWindowFlags.NoResize | ImGuiWindowFlags.NoMove | ImGuiWindowFlags.NoCollapse);
        ImGui.text(service.getFilename());
        if(ImGui.button("Save"))
        {
            service.saveCharacter();
        }

        ImGui.separator();

        DTO.Character data = service.getCharacter();

        ImGui.text("Animations");
        for(DTO.Animation entry : data.animations)
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

    private void drawAnimationFrameData(DTO.Animation anim)
    {
        ImGui.separator();
        ImGui.text("Animation Frame Data");

        ImGui.labelText(anim.usesSpriteSheet + "", "Uses spritesheet");
        if(anim.usesSpriteSheet)
        {
            ImGui.labelText(anim.textureFilePath, "Texture File Path");
        }
        ImGui.labelText(anim.animationName, "Animation Name");

        ImGui.text("Frames");
        int frameNumber = 0;
        for(DTO.AnimationFrame frame : anim.frames)
        {
            if(ImGui.collapsingHeader("frame " + frameNumber++))
            {
                ImGui.pushID(frame.hashCode());

                //NOTE(daniel): we use this array to remove both hitboxes and hurtboxes.
                Array<Rectangle> toRemove = new Array<>();

                //HITBOXES//////////////////////////////////////////////////////////
                int hitboxCounter = 0;
                ImGui.text("Hitboxes");
                ImGui.sameLine();
                if(ImGui.button("Add##hitbox"))
                {
                    frame.hitboxes.add(new Rectangle());
                }
                for(Rectangle rect : frame.hitboxes)
                {
                    ImGui.pushID(rect.hashCode());

                    String hitboxKey = "##" +  anim + frame + rect + hitboxCounter++;
                    float[] temp = {rect.x, rect.y, rect.width, rect.height};
                    ImGui.inputFloat4(hitboxKey, temp);
                    rect.x = temp[0];
                    rect.y = temp[1];
                    rect.width = temp[2];
                    rect.height = temp[3];

                    ImGui.sameLine();
                    if(ImGui.button("Remove##hitbox" + hitboxKey))
                    {
                        toRemove.add(rect);
                    }

                    ImGui.popID();
                }
                frame.hitboxes.removeAll(toRemove, true);
                toRemove.clear();

                //HURTBOXES//////////////////////////////////////////////////////////
                int hurtboxCounter = 0;
                ImGui.text("Hurtboxes");
                ImGui.sameLine();
                if(ImGui.button("Add##hurtbox"))
                {
                    frame.hurtboxes.add(new Rectangle());
                }
                for(Rectangle rect : frame.hurtboxes)
                {
                    ImGui.pushID(rect.hashCode());

                    String hurtboxKey = "##" +  anim + frame + rect + hurtboxCounter++;
                    float[] temp = {rect.x, rect.y, rect.width, rect.height};
                    ImGui.inputFloat4(hurtboxKey, temp);
                    rect.x = temp[0];
                    rect.y = temp[1];
                    rect.width = temp[2];
                    rect.height = temp[3];

                    ImGui.sameLine();
                    if(ImGui.button("Remove##hurtbox" + hurtboxKey))
                    {
                        toRemove.add(rect);
                    }

                    ImGui.popID();
                }
                frame.hurtboxes.removeAll(toRemove, true);

                ImGui.popID();
            }
        }
    }
}
