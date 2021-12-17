package smashdudes.core.boxtool;

import com.badlogic.gdx.ApplicationListener;
import smashdudes.core.RenderResources;
import smashdudes.core.boxtool.presentation.UI;

public class BoxTool implements ApplicationListener
{
    UI ui;

    @Override
    public void create()
    {
        RenderResources.init();

        ui = new UI(RenderResources.getSpriteBatch(), RenderResources.getShapeRenderer());
    }

    @Override
    public void resize(int width, int height)
    {
        ui.resize(width, height);
    }

    @Override
    public void render()
    {
        ui.draw();
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
