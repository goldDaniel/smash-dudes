package smashdudes.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import smashdudes.graphics.RenderResources;
import smashdudes.graphics.ui.GameSkin;

public class SettingsScreen extends GameScreen
{
    private GameSkin skin;
    private Stage uiStage;

    private ExtendViewport viewport;

    public SettingsScreen(Game game)
    {
        super(game);

        skin = new GameSkin();
        viewport = new ExtendViewport(1280, 720);
        uiStage = new Stage(viewport, RenderResources.getSpriteBatch());

        Table table = new Table();
        table.setFillParent(true);
        uiStage.addActor(table);
        table.top();

        Label settingsLabel = new Label("Settings", skin, "splash_continue");
        table.add(settingsLabel).padTop(100);
        table.row();

        CheckBox fullscreenCheckbox = new CheckBox("", skin, "checkbox_settings");
        fullscreenCheckbox.setChecked(!Gdx.graphics.isFullscreen());
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
        table.add(fullscreenCheckbox).padTop(20);
        table.add(new Label("Fullscreen", skin, "splash_continue"));
        table.row();

        Slider masterVolumeSlider = new Slider(0f, 100f, 1f, false, skin, "slider_settings");
        table.add(masterVolumeSlider).padTop(20);
        table.add(new Label("Volume", skin, "splash_continue"));
        table.row();

        TextButton backButton = new TextButton("Back", skin, "text_button_main_menu");
        backButton.addListener(new ChangeListener()
        {
            @Override
            public void changed(ChangeEvent event, Actor actor)
            {
                Action sequence = Actions.sequence(Actions.fadeOut(1f), new Action()
                {
                    @Override
                    public boolean act(float delta)
                    {
                        game.setScreen(new MainMenuScreen(game));
                        return true;
                    }
                });

                table.addAction(sequence);
            }
        });
        table.add(backButton).padTop(100);
        table.addAction(Actions.sequence(Actions.alpha(0), Actions.fadeIn(0.5f)));
    }

    @Override
    public void resize(int width, int height)
    {
        viewport.update(width, height);
        viewport.apply();
    }

    @Override
    public void show()
    {
        Gdx.input.setInputProcessor(uiStage);
    }

    @Override
    public void hide()
    {
        Gdx.input.setInputProcessor(null);
    }

    @Override
    public void update(float dt)
    {
        uiStage.act(dt);
    }

    @Override
    public void render()
    {
        ScreenUtils.clear(0,0,0,1);
        uiStage.draw();
    }
}
