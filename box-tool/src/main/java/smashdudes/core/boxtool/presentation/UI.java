package smashdudes.core.boxtool.presentation;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Graphics;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import imgui.ImGui;
import imgui.flag.ImGuiWindowFlags;
import imgui.gl3.ImGuiImplGl3;
import imgui.glfw.ImGuiImplGlfw;
import smashdudes.content.DTO;
import smashdudes.core.RenderResources;
import smashdudes.core.boxtool.logic.ContentService;

public class UI
{
    private ContentService service = new ContentService();

    private ImGuiImplGlfw imGuiGlfw = new ImGuiImplGlfw();
    private ImGuiImplGl3 imGuiGl3 = new ImGuiImplGl3();

    private final SpriteBatch sb;
    private final ShapeRenderer sh;

    public UI(SpriteBatch sb, ShapeRenderer sh)
    {
        ImGui.createContext();
        this.sb = sb;
        this.sh = sh;

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

            if (selectedAnimation != null && selectedAnimationFrame != null)
            {
                sb.begin();
                drawTexture(sb);
                sb.end();

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

    private DTO.AnimationFrame selectedAnimationFrame = null;
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

                if(ImGui.button("Show frame"))
                {
                    selectedAnimationFrame = frame;
                }
                drawBoxEditor("Hitboxes", frame.hitboxes);
                drawBoxEditor("Hurtboxes", frame.hurtboxes);

                ImGui.popID();
            }
        }
    }

    private Vector2 texturePos = new Vector2(800, 400);
    private float textureScale = 100;
    private void drawTexture(SpriteBatch sb)
    {
        DTO.Character character = service.getCharacter();
        sb.draw(RenderResources.getTexture(selectedAnimationFrame.texturePath), texturePos.x - textureScale * character.drawDim.x / 2 ,
                texturePos.y - textureScale * character.drawDim.y / 2, textureScale * character.drawDim.x, textureScale * character.drawDim.y);
    }

    private void drawAttackData(ShapeRenderer sh)
    {
        sh.setColor(Color.RED);
        for(Rectangle hurtbox : selectedAnimationFrame.hurtboxes)
        {
            sh.rect(textureScale * (hurtbox.x - hurtbox.width / 2) + texturePos.x, textureScale * (hurtbox.y - hurtbox.height / 2) + texturePos.y,
                    textureScale * hurtbox.width, textureScale * hurtbox.height);
        }
        sh.setColor(Color.BLUE);
        for(Rectangle hitbox : selectedAnimationFrame.hitboxes)
        {
            sh.rect(textureScale * (hitbox.x - hitbox.width / 2) + texturePos.x, textureScale * (hitbox.y - hitbox.height / 2) + texturePos.y,
                    textureScale * hitbox.width, textureScale * hitbox.height);
        }
    }

    private void drawBoxEditor(String name, Array<Rectangle> boxes)
    {
        Array<Rectangle> toRemove = new Array<>();
        int boxCounter = 0;
        ImGui.text(name);
        ImGui.sameLine();
        if(ImGui.button("Add##" + name))
        {
            boxes.add(new Rectangle());
        }
        for(Rectangle rect : boxes)
        {
            ImGui.pushID(rect.hashCode());

            String hitboxKey = "##" +  name + rect + boxCounter++;
            float[] temp = {rect.x, rect.y, rect.width, rect.height};
            ImGui.inputFloat4(hitboxKey, temp);
            rect.x = temp[0];
            rect.y = temp[1];
            rect.width = temp[2];
            rect.height = temp[3];

            ImGui.sameLine();
            if(ImGui.button("Remove##" + name + hitboxKey))
            {
                toRemove.add(rect);
            }

            ImGui.popID();
        }
        boxes.removeAll(toRemove, true);
        toRemove.clear();
    }
}
