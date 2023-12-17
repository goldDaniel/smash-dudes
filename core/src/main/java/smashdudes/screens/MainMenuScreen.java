package smashdudes.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import smashdudes.graphics.RenderResources;
import smashdudes.screens.characterselect.CharacterSelectScreen;

public class MainMenuScreen extends GameScreen
{
    public MainMenuScreen(Game game)
    {
        super(game);
    }

    @Override
    public void buildUI(Table table, Skin skin)
    {
        table.top();

        table.add(new Label("Smash Dudes", skin, "splash_title"));
        table.row();

        Action playAction = new Action()
        {
            @Override
            public boolean act(float delta)
            {
                transitionTo(CharacterSelectScreen.class);
                return true;
            }
        };
        table.add(createButton("Play", playAction, table, skin)).padTop(256);
        table.row();

        Action settingsAction = new Action()
        {
            @Override
            public boolean act(float delta)
            {
                transitionTo(SettingsScreen.class);
                return true;
            }
        };
        table.add(createButton("Settings", settingsAction, table, skin)).padTop(64);
        table.row();

        Action extiAction = Actions.sequence(Actions.fadeOut(1f), new Action()
        {
            @Override
            public boolean act(float delta)
            {
                Gdx.app.exit();
                return true;
            }
        });
        table.add(createButton("Exit", extiAction, table, skin)).padTop(64);
    }

    @Override
    public void update(float dt)
    {

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

    private TextButton createButton(String text, Action action, Table table, Skin skin)
    {
        TextButton result = new TextButton(text, skin, "text_button_main_menu");
        result.addListener(new ChangeListener()
        {
            @Override
            public void changed(ChangeEvent event, Actor actor)
            {
                table.addAction(action);
                result.removeListener(this);
            }
        });

        return result;
    }
}
