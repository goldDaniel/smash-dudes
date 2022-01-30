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
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import smashdudes.graphics.RenderResources;
import smashdudes.graphics.ui.GameSkin;

public class MainMenuScreen extends GameScreen
{
    GameSkin skin;
    Stage uiStage;

    ExtendViewport viewport;

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

        TextButton playButton = new TextButton("Play", skin, "default");
        playButton.addListener(new ChangeListener()
        {
            @Override
            public void changed(ChangeEvent event, Actor actor)
            {
                Action sequence = Actions.sequence(Actions.fadeOut(1f), new Action()
                {
                    @Override
                    public boolean act(float delta)
                    {
                        game.setScreen(new CharacterSelectScreen(game));
                        return true;
                    }
                });
                table.addAction(sequence);
            }
        });

        table.add(playButton).padTop(256);
        table.row();

        TextButton exitButton = new TextButton("Exit", skin, "default");

        exitButton.addListener(new ChangeListener()
        {
            @Override
            public void changed(ChangeEvent event, Actor actor)
            {
                Action sequence = Actions.sequence(Actions.fadeOut(1f), new Action()
                {
                    @Override
                    public boolean act(float delta)
                    {
                        Gdx.app.exit();
                        return true;
                    }
                });
                table.addAction(sequence);
            }
        });
        table.add(exitButton).padTop(64);
    }

    @Override
    public void show()
    {
        Gdx.input.setInputProcessor(uiStage);
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
}
