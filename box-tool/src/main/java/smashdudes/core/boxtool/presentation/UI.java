package smashdudes.core.boxtool.presentation;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Graphics;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import imgui.ImGui;
import imgui.gl3.ImGuiImplGl3;
import imgui.glfw.ImGuiImplGlfw;
import smashdudes.core.boxtool.logic.ContentService;
import smashdudes.core.boxtool.presentation.commands.CommandList;
import smashdudes.core.boxtool.presentation.viewmodel.VM;
import smashdudes.core.boxtool.presentation.widgets.CharacterEditorWidget;

public class UI
{
    private ContentService service = new ContentService();

    //State--------------------------------------------------
    private CommandList commandList = new CommandList();
    VM.Character character = null;
    //State--------------------------------------------------

    //Rendering---------------------------------------------
    private ImGuiImplGlfw imGuiGlfw = new ImGuiImplGlfw();
    private ImGuiImplGl3 imGuiGl3 = new ImGuiImplGl3();

    private final SpriteBatch sb;
    private final ShapeRenderer sh;

    private OrthographicCamera camera;
    private ExtendViewport viewport;
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
            CharacterEditorWidget.render(commandList, service, character, dt);

            sb.setProjectionMatrix(camera.combined);
            sb.begin();
            CharacterEditorWidget.drawTexture(sb);
            sb.end();

            sh.setProjectionMatrix(camera.combined);
            sh.begin(ShapeRenderer.ShapeType.Line);
            CharacterEditorWidget.drawAttackData(sh);
            sh.end();
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
}
