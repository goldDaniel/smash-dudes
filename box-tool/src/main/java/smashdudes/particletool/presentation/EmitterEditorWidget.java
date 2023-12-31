package smashdudes.particletool.presentation;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import imgui.ImGui;
import imgui.type.ImFloat;
import smashdudes.core.ImGuiWidget;
import smashdudes.core.logic.commands.PropertyEditCommand;
import smashdudes.graphics.effects.ParticleEmitterConfig;
import smashdudes.graphics.effects.ParticleEmitterShape;
import smashdudes.particletool.logic.ParticleEditorContext;

public class EmitterEditorWidget extends ImGuiWidget
{
    private final ParticleEditorContext context;
    public EmitterEditorWidget(ParticleEditorContext context)
    {
        super("Emitter Editor", 0);
        this.context = context;
    }

    @Override
    protected void draw(ShapeRenderer sh, SpriteBatch sb)
    {
        if(ImGui.button("Play"))
        {
            context.play();
        }
        ImGui.sameLine();
        if(ImGui.button("Pause"))
        {
            context.stop();
        }

        ParticleEmitterConfig config = context.getParticleEmitterConfig();

        ImGui.pushItemWidth(300);

        ImFloat emissionRate = new ImFloat(config.emissionRate);
        if(ImGui.inputFloat("Emission Rate", emissionRate, 1, 10))
        {
            if(emissionRate.get() < 0) emissionRate.set(0);
            context.execute(new PropertyEditCommand<>("emissionRate", emissionRate.get(), config));
        }

        ParticleEmitterShape[] shapes = ParticleEmitterShape.values();
        if(ImGui.beginCombo("Emission Shape", config.spawnShape.toString()))
        {
            for(ParticleEmitterShape shape : shapes)
            {
                String typeString = shape.toString();
                boolean isSelected = typeString.equalsIgnoreCase(shape.toString());
                if(ImGui.selectable(typeString, isSelected))
                {
                    context.execute(new PropertyEditCommand<>("spawnShape", shape, config));
                }
                if(isSelected)
                {
                    ImGui.setItemDefaultFocus();
                }

            }
            ImGui.endCombo();
        }

        ImGui.sameLine();
        ImFloat shapeScale = new ImFloat(config.spawnShapeScale);
        if(ImGui.inputFloat("Emission Shape Scale", shapeScale))
        {
            if(shapeScale.get() < 0) shapeScale.set(0);
            context.execute(new PropertyEditCommand<>("spawnShapeScale", shapeScale.get(), config));
        }


        ImGui.separator();
        ImGui.text("Lifetime Variables");
        ImGui.newLine();
        ImGui.newLine();

        ImFloat lifetime = new ImFloat(config.initialLife);
        if(ImGui.inputFloat("Lifetime", lifetime))
        {
            if(lifetime.get() < 0) lifetime.set(0);
            context.execute(new PropertyEditCommand<>("initialLife", lifetime.get(), config));
        }

        ImGui.sameLine();

        ImFloat lifetimeVar = new ImFloat(config.initialLifeVar);
        if(ImGui.inputFloat("Lifetime Variable", lifetimeVar))
        {
            if(lifetimeVar.get() < 0) lifetimeVar.set(0);
            context.execute(new PropertyEditCommand<>("initialLifeVar", lifetimeVar.get(), config));
        }

        ImGui.separator();
        ImGui.text("Velocity Variables");
        ImGui.newLine();
        ImGui.newLine();

        ImFloat velocityX = new ImFloat(config.velocity.x);
        if(ImGui.inputFloat("Velocity X", velocityX))
        {
            Vector2 next = new Vector2(velocityX.get(), config.velocity.y);
            context.execute(new PropertyEditCommand<>("velocity", next, config));
        }
        ImGui.sameLine();

        ImFloat velocityVarX = new ImFloat(config.velocityVar.x);
        if(ImGui.inputFloat("Velocity Variable X", velocityVarX))
        {
            Vector2 next = new Vector2(velocityVarX.get(), config.velocityVar.y);
            context.execute(new PropertyEditCommand<>("velocityVar", next, config));
        }

        ImFloat velocityY = new ImFloat(config.velocity.y);
        if(ImGui.inputFloat("Velocity Y", velocityY))
        {
            Vector2 next = new Vector2(config.velocity.x, velocityY.get());
            context.execute(new PropertyEditCommand<>("velocity", next, config));
        }
        ImGui.sameLine();

        ImFloat velocityVarY = new ImFloat(config.velocityVar.y);
        if(ImGui.inputFloat("Velocity Variable Y", velocityVarY))
        {
            Vector2 next = new Vector2(config.velocityVar.x, velocityVarY.get());
            context.execute(new PropertyEditCommand<>("velocityVar", next, config));
        }

        ImGui.separator();
        ImGui.text("Color Variables");
        ImGui.newLine();
        ImGui.newLine();

        float[] startColor = new float[]{config.startColor.r, config.startColor.g, config.startColor.b, config.startColor.a};
        if(ImGui.colorPicker4("Start Color", startColor))
        {
            Color next = new Color(startColor[0], startColor[1], startColor[2], startColor[3]);
            context.execute(new PropertyEditCommand<>("startColor", next, config));
        }

        ImGui.sameLine();

        float[] startColorVar = new float[]{config.startColorVar.r, config.startColorVar.g, config.startColorVar.b, config.startColorVar.a};
        if(ImGui.colorPicker4("Start Color Variable", startColorVar))
        {
            Color next = new Color(startColorVar[0], startColorVar[1], startColorVar[2], startColorVar[3]);
            context.execute(new PropertyEditCommand<>("startColorVar", next, config));
        }

        float[] endColor = new float[]{config.endColor.r, config.endColor.g, config.endColor.b, config.endColor.a};
        if(ImGui.colorPicker4("End Color", endColor))
        {
            Color next = new Color(endColor[0], endColor[1], endColor[2], endColor[3]);
            context.execute(new PropertyEditCommand<>("endColor", next, config));
        }

        ImGui.sameLine();

        float[] endColorVar = new float[]{config.endColorVar.r, config.endColorVar.g, config.endColorVar.b, config.endColorVar.a};
        if(ImGui.colorPicker4("End Color Variable", endColorVar))
        {
            Color next = new Color(endColorVar[0], endColorVar[1], endColorVar[2], endColorVar[3]);
            context.execute(new PropertyEditCommand<>("endColorVar", next, config));
        }

        ImGui.separator();
        ImGui.text("Scale Variables");
        ImGui.newLine();
        ImGui.newLine();

        ImFloat scaleStart = new ImFloat(config.scaleStart);
        if(ImGui.inputFloat("Scale Start", scaleStart))
        {
            if(scaleStart.get() < 0) scaleStart.set(0);
            context.execute(new PropertyEditCommand<>("scaleStart", scaleStart.get(), config));
        }

        ImGui.sameLine();

        ImFloat scaleStartVar = new ImFloat(config.scaleStartVar);
        if(ImGui.inputFloat("Scale Start Variable", scaleStartVar))
        {
            if(scaleStartVar.get() < 0) scaleStartVar.set(0);
            context.execute(new PropertyEditCommand<>("scaleStartVar", scaleStartVar.get(), config));
        }

        ImFloat scaleEnd = new ImFloat(config.scaleEnd);
        if(ImGui.inputFloat("Scale End", scaleEnd))
        {
            if(scaleEnd.get() < 0) scaleEnd.set(0);
            context.execute(new PropertyEditCommand<>("scaleEnd", scaleEnd.get(), config));
        }

        ImGui.sameLine();

        ImFloat scaleEndVar = new ImFloat(config.scaleEndVar);
        if(ImGui.inputFloat("Scale End Variable", scaleEndVar))
        {
            if(scaleEndVar.get() < 0) scaleEndVar.set(0);
            context.execute(new PropertyEditCommand<>("scaleEndVar", scaleEndVar.get(), config));
        }

        ImGui.popItemWidth();
    }
}
