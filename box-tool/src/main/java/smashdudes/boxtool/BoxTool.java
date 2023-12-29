package smashdudes.boxtool;

import com.badlogic.gdx.ApplicationListener;
import smashdudes.graphics.RenderResources;
import smashdudes.core.UI;

public class BoxTool implements ApplicationListener
{
    UI ui;

    @Override
    public void create()
    {
        RenderResources.init();
        ui = new BoxToolUI(RenderResources.getSpriteBatch(), RenderResources.getShapeRenderer());
    }

    @Override
    public void resize(int width, int height)
    {

    }

    @Override
    public void render()
    {
        ui.RenderUI();
    }

    @Override
    public void pause()
    {

    }

    @Override
    public void resume()
    {

    }

    @Override
    public void dispose()
    {
        ui.dispose();
    }
}
