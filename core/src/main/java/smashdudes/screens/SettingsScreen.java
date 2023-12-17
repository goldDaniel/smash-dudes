package smashdudes.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.ScreenUtils;
import smashdudes.core.input.MenuNavigator;

public class SettingsScreen extends GameScreen
{
    public SettingsScreen(Game game)
    {
        super(game);
    }

    @Override
    public void buildUI(Table table, Skin skin, MenuNavigator menuNavigator)
    {
        table.top();

        Label settingsLabel = new Label("Settings", skin, "splash_title");
        table.add(settingsLabel).padTop(100).row();

        Table menu = new Table();

        CheckBox fullscreenCheckbox = new CheckBox("  Fullscreen", skin, "checkbox_settings");
        fullscreenCheckbox.setChecked(Gdx.graphics.isFullscreen());
        fullscreenCheckbox.addListener(new ChangeListener()
        {
            @Override
            public void changed(ChangeEvent event, Actor actor)
            {
                boolean isFullscreen = Gdx.graphics.isFullscreen();
                Graphics.DisplayMode mode = Gdx.graphics.getDisplayMode();
                if(isFullscreen)
                {
                    Gdx.graphics.setWindowedMode(1280, 720);
                }
                else
                {
                    Gdx.graphics.setFullscreenMode(mode);
                }
            }
        });
        menu.add(fullscreenCheckbox).padTop(20).row();

        TextButton backButton = new TextButton("Back", skin, "text_button_main_menu");
        backButton.addListener(new ChangeListener()
        {
            @Override
            public void changed(ChangeEvent event, Actor actor)
            {
                transitionTo(MainMenuScreen.class);
            }
        });
        menu.add(backButton).padTop(100);

        table.add(menu);
        menuNavigator.setButtonGroup(menu);
    }

    @Override
    public void update(float dt)
    {

    }

    @Override
    public void render()
    {
        ScreenUtils.clear(0,0,0,1);
    }
}
