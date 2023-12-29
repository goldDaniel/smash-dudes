package smashdudes.core;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Graphics;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import imgui.ImGui;
import imgui.ImGuiStyle;
import imgui.flag.*;
import imgui.gl3.ImGuiImplGl3;
import imgui.glfw.ImGuiImplGlfw;
import imgui.type.ImBoolean;
import smashdudes.boxtool.logic.commands.CommandList;
import smashdudes.boxtool.presentation.widgets.ImGuiWidget;
import smashdudes.graphics.RenderResources;

public abstract class UI extends ApplicationAdapter
{
    private final CommandList commandList = new CommandList();

    //Rendering---------------------------------------------
    protected ImGuiImplGlfw imGuiGlfw = new ImGuiImplGlfw();
    protected ImGuiImplGl3 imGuiGl3 = new ImGuiImplGl3();

    protected SpriteBatch sb;
    protected ShapeRenderer sh;
    //Rendering---------------------------------------------

    private boolean firstFrame = true;

    private Array<ImGuiWidget> widgets = new Array<>();

    @Override
    public void create()
    {
        ImGui.createContext();
        RenderResources.init();
        this.sb = RenderResources.getSpriteBatch();
        this.sh = RenderResources.getShapeRenderer();

        long windowHandle = ((Lwjgl3Graphics) Gdx.graphics).getWindow().getWindowHandle();
        imGuiGlfw.init(windowHandle, true);
        imGuiGl3.init();

        ImGui.getIO().addConfigFlags(ImGuiConfigFlags.DockingEnable);
    }

    @Override
    public void render()
    {
        this.RenderUI();
    }

    protected final CommandList getCommandList()
    {
        return commandList;
    }

    protected final void addWidget(ImGuiWidget widget)
    {
        this.widgets.add(widget);
    }

