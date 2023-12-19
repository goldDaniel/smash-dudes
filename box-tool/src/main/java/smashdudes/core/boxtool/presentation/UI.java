package smashdudes.core.boxtool.presentation;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Graphics;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import imgui.ImGui;
import imgui.flag.ImGuiCond;
import imgui.flag.ImGuiConfigFlags;
import imgui.flag.ImGuiStyleVar;
import imgui.flag.ImGuiWindowFlags;
import imgui.gl3.ImGuiImplGl3;
import imgui.glfw.ImGuiImplGlfw;
import imgui.type.ImBoolean;
import imgui.type.ImString;
import smashdudes.content.DTO;
import smashdudes.core.boxtool.logic.BoxToolContext;
import smashdudes.core.boxtool.logic.ContentService;
import smashdudes.core.boxtool.presentation.widgets.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class UI
{
    //State--------------------------------------------------
    private final BoxToolContext context = new BoxToolContext();
    ImString addCharacterName = new ImString();
    //State--------------------------------------------------

    //Rendering---------------------------------------------
    private ImGuiImplGlfw imGuiGlfw = new ImGuiImplGlfw();
    private ImGuiImplGl3 imGuiGl3 = new ImGuiImplGl3();

    private final SpriteBatch sb;
    private final ShapeRenderer sh;
    //Rendering---------------------------------------------


    Array<ImGuiWidget> widgets = new Array<>();

    public UI(SpriteBatch sb, ShapeRenderer sh)
    {
        ImGui.createContext();
        this.sb = sb;
        this.sh = sh;

        long windowHandle = ((Lwjgl3Graphics) Gdx.graphics).getWindow().getWindowHandle();
        imGuiGlfw.init(windowHandle, true);
        imGuiGl3.init();

        ImGui.getIO().addConfigFlags(ImGuiConfigFlags.DockingEnable);

        widgets.add(new CharacterWidget(context));
        widgets.add(new AnimationWidget(context));
        widgets.add(new AnimationFrameWidget(context));
        widgets.add(new AnimationViewerWidget(context));
    }

    public void draw()
    {
        context.incrementTime(Gdx.graphics.getDeltaTime());

        if(Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT) &&
           Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT) &&
           Gdx.input.isKeyJustPressed(Input.Keys.Z))
        {
            context.redo();
        }
        else if(Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT) &&
                Gdx.input.isKeyJustPressed(Input.Keys.Z))
        {
            context.undo();
        }

        imGuiGlfw.newFrame();
        ScreenUtils.clear(0,0,0,1);
        ImGui.newFrame();


        setupDockspace();
        drawMainMenuBar();

        for(ImGuiWidget widget : widgets)
        {
            widget.DrawWindow(sh, sb);
        }

        teardownDockspace();

        ImGui.render();
        imGuiGl3.renderDrawData(ImGui.getDrawData());
        context.endFrame();
    }

    public void dispose()
    {
        imGuiGl3.dispose();
        imGuiGlfw.dispose();
        ImGui.destroyContext();
    }

    private void setupDockspace()
    {
        int windowFlags = ImGuiWindowFlags.MenuBar |
                          ImGuiWindowFlags.NoDocking |
                          ImGuiWindowFlags.NoTitleBar |
                          ImGuiWindowFlags.NoCollapse |
                          ImGuiWindowFlags.NoResize |
                          ImGuiWindowFlags.NoMove |
                          ImGuiWindowFlags.NoBringToFrontOnFocus |
                          ImGuiWindowFlags.NoNavFocus;

        ImGui.pushStyleVar(ImGuiStyleVar.WindowRounding, 0.0f);
        ImGui.pushStyleVar(ImGuiStyleVar.WindowBorderSize, 0.0f);
        {
            ImGui.setNextWindowPos(0,0, ImGuiCond.Always);
            ImGui.setNextWindowSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            ImGui.begin("DockSpace", new ImBoolean(true), windowFlags);
        }
        ImGui.popStyleVar();
        ImGui.popStyleVar();

        ImGui.dockSpace(ImGui.getID("DockSpace"));
    }

    private void teardownDockspace()
    {
        ImGui.end();
    }

    private void drawMainMenuBar()
    {
        boolean newCharacterPopup = false;
        ImGui.beginMainMenuBar();
        {
            if(ImGui.beginMenu("File"))
            {
                if(ImGui.menuItem("New..."))
                {
                    addCharacterName.set("");
                    newCharacterPopup = true;
                }

                if(ImGui.menuItem("Load..."))
                {
                    FileHandle dir = Gdx.files.internal("characters");
                    String filepath = Utils.chooseFileToLoad(dir, "json");
                    if(filepath != null)
                    {
                        context.setCurrentCharacter(loadCharacter(filepath));
                    }
                }

                ImGui.endMenu();
            }

            if(ImGui.beginMenu("Edit"))
            {
                if(ImGui.menuItem("Redo", "SHIFT-CTRL-Z"))
                {
                    context.redo();
                }
                if(ImGui.menuItem("Undo", "CTRL-Z"))
                {
                    context.undo();
                }
                ImGui.endMenu();
            }
        }
        ImGui.endMainMenuBar();

        ImGui.setNextWindowSize(360, 78);

        if(newCharacterPopup)
        {
            ImGui.openPopup("Add New Character");
        }
        if(ImGui.beginPopupModal("Add New Character", ImGuiWindowFlags.NoResize))
        {
            ImGui.inputText("Character Name", addCharacterName);

            if(ImGui.button("Confirm"))
            {
                if(!addCharacterName.get().isEmpty())
                {
                    String path = new ContentService().createCharacter(Gdx.files.getLocalStoragePath() + "/characters/", addCharacterName.get());
                    if(path != null)
                    {
                        try
                        {
                            createCharacter(path);
                            context.setCurrentCharacter(loadCharacter(path));
                        }
                        catch(IOException e)
                        {
                            // TODO (danielg): exception for control flow, bad-bad-bad
                        }
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

    private void createCharacter(String path) throws IOException
    {
        createDirectoryStructure(path);
        createPortrait(path);
    }

    private DTO.Character loadCharacter(String filepath)
    {
        return new ContentService().readCharacter(filepath);
    }

    private void createDirectoryStructure(String filepath) throws IOException
    {
        filepath = filepath.replace(".json", "");
        Files.createDirectory(Paths.get(filepath));
        Files.createDirectory(Paths.get(filepath + "/animations"));
        Files.createDirectory(Paths.get(filepath + "/portrait"));
    }

    private void createPortrait(String path) throws IOException
    {
        String portraitPath = Utils.chooseFileToLoad(Gdx.files.local("textures"), "png", "jpeg", "jpg");
        if(portraitPath == null) throw new IOException("No portrait chosen");
        Files.copy(Paths.get(portraitPath), Paths.get(path.replace(".json", "/portrait/portrait.png")), StandardCopyOption.REPLACE_EXISTING);
    }
}
