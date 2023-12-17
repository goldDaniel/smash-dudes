package smashdudes.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import org.libsdl.SDL;
import smashdudes.core.input.MenuNavigator;
import smashdudes.graphics.RenderResources;

public class SplashScreen extends GameScreen
{

    public SplashScreen(Game game)
    {
        super(game);
    }

    @Override
    public void buildUI(Table table, Skin skin, MenuNavigator menuNavigator)
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
            transitionTo(MainMenuScreen.class);
        }
        for(Controller c : Controllers.getControllers())
        {
            if(c.getButton(SDL.SDL_CONTROLLER_BUTTON_A))
            {
                transitionTo(MainMenuScreen.class);
                break;
            }
        }
    }

    @Override
    public void render()
    {
        SpriteBatch sb = RenderResources.getSpriteBatch();

        float width = getViewport().getWorldWidth();
        float height = getViewport().getWorldHeight();
        sb.setProjectionMatrix(getViewport().getCamera().combined);
        sb.begin();
        sb.draw(RenderResources.getTexture("textures/main_menu.jpg"), 0, 0, width, height);
        sb.end();
    }
}