    public final void RenderUI()
    {
        if(Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT) &&
                Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT) &&
                Gdx.input.isKeyJustPressed(Input.Keys.Z))
        {
            commandList.redo();
        }
        else if(Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT) &&
                Gdx.input.isKeyJustPressed(Input.Keys.Z))
        {
            commandList.undo();
        }


        imGuiGlfw.newFrame();
        ScreenUtils.clear(0,0,0,1);
        ImGui.newFrame();

        if(firstFrame)
        {
            firstFrame = false;
            final float scale = ImGui.getWindowDpiScale();
            ImGui.getIO().setFontGlobalScale(scale);
            setupImGuiStyle(ImGui.getStyle());
            ImGui.getStyle().scaleAllSizes(scale);
        }

        setupDockspace();
        drawMainMenuBar();

        for(ImGuiWidget widget : widgets)
        {
            widget.DrawWindow(sh, sb);
        }
        frame();

        teardownDockspace();

        ImGui.render();
        imGuiGl3.renderDrawData(ImGui.getDrawData());

    }

    protected abstract void frame();

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

    protected abstract void drawMainMenuBar();



    private void setupImGuiStyle(ImGuiStyle style)
    {
        style.setAlpha(1.0f);
        style.setDisabledAlpha(0.1000000014901161f);
        style.setWindowPadding(8.0f, 8.0f);
        style.setWindowRounding(10.0f);
        style.setWindowBorderSize(0.0f);
        style.setWindowMinSize(30.0f, 30.0f);
        style.setWindowTitleAlign(0.5f, 0.5f);
        style.setWindowMenuButtonPosition(ImGuiDir.Right);
        style.setChildRounding(5.0f);
        style.setChildBorderSize(1.0f);
        style.setPopupRounding(10.0f);
        style.setPopupBorderSize(0.0f);
        style.setFramePadding(5.0f, 3.5f);
        style.setFrameRounding(5.0f);
        style.setFrameBorderSize(0.0f);
        style.setItemSpacing(5.0f, 4.0f);
        style.setItemInnerSpacing(5.0f, 5.0f);
        style.setCellPadding(4.0f, 2.0f);
        style.setIndentSpacing(5.0f);
        style.setColumnsMinSpacing(5.0f);
        style.setScrollbarSize(15.0f);
        style.setScrollbarRounding(9.0f);
        style.setGrabMinSize(15.0f);
        style.setGrabRounding(5.0f);
        style.setTabRounding(5.0f);
        style.setTabBorderSize(0.0f);
        style.setTabMinWidthForCloseButton(0.0f);
        style.setColorButtonPosition(ImGuiDir.Right);
        style.setButtonTextAlign(0.5f, 0.5f);
        style.setSelectableTextAlign(0.0f, 0.0f);

        float[][] colors = style.getColors();
        colors[ImGuiCol.Text] = new float[]{0.75f, 0.75f, 0.75f, 1.00f};
        colors[ImGuiCol.TextDisabled] = new float[]{0.35f, 0.35f, 0.35f, 1.00f};
        colors[ImGuiCol.WindowBg] = new float[]{0.15f, 0.12f, 0.15f, 0.94f};
        colors[ImGuiCol.ChildBg] = new float[]{0.00f, 0.00f, 0.00f, 0.00f};
        colors[ImGuiCol.PopupBg] = new float[]{0.08f, 0.08f, 0.08f, 0.94f};
        colors[ImGuiCol.Border] = new float[]{0.00f, 0.00f, 0.00f, 0.50f};
        colors[ImGuiCol.BorderShadow] = new float[]{0.00f, 0.00f, 0.00f, 0.00f};
        colors[ImGuiCol.FrameBg] = new float[]{0.00f, 0.00f, 0.00f, 0.54f};
        colors[ImGuiCol.FrameBgHovered] = new float[]{0.37f, 0.14f, 0.14f, 0.67f};
        colors[ImGuiCol.FrameBgActive] = new float[]{0.39f, 0.20f, 0.20f, 0.67f};
        colors[ImGuiCol.TitleBg] = new float[]{0.48f, 0.16f, 0.16f, 1.00f};
        colors[ImGuiCol.TitleBgActive] = new float[]{0.48f, 0.16f, 0.16f, 1.00f};
        colors[ImGuiCol.TitleBgCollapsed] = new float[]{0.48f, 0.16f, 0.16f, 1.00f};
        colors[ImGuiCol.MenuBarBg] = new float[]{0.14f, 0.14f, 0.14f, 1.00f};
        colors[ImGuiCol.ScrollbarBg] = new float[]{0.02f, 0.02f, 0.02f, 0.53f};
        colors[ImGuiCol.ScrollbarGrab] = new float[]{0.31f, 0.31f, 0.31f, 1.00f};
        colors[ImGuiCol.ScrollbarGrabHovered] = new float[]{0.41f, 0.41f, 0.41f, 1.00f};
        colors[ImGuiCol.ScrollbarGrabActive] = new float[]{0.51f, 0.51f, 0.51f, 1.00f};
        colors[ImGuiCol.CheckMark] = new float[]{0.56f, 0.10f, 0.10f, 1.00f};
        colors[ImGuiCol.SliderGrab] = new float[]{1.00f, 0.19f, 0.19f, 0.40f};
        colors[ImGuiCol.SliderGrabActive] = new float[]{0.89f, 0.00f, 0.19f, 1.00f};
        colors[ImGuiCol.Button] = new float[]{1.00f, 0.19f, 0.19f, 0.40f};
        colors[ImGuiCol.ButtonHovered] = new float[]{0.80f, 0.17f, 0.00f, 1.00f};
        colors[ImGuiCol.ButtonActive] = new float[]{0.89f, 0.00f, 0.19f, 1.00f};
        colors[ImGuiCol.Header] = new float[]{0.33f, 0.35f, 0.36f, 0.53f};
        colors[ImGuiCol.HeaderHovered] = new float[]{0.76f, 0.28f, 0.44f, 0.67f};
        colors[ImGuiCol.HeaderActive] = new float[]{0.47f, 0.47f, 0.47f, 0.67f};
        colors[ImGuiCol.Separator] = new float[]{0.32f, 0.32f, 0.32f, 1.00f};
        colors[ImGuiCol.SeparatorHovered] = new float[]{0.32f, 0.32f, 0.32f, 1.00f};
        colors[ImGuiCol.SeparatorActive] = new float[]{0.32f, 0.32f, 0.32f, 1.00f};
        colors[ImGuiCol.ResizeGrip] = new float[]{1.00f, 1.00f, 1.00f, 0.85f};
        colors[ImGuiCol.ResizeGripHovered] = new float[]{1.00f, 1.00f, 1.00f, 0.60f};
        colors[ImGuiCol.ResizeGripActive] = new float[]{1.00f, 1.00f, 1.00f, 0.90f};
        colors[ImGuiCol.Tab] = new float[]{0.07f, 0.07f, 0.07f, 0.51f};
        colors[ImGuiCol.TabHovered] = new float[]{0.86f, 0.23f, 0.43f, 0.67f};
        colors[ImGuiCol.TabActive] = new float[]{0.19f, 0.19f, 0.19f, 0.57f};
        colors[ImGuiCol.TabUnfocused] = new float[]{0.05f, 0.05f, 0.05f, 0.90f};
        colors[ImGuiCol.TabUnfocusedActive] = new float[]{0.13f, 0.13f, 0.13f, 0.74f};
        colors[ImGuiCol.DockingPreview] = new float[]{0.47f, 0.47f, 0.47f, 0.47f};
        colors[ImGuiCol.DockingEmptyBg] = new float[]{0.20f, 0.20f, 0.20f, 1.00f};
        colors[ImGuiCol.PlotLines] = new float[]{0.61f, 0.61f, 0.61f, 1.00f};
        colors[ImGuiCol.PlotLinesHovered] = new float[]{1.00f, 0.43f, 0.35f, 1.00f};
        colors[ImGuiCol.PlotHistogram] = new float[]{0.90f, 0.70f, 0.00f, 1.00f};
        colors[ImGuiCol.PlotHistogramHovered] = new float[]{1.00f, 0.60f, 0.00f, 1.00f};
        colors[ImGuiCol.TableHeaderBg] = new float[]{0.19f, 0.19f, 0.20f, 1.00f};
        colors[ImGuiCol.TableBorderStrong] = new float[]{0.31f, 0.31f, 0.35f, 1.00f};
        colors[ImGuiCol.TableBorderLight] = new float[]{0.23f, 0.23f, 0.25f, 1.00f};
        colors[ImGuiCol.TableRowBg] = new float[]{0.00f, 0.00f, 0.00f, 0.00f};
        colors[ImGuiCol.TableRowBgAlt] = new float[]{1.00f, 1.00f, 1.00f, 0.07f};
        colors[ImGuiCol.TextSelectedBg] = new float[]{0.26f, 0.59f, 0.98f, 0.35f};
        colors[ImGuiCol.DragDropTarget] = new float[]{1.00f, 1.00f, 0.00f, 0.90f};
        colors[ImGuiCol.NavHighlight] = new float[]{0.26f, 0.59f, 0.98f, 1.00f};
        colors[ImGuiCol.NavWindowingHighlight] = new float[]{1.00f, 1.00f, 1.00f, 0.70f};
        colors[ImGuiCol.NavWindowingDimBg] = new float[]{0.80f, 0.80f, 0.80f, 0.20f};
        colors[ImGuiCol.ModalWindowDimBg] = new float[]{0.80f, 0.80f, 0.80f, 0.35f};

        style.setColors(colors);
    }
}
