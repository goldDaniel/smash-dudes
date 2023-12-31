package smashdudes.boxtool;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import imgui.ImGui;
import imgui.flag.ImGuiWindowFlags;
import imgui.type.ImString;
import smashdudes.core.logic.BoxToolContext;
import smashdudes.core.logic.ContentService;
import smashdudes.boxtool.presentation.AnimationFrameWidget;
import smashdudes.boxtool.presentation.AnimationViewerWidget;
import smashdudes.boxtool.presentation.AnimationWidget;
import smashdudes.boxtool.presentation.CharacterWidget;
import smashdudes.content.DTO;
import smashdudes.core.Utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class BoxToolUI extends smashdudes.core.UI
{
    //State--------------------------------------------------

    private final BoxToolContext context;
    ImString addCharacterName = new ImString();
    //State--------------------------------------------------


    public BoxToolUI()
    {
        context = new BoxToolContext(getCommandList());

        addWidget(new CharacterWidget(context));
        addWidget(new AnimationWidget(context));
        addWidget(new AnimationFrameWidget(context));
        addWidget(new AnimationViewerWidget(context));
    }

    @Override
    protected void frame()
    {
        context.incrementTime(Gdx.graphics.getDeltaTime());
        context.endFrame();
    }

    @Override
    protected void drawMainMenuBar()
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
