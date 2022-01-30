package smashdudes.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import smashdudes.graphics.RenderResources;
import smashdudes.graphics.ui.GameSkin;

public class SplashScreen extends GameScreen
{
    private GameSkin skin;
    private Stage uiStage;
    private ExtendViewport viewport;

    public SplashScreen(Game game)
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

        Label continueLabel = new Label("Press ENTER to continue", skin, "splash_continue");
        Action continueAction = Actions.forever(Actions.sequence(Actions.fadeOut(1f), Actions.fadeIn(1f)));
        continueLabel.addAction(continueAction);
        table.add(continueLabel).padTop(400);
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

        if(Gdx.input.isKeyJustPressed(Input.Keys.ENTER))
        {
            game.setScreen(new MainMenuScreen(game));
        }
    }

    @Override
    public void render()
    {
        ScreenUtils.clear(0,0,0,1);
        uiStage.draw();
    }
}
