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
import imgui.ImVec2;
import imgui.flag.ImGuiConfigFlags;
import imgui.flag.ImGuiWindowFlags;
import imgui.gl3.ImGuiImplGl3;
import imgui.glfw.ImGuiImplGlfw;
import imgui.type.ImString;
import smashdudes.content.DTO;
import smashdudes.core.boxtool.logic.ContentService;
import smashdudes.core.boxtool.presentation.commands.CommandList;
import smashdudes.core.boxtool.presentation.widgets.CharacterEditorWidget;
import smashdudes.graphics.RenderResources;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class UI
{
    private ContentService service = new ContentService();
    private String characterPath;

    //State--------------------------------------------------
    private CommandList commandList = new CommandList();
    DTO.Character character = null;
    ImString addCharacterName = new ImString();
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

        characterPath = Gdx.files.getLocalStoragePath() + "/characters/";

        camera = new OrthographicCamera(WORLD_WIDTH, WORLD_HEIGHT);
        viewport = new ExtendViewport(WORLD_WIDTH, WORLD_HEIGHT, camera);

        long windowHandle = ((Lwjgl3Graphics) Gdx.graphics).getWindow().getWindowHandle();
        imGuiGlfw.init(windowHandle, true);
        imGuiGl3.init();

        ImGui.getIO().addConfigFlags(ImGuiConfigFlags.DockingEnable);
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
            CharacterEditorWidget.drawTerrainCollider(sh);
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
        boolean newCharFlag = false;
        ImGui.beginMainMenuBar();
        {
            if(ImGui.beginMenu("File"))
            {
                if(ImGui.menuItem("New.."))
                {
                    addCharacterName.set("");
                    newCharFlag = true;
                }

                if(ImGui.menuItem("Load..."))
                {
                    String filepath = Utils.chooseFileToLoad("json");
                    if(filepath != null)
                    {
                        loadCharacter(filepath);
                    }
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

        if(newCharFlag)
        {
            ImGui.openPopup("Add New Character");
        }

        ImGui.setNextWindowSize(360, 78);
        if(ImGui.beginPopupModal("Add New Character", ImGuiWindowFlags.NoResize))
        {
            ImGui.inputText("Character Name", addCharacterName);

            if(ImGui.button("Confirm"))
            {
                if(!addCharacterName.get().equals(""))
                {
                    String path = service.createCharacter(characterPath, addCharacterName.get());
                    if(path != null)
                    {
                        createDirectoryStructure(path);
                        loadCharacter(path);
                    }
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

    private void loadCharacter(String filepath)
    {
        CharacterEditorWidget.reset();
        character = service.readCharacter(filepath);
        commandList.clear();
    }

    private void createDirectoryStructure(String filepath)
    {
        try
        {
            filepath = filepath.replace(".json", "");
            Files.createDirectory(Paths.get(filepath));
            Files.createDirectory(Paths.get(filepath + "/animations"));
            Files.createDirectory(Paths.get(filepath + "/portrait"));
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
