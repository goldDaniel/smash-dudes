package smashdudes.particletool;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import imgui.ImGui;
import imgui.flag.ImGuiWindowFlags;
import imgui.type.ImString;
import smashdudes.content.DTO;
import smashdudes.particletool.logic.ParticleEditorContext;
import smashdudes.particletool.presentation.EffectEditorWidget;
import smashdudes.particletool.presentation.EffectViewerWidget;
import smashdudes.particletool.presentation.EmitterEditorWidget;

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
        context = new ParticleEditorContext(getCommandList(), new DTO.EffectDescription());
        addWidget(new EffectViewerWidget(context));
        addWidget(new EmitterEditorWidget(context));
        addWidget(new EffectEditorWidget(context));
    }

    @Override
    protected void frame()
    {

    }

    @Override
    protected void drawMainMenuBar()
    {
        boolean newEffectPopup = false;
        ImGui.beginMainMenuBar();
        {
            if(ImGui.beginMenu("File"))
            {
                if(ImGui.menuItem("New..."))
                {
                    newEffectPopup = true;

                }
                if(ImGui.menuItem("Save..."))
                {

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

        drawNewEffectPopup(newEffectPopup);
    }

    private void drawNewEffectPopup(boolean show)
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
                createEffectFile(newEffectName.get());
                context.setEffect(loadEffectFile(newEffectName.get()));

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

    private void createEffectFile(String effectName)
    {
        DTO.EffectDescription config = new DTO.EffectDescription();
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

    private DTO.EffectDescription loadEffectFile(String effectName)
    {
        FileHandle jsonFile = Gdx.files.absolute("fx/" + effectName + ".json");
        String jsonString = jsonFile.readString();
        return new Json().fromJson(DTO.EffectDescription.class, jsonString);
    }
}
