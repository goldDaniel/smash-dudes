package smashdudes.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;

public abstract class GameScreen implements Screen
{
    protected Game game;

    public GameScreen(Game game)
    {
        this.game = game;
    }

    @Override
    public final void render(float dt)
    {
        this.update(dt);
        this.render();
    }

    public abstract void update(float dt);
    public abstract void render();

    @Override
    public void resize(int width, int height)  {}

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    @Override
    public void dispose() {}

    @Override
    public void show() {}
}
