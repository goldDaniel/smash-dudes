package smashdudes.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.ScreenUtils;

public class SplashScreen extends GameScreen
{

    public SplashScreen(Game game)
    {
        super(game);
    }

    @Override
    public void buildUI(Table table, Skin skin)
    {
        table.top();
        table.add(new Label("Smash Dudes", skin, "splash_title"));
        table.row();

        Label continueLabel = new Label("Press ENTER to continue", skin, "splash_continue");
        Action continueAction = Actions.forever(Actions.sequence(Actions.fadeOut(1f), Actions.fadeIn(1f)));
        continueLabel.addAction(continueAction);
        table.add(continueLabel).padTop(400);
    }

    @Override
    public void update(float dt)
    {
        if(Gdx.input.isKeyJustPressed(Input.Keys.ENTER))
        {
            transitionTo(new MainMenuScreen(game));
        }
    }

    @Override
    public void render()
    {
        ScreenUtils.clear(0,0,0,1);
    }
}
