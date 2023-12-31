package smashdudes.particletool;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import imgui.ImGui;
import imgui.flag.ImGuiWindowFlags;
import imgui.type.ImString;
import smashdudes.graphics.effects.ParticleEmitterConfig;
import smashdudes.particletool.logic.ParticleEditorContext;
import smashdudes.particletool.presentation.EmitterEditorWidget;
import smashdudes.particletool.presentation.EffectViewerWidget;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ParticleToolUI extends smashdudes.core.UI
{
    private final ImString newEffectName = new ImString();

    private ParticleEditorContext context;

    @Override
    public void create()
    {
        super.create();
        context = new ParticleEditorContext(getCommandList());
        addWidget(new EffectViewerWidget(context));
        addWidget(new EmitterEditorWidget(context));
    }

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
                if(ImGui.menuItem("Save..."))
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
            ImGui.openPopup("New Effect");
        }
        ImGui.setNextWindowSize(800, 120);
        if(ImGui.beginPopupModal("New Effect", ImGuiWindowFlags.NoResize))
        {
            ImGui.inputText("Effect Name", newEffectName);
            ImGui.beginDisabled(newEffectName.isEmpty());
            if(ImGui.button("Confirm"))
            {
                createParticleFile(newEffectName.get());
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

    private void createParticleFile(String effectName)
    {
        ParticleEmitterConfig config = new ParticleEmitterConfig();

        String jsonOutput = new Json().toJson(config);

        FileHandle jsonFile = Gdx.files.absolute("fx/" + effectName + ".json");

        try
        {
            String parentDir = jsonFile.parent().toString();
            Files.createDirectories(Paths.get(parentDir));

            jsonFile.writeString(jsonOutput, false);
        }
        catch (IOException ignored)
        {

        }
    }
}
