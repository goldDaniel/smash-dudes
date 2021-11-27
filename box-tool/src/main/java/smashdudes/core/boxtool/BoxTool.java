package smashdudes.core.boxtool;

import com.badlogic.gdx.ApplicationListener;
import smashdudes.core.boxtool.presentation.UI;

public class BoxTool implements ApplicationListener
{
    UI ui;

    @Override
    public void create()
    {
        ui = new UI();
    }

    @Override
    public void resize(int width, int height)
    {

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
