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

import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.atomic.AtomicReference;

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

    public abstract void buildUI(Table table, Skin skin);
    public abstract void update(float dt);
    public abstract void render();

    public void setViewport(Viewport viewport)
    {
        this.viewport = viewport;
        uiStage.setViewport(viewport);
    }

    public Viewport getViewport()
    {
        return viewport;
    }

    protected final void addInputProcessor(InputProcessor processor)
    {
        multiplexer.addProcessor(processor);
    }
    protected final void removeInputProcessor(InputProcessor processor)
    {
        multiplexer.removeProcessor(processor);
    }

    protected final void transitionTo(Class<? extends GameScreen> screenClass)
    {
        AtomicReference<GameScreen> nextScreen = new AtomicReference<>();
        Action loadScreenAction = new Action()
        {
            @Override
            public boolean act(float delta)
            {
                try
                {
                    nextScreen.set(screenClass.getDeclaredConstructor(Game.class).newInstance(game));
                }
                catch (NullPointerException | NoSuchMethodException | InvocationTargetException | IllegalAccessException | InstantiationException e)
                {
                    throw new RuntimeException(e);
                }

                return true;
            };
        };

        Action setScreenAction = new Action()
        {
            @Override
            public boolean act(float delta)
            {
                game.setScreen(nextScreen.get());
                return true;
            }
        };

        uiStage.addAction(Actions.sequence(Actions.fadeOut(0.65f), loadScreenAction, setScreenAction));
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
        uiStage.addAction(Actions.sequence(Actions.alpha(0), Actions.fadeIn(0.65f)));
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
