package smashdudes.particletool.presentation;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import imgui.ImGui;
import imgui.type.ImBoolean;
import smashdudes.content.DTO;
import smashdudes.core.ImGuiWidget;
import smashdudes.core.Utils;
import smashdudes.core.logic.commands.ArraySwapCommand;
import smashdudes.core.logic.commands.Command;
import smashdudes.graphics.effects.ParticleEmitter;
import smashdudes.graphics.effects.ParticleEmitterConfig;
import smashdudes.particletool.logic.ParticleEditorContext;

public class EffectEditorWidget extends ImGuiWidget
{
    private final ParticleEditorContext context;
    public EffectEditorWidget(ParticleEditorContext context)
    {
        super("Effect Editor", 0);
        this.context = context;
    }

    @Override
    protected void draw(ShapeRenderer sh, SpriteBatch sb)
    {
        DTO.EffectDescription desc = context.getEffect();

        ImGui.text("Effect Emitters");
        ImGui.newLine();
        if(ImGui.button("Add Emitter"))
        {
            ParticleEmitterConfig config = new ParticleEmitterConfig();
            context.execute(new Command()
            {
                @Override
                protected void execute() {context.addEmitter(config); }

                @Override
                protected void undo()  { context.removeEmitter(config); }
            });

        }

        for(ParticleEmitterConfig config : desc.emitterConfigs)
        {
            ImGui.pushID(Utils.getUniqueKey(config));
            ImGui.newLine();
            ImGui.separator();

            if(ImGui.selectable(config.name, context.getSelectedConfig() == config))
            {
                context.setSelectedConfig(config);
            }

            if (ImGui.button("/\\"))
            {
                int index = desc.emitterConfigs.indexOf(config, true);
                if (index > 0)
                {
                    int swapIndex = index - 1;
                    context.execute(new ArraySwapCommand(desc.emitterConfigs, index, swapIndex));
                }
            }
            ImGui.sameLine();
            if (ImGui.button("\\/"))
            {
                int index = desc.emitterConfigs.indexOf(config, true);
                if (index < desc.emitterConfigs.size - 1)
                {
                    int swapIndex = index + 1;
                    context.execute(new ArraySwapCommand(desc.emitterConfigs, index, swapIndex));
                }
            }

            ImGui.sameLine();

            if(ImGui.button("Delete##"))
            {
                context.execute(new Command()
                {
                    @Override
                    protected void execute()
                    {
                        context.removeEmitter(config);
                        context.setSelectedConfig(null);
                    }

                    @Override
                    protected void undo()
                    {
                        context.addEmitter(config);
                    }
                });
            }
            ImGui.sameLine();

            ParticleEmitter emitter = context.getEmitter(config);
            if(emitter != null)
            {
                ImBoolean enabled = new ImBoolean(context.getEmitter(config).enabled);
                if(ImGui.checkbox("Enabled", enabled))
                {
                    context.getEmitter(config).enabled = enabled.get();
                }
            }

            ImGui.popID();
        }
    }
}
