package smashdudes.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import smashdudes.graphics.RenderResources;
import smashdudes.graphics.ui.GameSkin;

public class MainMenuScreen extends GameScreen
{
    private GameSkin skin;
    private Stage uiStage;
    private ExtendViewport viewport;

    public MainMenuScreen(Game game)
    {
        super(game);

        skin = new GameSkin();
        viewport = new ExtendViewport(1280, 720);
        uiStage = new Stage(viewport, RenderResources.getSpriteBatch());
        Table table = new Table();
        table.setFillParent(true);
        uiStage.addActor(table);

        table.top();
        table.add(new Label("Smash Dudes", skin, "splash_title"));
        table.row();

        table.add(createButton("Play", new Action()
        {
            @Override
            public boolean act(float delta)
            {
                game.setScreen(new CharacterSelectScreen(game));
                return true;
            }
        },
        table)).padTop(256);

        table.row();

        table.add(createButton("Settings", new Action()
        {
            @Override
            public boolean act(float delta)
            {
                game.setScreen(new SettingsScreen(game));
                return true;
            }
        }, table)).padTop(64);

        table.row();

        table.add(createButton("Exit", new Action()
        {
            @Override
            public boolean act(float delta)
            {
                Gdx.app.exit();
                return true;
            }
        }, table)).padTop(64);
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
    public void resize(int width, int height)
    {
        viewport.update(width, height);
        viewport.apply();
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

    private TextButton createButton(String text, Action action, Table table)
    {
        TextButton result = new TextButton(text, skin, "text_button_main_menu");
        result.addAction(Actions.sequence(Actions.alpha(0), Actions.fadeIn(0.25f)));
        result.addListener(new ChangeListener()
        {
            @Override
            public void changed(ChangeEvent event, Actor actor)
            {
                table.addAction(Actions.sequence(Actions.fadeOut(1f), action));
                result.removeListener(this);
            }
        });

        return result;
    }
}
