package smashdudes.screens;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import smashdudes.graphics.RenderResources;
import smashdudes.graphics.ui.GameSkin;

public abstract class GameScreen implements Screen
{
    private InputMultiplexer multiplexer;
    private GameSkin skin;
    private Stage uiStage;
    private Viewport viewport;

    protected Game game;


    public GameScreen(Game game)
    {
        this.game = game;

        skin = new GameSkin();
        viewport = new ExtendViewport(1280, 720);
        uiStage = new Stage(viewport, RenderResources.getSpriteBatch());

        multiplexer = new InputMultiplexer(uiStage);

        Table table = new Table();
        table.setFillParent(true);
        uiStage.addActor(table);
        buildUI(table, skin);
    }

    @Override
    public final void render(float dt)
    {
        uiStage.act(dt);
        this.update(dt);

        this.render();
        uiStage.draw();
    }

    public abstract void buildUI(Table table, Skin skin);
    public abstract void update(float dt);
    public abstract void render();


    protected final void addInputProcessor(InputProcessor processor)
    {
        multiplexer.addProcessor(processor);
    }

    protected final void transitionTo(GameScreen screen)
    {
        uiStage.addAction(Actions.sequence(Actions.fadeOut(0.5f), new Action()
        {
            @Override
            public boolean act(float delta)
            {
                game.setScreen(screen);
                return true;
            }
        }));
    }

    @Override
    public void resize(int width, int height)
    {
        viewport.update(width, height);
        viewport.apply(true);
    }

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void show()
    {
        Gdx.input.setInputProcessor(multiplexer);
        uiStage.addAction(Actions.sequence(Actions.alpha(0), Actions.fadeIn(0.5f)));
    }

    @Override
    public void hide()
    {
        Gdx.input.setInputProcessor(null);
    }

    @Override
    public void dispose() {}
}
