package smashdudes.core;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import imgui.ImGui;
import imgui.flag.ImGuiWindowFlags;

public abstract class ImGuiWidget
{
    private final String title;
    private final int windowFlags;


    public ImGuiWidget(String title, int windowFlags)
    {
        this.title = title;
        this.windowFlags = windowFlags | ImGuiWindowFlags.NoCollapse;
    }

    public final void DrawWindow(ShapeRenderer sh, SpriteBatch sb)
    {
        ImGui.begin(title, windowFlags);
        this.draw(sh, sb);
        ImGui.end();
    }

    protected abstract void draw(ShapeRenderer sh, SpriteBatch sb);
}
