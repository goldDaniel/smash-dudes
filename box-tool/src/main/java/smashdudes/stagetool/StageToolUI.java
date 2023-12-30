package smashdudes.stagetool;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import imgui.ImGui;
import imgui.flag.ImGuiWindowFlags;
import imgui.type.ImString;
import smashdudes.content.DTO;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class StageToolUI extends smashdudes.core.UI
{
    private ImString newStageName = new ImString();

    @Override
    protected void frame()
    {

    }

    @Override
    protected void drawMainMenuBar()
    {
        boolean newStagePopup = false;
        ImGui.beginMainMenuBar();
        {
            if(ImGui.beginMenu("File"))
            {
                if(ImGui.menuItem("New..."))
                {
                    newStagePopup = true;
                }
                if(ImGui.menuItem("Load..."))
                {

                }

                ImGui.endMenu();
            }

            if(ImGui.beginMenu("Edit"))
            {
                if(ImGui.menuItem("Redo", "SHIFT-CTRL-Z"))
                {
                    getCommandList().redo();
                }
                if(ImGui.menuItem("Undo", "CTRL-Z"))
                {
                    getCommandList().undo();
                }
                ImGui.endMenu();
            }
        }
        ImGui.endMainMenuBar();

        drawNewStagePopup(newStagePopup);
    }

    private void drawNewStagePopup(boolean show)
    {
        if(show)
        {
            ImGui.openPopup("New Stage");
        }
        ImGui.setNextWindowSize(800, 120);
        if(ImGui.beginPopupModal("New Stage", ImGuiWindowFlags.NoResize))
        {
            ImGui.inputText("Stage name", newStageName);
            ImGui.beginDisabled(newStageName.isEmpty());
            if(ImGui.button("Confirm"))
            {
                createStageFile(newStageName.get());
                ImGui.closeCurrentPopup();
            }
            ImGui.endDisabled();

            ImGui.sameLine();
            if(ImGui.button("Cancel"))
            {
                ImGui.closeCurrentPopup();
            }

            ImGui.endPopup();
        }
    }

    private void createStageFile(String stageName)
    {
        DTO.Stage stage = new DTO.Stage();
        stage.name = stageName;

        String jsonOutput = new Json().toJson(stage);

        FileHandle jsonFile = Gdx.files.absolute("stages/" + stageName + ".json");

        try
        {
            String parentDir = jsonFile.parent().toString();
            Files.createDirectories(Paths.get(parentDir));

            jsonFile.writeString(jsonOutput, false);
        }
        catch (IOException e)
        {

        }
    }
}
