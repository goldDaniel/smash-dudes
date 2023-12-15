package smashdudes.screens;

import com.badlogic.gdx.*;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import smashdudes.graphics.RenderResources;
import smashdudes.ui.GameSkin;

public abstract class GameScreen implements Screen
{
    private InputMultiplexer multiplexer;

    private Table table;
    private Stage uiStage;
    private Viewport viewport;

    protected Game game;


    public GameScreen(Game game)
    {
        this.game = game;

        viewport = new ExtendViewport(1280, 720);
        uiStage = new Stage(viewport, RenderResources.getSpriteBatch());

        multiplexer = new InputMultiplexer(uiStage);

        table = new Table();
        table.setFillParent(true);
        uiStage.addActor(table);
        buildUI(table, GameSkin.Get());
    }

    @Override
    public final void render(float dt)
    {
        uiStage.act(dt);
        this.update(dt);

        this.render();
        uiStage.draw();
    }

    public final void rebuildUI()
    {
        table.layout();
    }


    public abstract void buildUI(Table table, Skin skin);
    public abstract void update(float dt);
    public abstract void render();

    public void setViewport(Viewport viewport)
    {
        this.viewport = viewport;
        uiStage.setViewport(viewport);
    }


    protected final void addInputProcessor(InputProcessor processor)
    {
        multiplexer.addProcessor(processor);
    }
    protected final void removeInputProcessor(InputProcessor processor)
    {
        multiplexer.removeProcessor(processor);
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
    public void dispose()
    {
        multiplexer = null;
        Gdx.input.setInputProcessor(null);
    }
}
