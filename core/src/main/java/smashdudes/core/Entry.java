package smashdudes.core;

import com.badlogic.gdx.Game;
import smashdudes.graphics.RenderResources;
import smashdudes.screens.CharacterSelectScreen;

/**
 * {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms.
 */
public class Entry extends Game
{

    @Override
    public void create()
    {
        RenderResources.init();
        AudioResources.init();

        setScreen(new CharacterSelectScreen(this));
    }
}