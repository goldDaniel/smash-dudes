package smashdudes.core;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import imgui.ImGui;
import imgui.flag.ImGuiWindowFlags;
import smashdudes.core.logic.BoxToolContext;

public abstract class ImGuiWidget
{
    private final String title;
    private final int windowFlags;

    protected BoxToolContext context;

    public ImGuiWidget(String title, int windowFlags, BoxToolContext context)
    {
        this.title = title;
        this.windowFlags = windowFlags | ImGuiWindowFlags.NoCollapse;
        this.context = context;
    }

    public final void DrawWindow(ShapeRenderer sh, SpriteBatch sb)
    {
        ImGui.begin(title, windowFlags);
        this.draw(sh, sb);
        ImGui.end();
    }

    protected abstract void draw(ShapeRenderer sh, SpriteBatch sb);
}
