package smashdudes.screens;

import com.badlogic.gdx.*;
import com.badlogic.gdx.controllers.ControllerListener;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import smashdudes.core.input.IGameInputListener;
import smashdudes.core.input.MenuNavigator;
import smashdudes.graphics.RenderResources;
import smashdudes.ui.GameSkin;

import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.atomic.AtomicReference;

public abstract class GameScreen implements Screen
{
    private InputMultiplexer multiplexer;
    private MenuNavigator menuNavigator;

    private Table table;
    private Stage uiStage;
    private Viewport viewport;

    protected Game game;

    private Array<IGameInputListener> gameListeners;

    public GameScreen(Game game)
    {
        this.game = game;

        viewport = new ScreenViewport();
        uiStage = new Stage(viewport, RenderResources.getSpriteBatch());

        multiplexer = new InputMultiplexer(uiStage);
        gameListeners = new Array<>();

        table = new Table();
        table.setFillParent(true);
        uiStage.addActor(table);

        menuNavigator = new MenuNavigator();
        buildUI(table, GameSkin.Get(), menuNavigator);
    }

    @Override
    public final void render(float dt)
    {
        uiStage.act(dt);
        this.update(dt);

        this.render();
        uiStage.draw();
    }

    public abstract void buildUI(Table table, Skin skin, MenuNavigator navigator);
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

    protected final void addInputProcessor(IGameInputListener processor)
    {
        gameListeners.add(processor);

        if(processor instanceof InputProcessor)
        {
            multiplexer.addProcessor((InputProcessor)processor);
        }
        else if(processor instanceof ControllerListener)
        {
            Controllers.addListener((ControllerListener)processor);
        }


    }
    protected final void removeInputProcessor(IGameInputListener processor)
    {
        gameListeners.removeValue(processor, true);

        if(processor instanceof InputProcessor)
        {
            multiplexer.removeProcessor((InputProcessor)processor);
        }
        else if(processor instanceof ControllerListener)
        {
            Controllers.removeListener((ControllerListener)processor);
        }
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
        multiplexer.addProcessor(menuNavigator);
        Controllers.addListener(menuNavigator);

        uiStage.addAction(Actions.sequence(Actions.alpha(0), Actions.fadeIn(0.65f)));
    }

    @Override
    public void hide()
    {
        Controllers.removeListener(menuNavigator);
        while(gameListeners.notEmpty())
        {
            removeInputProcessor(gameListeners.pop());
        }
        Gdx.input.setInputProcessor(null);
    }

    @Override
    public void dispose()
    {
        multiplexer = null;
        Gdx.input.setInputProcessor(null);
    }
}
