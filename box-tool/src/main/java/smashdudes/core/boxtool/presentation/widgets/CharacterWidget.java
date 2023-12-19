package smashdudes.core.boxtool.presentation.widgets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import imgui.ImGui;
import imgui.flag.ImGuiWindowFlags;
import imgui.type.ImFloat;
import imgui.type.ImString;
import smashdudes.content.DTO;
import smashdudes.core.boxtool.logic.BoxToolContext;
import smashdudes.core.boxtool.logic.ContentService;
import smashdudes.core.boxtool.presentation.Utils;
import smashdudes.core.boxtool.presentation.commands.AddAnimationCommand;
import smashdudes.core.boxtool.presentation.commands.PropertyEditCommand;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class CharacterWidget extends ImGuiWidget
{
    private final ImString newAnimationName =  new ImString();

    public CharacterWidget(BoxToolContext context)
    {
        super("Character Editor", 0, context);
    }

    @Override
    public void draw(ShapeRenderer sh, SpriteBatch sb)
    {
        if(context.getCharacter() == null)
        {
            ImGui.text("No character loaded");
            return;
        }

        ImGui.text("Character: " + context.getCharacter().name);
        ImGui.separator();

        if(ImGui.button("Save.."))
        {
            String path = Utils.chooseFileToSave();
            if(path != null)
            {
                new ContentService().updateCharacter(context.getCharacter(), path);
            }
        }

        ImGui.sameLine();

        if(ImGui.button("Change Portrait.."))
        {
            changePortraitDialog();
        }

        if(ImGui.collapsingHeader("Properties"))
        {
            DrawProperties();
        }

        if(ImGui.collapsingHeader("Terrain Collider"))
        {
            DTO.Character character = context.getCharacter();
            float[] dim = {character.terrainCollider.x, character.terrainCollider.y, character.terrainCollider.width, character.terrainCollider.height};
            if (ImGui.inputFloat4("", dim))
            {
                Rectangle rect = new Rectangle(dim[0], dim[1], dim[2], dim[3]);
                context.execute(new PropertyEditCommand<>("terrainCollider", rect, character));
            }
        }

        if(ImGui.collapsingHeader("Animation"))
        {
            if (ImGui.button("Add animation"))
            {
                ImGui.openPopup("Add Animation?");
            }

            drawAddAnimationPopup();

            for(DTO.Animation entry : context.getCharacter().animations)
            {
                if(ImGui.selectable(entry.animationName, context.getCurrentAnimation() == entry))
                {
                    context.setCurrentAnimation(entry);
                }
            }
        }
    }

    private void changePortraitDialog()
    {
        String portraitPath = Utils.chooseFileToLoad(Gdx.files.local("textures"), "png", "jpeg", "jpg");
        FileHandle directory = Gdx.files.internal("characters/" + context.getCharacter().name + "/portrait");
        String path = Gdx.files.getLocalStoragePath() + directory.toString() + "/portrait.png";
        if (portraitPath != null && directory.exists() && directory.isDirectory())
        {
            try
            {
                Files.copy(Paths.get(portraitPath), Paths.get(path), StandardCopyOption.REPLACE_EXISTING);
            }
            catch(IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    private void DrawProperties()
    {
        ImFloat jumpStrength = new ImFloat(context.getCharacter().jumpStrength);
        if(ImGui.inputFloat("Jump Strength", jumpStrength))
        {
            context.execute(new PropertyEditCommand<>("jumpStrength", jumpStrength.get(), context.getCharacter()));
        }

        ImFloat airSpeed = new ImFloat(context.getCharacter().airSpeed);
        if (ImGui.inputFloat("Air Speed", airSpeed))
        {
            context.execute(new PropertyEditCommand<>("airSpeed", airSpeed.get(), context.getCharacter()));
        }

        ImFloat runSpeed = new ImFloat(context.getCharacter().runSpeed);
        if (ImGui.inputFloat("Run Speed", runSpeed))
        {
            context.execute(new PropertyEditCommand<>("runSpeed", runSpeed.get(), context.getCharacter()));
        }

        ImFloat deceleration = new ImFloat(context.getCharacter().deceleration);
        if (ImGui.inputFloat("Deceleration", deceleration))
        {
            context.execute(new PropertyEditCommand<>("deceleration", deceleration.get(), context.getCharacter()));
        }

        ImFloat gravity = new ImFloat(context.getCharacter().gravity);
        if (ImGui.inputFloat("Gravity", gravity))
        {
            context.execute(new PropertyEditCommand<>("gravity", gravity.get(), context.getCharacter()));
        }

        ImFloat weight = new ImFloat(context.getCharacter().weight);
        if (ImGui.inputFloat("Weight", weight))
        {
            context.execute(new PropertyEditCommand<>("weight", weight.get(), context.getCharacter()));
        }

        ImFloat scale = new ImFloat(context.getCharacter().scale);
        if (ImGui.inputFloat("Scale", scale))
        {
            context.execute(new PropertyEditCommand<>("scale", scale.get(), context.getCharacter()));
        }
    }

    private void drawAddAnimationPopup()
    {
        ImGui.setNextWindowSize(360, 78);
        if(ImGui.beginPopupModal("Add Animation?", ImGuiWindowFlags.NoResize))
        {
            ImGui.inputText("Animation Name", newAnimationName);

            if(ImGui.button("Confirm"))
            {
                if(!newAnimationName.get().equals(""))
                {
                    DTO.Animation anim = new DTO.Animation();
                    anim.animationName = newAnimationName.get();

                    context.execute(new AddAnimationCommand(context.getCharacter().animations, anim));
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
}
