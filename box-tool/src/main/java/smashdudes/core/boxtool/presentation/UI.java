package smashdudes.core.boxtool.presentation;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Graphics;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.*;
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

    private String selectedAnimation = null;
    private void drawCharacterData()
    {
        ImGui.begin("Character Data", ImGuiWindowFlags.NoResize | ImGuiWindowFlags.NoMove);
        ImGui.text(service.getFilename());
        ImGui.separator();

        DTO.Character data = service.getCharacter();

        ImGui.text("Animations");
        for(ObjectMap.Entry<String, DTO.Animation> entry : data.animations)
        {
            String animationName = entry.key;
            if(ImGui.selectable(animationName, selectedAnimation == animationName))
            {
                selectedAnimation = animationName;
            }
        }

        if(selectedAnimation != null)
        {
            drawAnimationFrameData(selectedAnimation);
        }

        ImGui.end();
    }

    private ArrayMap<String, Array<FloatArray>> hitboxes = new ArrayMap<>();
    private ArrayMap<String, Array<FloatArray>> hurtboxes = new ArrayMap<>();
    private void drawAnimationFrameData(String selectedAnimation)
    {
        ImGui.separator();
        ImGui.text("Animation Frame Data");

        DTO.Animation anim = service.getCharacter().animations.get(selectedAnimation);

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
            String frameName = "frame " + frameNumber++;
            if(ImGui.collapsingHeader(frameName))
            {
                String hitboxKey = "##" +  selectedAnimation + frameName + "hitbox";
                String hurtboxKey = "##" +  selectedAnimation + frameName + "hurtbox";

                if(!hitboxes.containsKey(hitboxKey))
                {
                    hitboxes.put(hitboxKey, new Array<>());
                    for(Rectangle rect : frame.hitboxes)
                    {
                        FloatArray arr = new FloatArray(4);
                        arr.add(rect.x);
                        arr.add(rect.y);
                        arr.add(rect.width);
                        arr.add(rect.height);
                        hitboxes.get(hitboxKey).add(arr);
                    }
                }

                if(!hurtboxes.containsKey(hurtboxKey))
                {
                    hurtboxes.put(hurtboxKey, new Array<>());
                    for(Rectangle rect : frame.hurtboxes)
                    {
                        FloatArray arr = new FloatArray(4);
                        arr.add(rect.x);
                        arr.add(rect.y);
                        arr.add(rect.width);
                        arr.add(rect.height);
                        hurtboxes.get(hurtboxKey).add(arr);
                    }
                }

                ImGui.text("Hitboxes");
                int hitboxNumber = 0;
                for(FloatArray rect : hitboxes.get(hitboxKey))
                {
                    if(ImGui.inputFloat4(hitboxKey + hitboxNumber++, rect.items))
                    {
                        service.updateHitboxes(selectedAnimation, frameNumber, hitboxes.get(hitboxKey));
                    }
                }

                ImGui.text("Hurtboxes");
                int hurtboxNumber = 0;
                for(FloatArray rect : hurtboxes.get(hurtboxKey))
                {
                    if(ImGui.inputFloat4(hurtboxKey + hurtboxNumber++, rect.items))
                    {
                        service.updateHurtboxes(selectedAnimation, frameNumber, hurtboxes.get(hurtboxKey));
                    }
                }
            }
        }
    }
}
